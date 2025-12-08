package ast;

public abstract class AstExp extends AstNode
{
    public AstExp(String derivation, int lineNum) {
		super(derivation, lineNum);
	}
}