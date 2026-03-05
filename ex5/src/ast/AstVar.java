package ast;

public abstract class AstVar extends AstNode {
    String id;

    public AstVar(String derivation, int lineNum) {
        super(derivation, lineNum);
    }
}
