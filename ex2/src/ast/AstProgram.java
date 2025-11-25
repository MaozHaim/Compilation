package ast;

import java.util.List;

public class AstProgram extends AstNode{
    public List<AstDec> declarations;

    public AstProgram(NodeList<AstDec> declarations){
        super("program -> dec+");
        this.declarations = declarations.unroll();
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
