package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstFuncdec extends AstDec {
    public AstType returnType;
    public String id;
    public AstStmtList statements;
    public List<AstFuncParam> params;

    public AstFuncdec(AstType returnType, String id, NodeList<AstFuncParam> params, AstStmtList statements) {
        super("funcDec -> type ID LPAREN (type ID (COMMA type ID)*)? RPAREN LBRACE stmt (stmt)* RBRACE");

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
}
