package ast;

import java.util.Arrays;
import java.util.List;

public class AstStmtFunc extends AstStmt {
    public String id;
    public List<AstExp> args;

    public AstStmtFunc(String id, NodeList<AstExp> args) {
        super("stmt -> ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON");
        this.id = id;
        this.args = args.unroll();
    }

    public AstStmtFunc(String id){
        super("stmt -> ID LPAREN RPAREN SEMICOLON");
        this.id = id;
        this.args = Arrays.asList();
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\n%s(%s)", id, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return args;
    }
}
