package ast;

import types.Type;
import types.TypeClass;
import types.TypeFunction;
import symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Utils.matchTypesArgsParams;

public class AstStmtMethod extends AstStmt {
    public AstVar var;
    public String methodName;
    public List<AstExp> args;

    public AstStmtMethod(AstVar var, String methodName, NodeList<AstExp> args, int lineNum) {
        super("stmt -> var DOT ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // x.method(exp, exp...);
        this.var = var;
        this.methodName = methodName;
        this.args = args.unroll();
    }

    public AstStmtMethod(AstVar var, String methodName, int lineNum){
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // x.method();
        this.var = var;
        this.methodName = methodName;
        this.args = Arrays.asList();
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\nVAR...->%s(%s)", methodName, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        List<AstNode> children = new ArrayList<>(args);
        children.add(0, var);
        return children;
    }

    public Type SemantMe() {
        SymbolTable table = SymbolTable.getInstance();
        Type data = var.SemantMe();

        if (!(data instanceof TypeClass)) {
            throwException("Attempting to call method on a non-class type.");
        }

        TypeClass classData = (TypeClass) data;
        Type methodType = table.findMemberType(classData, methodName);

        if (!(methodType instanceof TypeFunction)) {
            throwException("Attempting to call attribute as a method.");
        }

        TypeFunction functionData = (TypeFunction)methodType;
        Type returnType = functionData.returnType;
        List<Type> argsList = expsToList();
        
        if (!(matchTypesArgsParams(argsList, functionData.params))) {
            throwException("bad parameters or arguments mate.");
        }

        return returnType;
    }

    /**********************************/
    /* Duplication from AstStmtFunc */
    /**********************************/
    private List<Type> expsToList() {
        List<Type> argumentTypes = new ArrayList<>();
        for (AstExp expression : args) {
            argumentTypes.add(expression.SemantMe());
        }
        return argumentTypes;
    }

}
