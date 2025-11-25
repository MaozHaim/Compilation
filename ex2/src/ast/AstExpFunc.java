package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpFunc extends AstExp {
    public String id;

    public List<AstExp> exps;

    public AstExpFunc(String id) {
        super(String.format("exp -> ID( %s ) LBRACK RBRACK", id));
        this.id = id;
        exps = Arrays.asList();
    }

    public AstExpFunc(String id, NodeList expStar){
        super(String.format("exp -> ID( %s ) LBRACK exp* RBRACK", id));
        this.id = id;
        exps = expStar.unroll();
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return exps;
    }

    @Override
    protected String GetNodeName() {
        return String.format("%s(%s)", id, exps != null ? "PARAMS" : "");
    }
}
