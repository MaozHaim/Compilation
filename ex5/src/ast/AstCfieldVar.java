package ast;

import types.Type;

import java.util.Arrays;
import java.util.List;

public class AstCfieldVar extends AstCfield {
    public AstVardec varDec;

    public AstCfieldVar(AstVardec varDec, int lineNum) {
        super("CField -> varDec", lineNum);
        this.varDec = varDec;
    }


    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(varDec);
    }


    @Override
    protected String GetNodeName() {
        return "CFIELD\nVARDEC";
    }


    @Override
    public Type SemantMe() {
        if (varDec.exp != null && !varDec.exp.isConstant()){
            throwException("Field may only be initialized with a constant value.");
        }
        return varDec.SemantMe();
    }
}
