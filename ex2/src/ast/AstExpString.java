package ast;

public class AstExpString extends AstExp {
    public String value;

    public AstExpString(String value) {
        super(String.format("exp -> STRING( %s )", value));
        this.value = value;
    }

    @Override
    protected String GetNodeName() {
        return String.format("EXP\nSTRING( %s )", value);
    }
}
