package ast;

import types.Type;

import java.util.List;

public class AstProgram extends AstNode{
    public List<AstDec> declarations;

    public AstProgram(NodeList<AstDec> declarations, int lineNum){
        super("program -> dec+", lineNum);
        this.declarations = declarations.unroll();
    }

    @Override
    public Type SemantMe() {
        for (AstDec dec : declarations)
            dec.SemantMe();
        return null;
    }

    @Override
    protected String GetNodeName() {
        return "PROGRAM\nDECLARATIONS";
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return declarations;
    }
}
