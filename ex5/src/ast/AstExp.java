package ast;

public abstract class AstExp extends AstNode
{
    public AstExp(String derivation, int lineNum) {
		super(derivation, lineNum);
	}

	// to be overridden:
	public boolean isConstant() { return false; }
	public boolean isNewExp() {return false; }
}