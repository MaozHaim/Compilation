package ast;

public abstract class AstStmt extends AstNode
{
    public AstStmt(String derivation, int lineNum) {
        super(derivation, lineNum);
    }
}
