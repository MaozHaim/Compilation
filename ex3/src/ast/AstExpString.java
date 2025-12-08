package ast;

import types.*;

public class AstExpString extends AstExp {
    public String value;

    public AstExpString(String value, int lineNum) {
        super(String.format("exp -> STRING( %s )", value), lineNum);
        this.value = value;
    }


    @Override
    protected String GetNodeName() {
        return String.format("EXP\nSTRING( %s )", value);
    }


    @Override
    public Type SemantMe() {
        return TypeString.getInstance();
    }


    @Override
    public boolean isConstant() {
        return true;
    }
}
