package ast;

import types.Type;
import types.TypeNil;

public class AstExpNil extends AstExp {
    public AstExpNil(int lineNum) {
        super("exp -> NIL", lineNum);
    }

    @Override
    protected String GetNodeName() {
        return "NIL";
    }

    @Override
    public Type SemantMe() {
        return TypeNil.getInstance();
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
