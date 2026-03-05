package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandGetAttributeAddress extends IrCommandGetAddress {
    int offset;
    String class_name;


    public IrCommandGetAttributeAddress(Temp dst, String var_name, int offset, String class_name) {
        super(dst, var_name);
        this.class_name = class_name;
        this.offset = offset;
    }


    @Override
    public String toString() {
        return String.format("%s := address of attribute %s (from %s offset %d)", dst, nameToLoad, class_name, offset);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().getAttributeAddress(dst, offset);
    }

}
