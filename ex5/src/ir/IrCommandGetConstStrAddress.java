package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandGetConstStrAddress extends IrCommandGetAddress {
    public IrCommandGetConstStrAddress(Temp dst, String value) {
        super(dst, value);
    }


    @Override
    public String toString() {
        return String.format("%s := the address of the constant string \"%s\"", dst, nameToLoad);
    }


    public void MIPSme() {
        MipsGenerator mips = MipsGenerator.getInstance();

        mips.loadStringConst(dst, nameToLoad);
    }
}
