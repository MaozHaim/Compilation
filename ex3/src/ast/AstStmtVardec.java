package ast;

import types.Type;

import java.util.Arrays;
import java.util.List;

public class AstStmtVardec extends AstStmt {
    public AstVardec dec;

    public AstStmtVardec(AstVardec dec, int lineNum) {
        super("stmt -> vardec", lineNum);
        this.dec = dec;
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(dec);
    }

    @Override
    protected String GetNodeName() {
        return "STMT\nVARDEC";
    }

    @Override public Type SemantMe() {
        return dec.SemantMe();
    }

}
