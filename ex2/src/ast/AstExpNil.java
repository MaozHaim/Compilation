package ast;

public class AstExpNil extends AstExp {
    public AstExpNil() {
        super("exp -> NIL");
    }

    @Override
    protected String GetNodeName() {
        return "NIL";
    }
}
