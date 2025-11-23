package ast;

public class AstExpstring extends AstExp {
    public String value;

    public AstExpstring(String value) {
        super(String.format("exp -> STRING( %s )", value));
        this.value = value;
    }

    @Override
    protected String GetNodeName() {
        return String.format("EXP\nSTRING( %s )", value);
    }
}
