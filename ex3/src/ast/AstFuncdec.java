package ast;

import symboltable.SymbolTable;
import types.Type;
import types.TypeFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstFuncdec extends AstDec {
    public AstType returnType;
    public String id;
    public AstStmtList statements;
    public List<AstFuncParam> params;

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
        SymbolTable symbolTable = SymbolTable.getInstance();
        if (symbolTable.getExpectedReturnType() != null)
            throwException("Nested function declaration");

        Type returnType = returnType.SemantMe();
        List<Type> paramTypes = new ArrayList<>();
        TypeFunction thisType = new TypeFunction(returnType, id, paramTypes);
        tryTableEnter(id, thisType);

        symbolTable.beginScope();

        for (AstFuncParam param : params) {
            paramTypes.add(param.SemantMe());
        }

        symbolTable.setExpectedReturnType(returnType);

        statements.SemantMe();

        symbolTable.setExpectedReturnType(null);
        symbolTable.endScope();

        return thisType;
    }
}
