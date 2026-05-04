package ast;

import ir.InitialConstVal;
import ir.IrCommandFuncDec;
import ir.IrCommandGetGlobalAddress;
import ir.IrCommandReturn;
import ir.IrCommandStore;
import symboltable.SymbolTable;
import types.Type;
import types.TypeFunction;
import temp.Temp;
import ir.Ir;
import ir.IrCommandLabel;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstFuncdec extends AstDec {
    public AstType returnType;
    public String id;
    public AstStmtList statements;
    public List<AstFuncParam> params;
    public String className; // Useful for naming the method
    public int localVarAmount; // to be found at the end of SemantMe


    public AstFuncdec(AstType returnType, String id, NodeList<AstFuncParam> params, AstStmtList statements, int lineNum) {
        super("funcDec -> type ID LPAREN (type ID (COMMA type ID)*)? RPAREN LBRACE stmt (stmt)* RBRACE", lineNum);

        this.returnType = returnType;
        this.id = id;
        this.statements = statements;
        if (params != null) this.params = params.unroll();
        else this.params = Arrays.asList();
    }


    @Override
    protected List<? extends AstNode> GetChildren() {
        List<AstNode> children;

        if (params != null) children = new ArrayList<>(params);
        else children = new ArrayList<>();

        children.add(statements);
        children.add(0, returnType);

        return children;
    }


    @Override
    protected String GetNodeName() {
        return String.format("DEC\nFUNC(%s)", id);
    }


    @Override
    public Type SemantMe() {
        // Initialization
        SymbolTable symbolTable = SymbolTable.getInstance();
        if (symbolTable.getExpectedReturnType() != null) // yea this probably never happens
            throwException("Nested function declaration.");
        Type returnType = this.returnType.SemantMe();
        List<Type> paramTypes = new ArrayList<>();
        TypeFunction thisType = new TypeFunction(returnType, id, paramTypes);
        tryTableEnter(id, thisType);

        // Beginning of function
        symbolTable.currentFunction = thisType;
        vardecCounter = 0;
        symbolTable.beginScope();

        System.out.println("\nFunction Name: " + id);
        List<Pair<String, Type>> idTypeList = new ArrayList<>();
        int offset = 1;
        for (AstFuncParam param : params) { // SemantMe on parameters
            String paramName = param.id;
            Type paramType = param.SemantMe(offset);
            paramTypes.add(paramType);
            Pair<String, Type> idType = new Pair<>(paramName, paramType);
            System.out.println("paramName: " + paramName + ", paramType: " + paramType);
            idTypeList.add(idType);
            offset++;
        }

        symbolTable.setExpectedReturnType(returnType);
        statements.SemantMe();
        localVarAmount = vardecCounter; // hope this le works

        if (symbolTable.currentClass != null) { this.className = symbolTable.currentClass.name;}

        symbolTable.setExpectedReturnType(null);
        symbolTable.endScope();
        vardecCounter = 0; // Paranoia
        symbolTable.currentFunction = null;

        return thisType;
    }


    // Prefix user-defined free function labels to avoid clashing with MIPS
    // pseudo-instructions/keywords (e.g. abs, add, sub, mul, move). main is
    // exempt because SPIM expects it as the entry-point label. Class methods
    // already get a Class_method form that's safely unique.
    public static String mangleFunctionLabel(String className, String id) {
        if (className != null) return className + "_" + id;
        if (id.equals("main")) return id;
        return "func_" + id;
    }


    public Temp IRme() {
        Ir ir = Ir.getInstance();
        String labelName = mangleFunctionLabel(className, id);
        ir.AddIrCommand(new IrCommandFuncDec(labelName, localVarAmount));

        for (AstFuncParam param : params) { // Logging purposes
            param.IRme();
        }

        // Per spec §2.3: before entering main, all globals with non-constant
        // initializers are evaluated in source order. SPIM jumps straight to
        // main, so we emit the init code as main's prologue.
        if (id.equals("main") && className == null) {
            for (Pair<String, InitialConstVal> g : AstProgram.getGlobals()) {
                if (!g.second.needsRuntimeInit()) continue;
                Temp val  = g.second.getRuntimeInitExp().IRme();
                Temp addr = new Temp();
                ir.AddIrCommand(new IrCommandGetGlobalAddress(addr, g.first));
                ir.AddIrCommand(new IrCommandStore(val, addr, 0));
            }
        }

        statements.IRme();


        if (!(statements.getStatements().get(statements.getStatements().size() - 1) instanceof AstStmtReturn))
            ir.AddIrCommand(new IrCommandReturn(id.equals("main")));
        return null;
    }
}
