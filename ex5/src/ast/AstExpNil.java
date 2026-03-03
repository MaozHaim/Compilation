package ast;

import ir.Ir;
import ir.IrCommandConstInt;
import temp.Temp;
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


    @Override
    public Temp IRme() {
        Temp dst = new Temp();
        Ir.getInstance().AddIrCommand(new IrCommandConstInt(dst, 0));
        return dst;
    }
}
