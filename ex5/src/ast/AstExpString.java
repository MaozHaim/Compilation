package ast;

import ir.Ir;
import ir.IrCommandGetConstStrAddress;
import temp.Temp;
import types.Type;
import types.TypeString;

public class AstExpString extends AstExp {
    public String value;

    public AstExpString(String value, int lineNum) {
        super(String.format("exp -> STRING( %s )", value), lineNum);
        this.value = value.substring(1, value.length() - 1); // Cut off the " " at the end of the string
        AstProgram.addStringConstant(this.value);
    }


    @Override
    protected String GetNodeName() {
        return String.format("EXP\nSTRING( %s )", value);
    }


    @Override
    public Type SemantMe() {
        return TypeString.getInstance();
    }


    @Override
    public boolean isConstant() {
        return true;
    }


    @Override
    public Temp IRme() {
        Temp temp = new Temp();
        Ir.getInstance().AddIrCommand(new IrCommandGetConstStrAddress(temp, value));
        return temp;
    }
}
