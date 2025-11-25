package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstExpMethod extends AstExp{
    public AstVar var;
    public String methodName;

    public List<AstExp> exps;

    public AstExpMethod(AstVar var, String methodName){
        super("exp -> var DOT ID LPAREN RPAREN");
        this.methodName = methodName;
        this.var = var;
        exps = Arrays.asList();
    }

    public AstExpMethod(AstVar var, String methodName, NodeList expStar){
        super("exp -> var DOT ID LPAREN exp* RPAREN");
        this.methodName = methodName;
        this.var = var;
        exps = expStar.unroll();
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        List<AstNode> children = new ArrayList<>(exps);
        children.add(0, var);
        return children;
    }

    @Override
    protected String GetNodeName() {
        return String.format("VAR.%s(%s)", methodName, exps != null ? "PARAMS" : "");
    }
}
