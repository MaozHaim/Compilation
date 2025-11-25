package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstStmtMethod extends AstStmt {
    public AstVar var;
    public String fieldName;
    public List<AstExp> args;

    public AstStmtMethod(AstVar var, String fieldName, NodeList<AstExp> args) {
        super("stmt -> var DOT ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON");
        this.var = var;
        this.fieldName = fieldName;
        this.args = args.unroll();
    }

    public AstStmtMethod(AstVar var, String fieldName){
        super("stmt -> ID LPAREN RPAREN SEMICOLON");
        this.var = var;
        this.fieldName = fieldName;
        this.args = Arrays.asList();
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\nVAR...->%s(%s)", fieldName, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        List<AstNode> children = new ArrayList<>(args);
        children.add(0, var);
        return children;
    }
}
