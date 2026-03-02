package ast;

import types.Type;

import java.util.Arrays;
import java.util.List;

public class AstCfieldFunc extends AstCfield{
    public AstFuncdec funcDec;

    public AstCfieldFunc(AstFuncdec funcDec, int lineNum) {
        super("CField -> funcDec", lineNum);
        this.funcDec = funcDec;
    }


    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(funcDec);
    }


    @Override
    protected String GetNodeName() {
        return "CFIELD\nFUNCDEC";
    }


    @Override
    public Type SemantMe() { return funcDec.SemantMe(); }
}
