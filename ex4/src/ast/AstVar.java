package ast;

public abstract class AstVar extends AstNode
{
    public AstVar(String derivation, int lineNum) {
        super(derivation, lineNum);
    }
}
