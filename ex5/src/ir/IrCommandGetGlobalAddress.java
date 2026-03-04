package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandGetGlobalAddress extends IrCommandGetAddress {
    public IrCommandGetGlobalAddress(Temp dst, String var_name) {
        super(dst, var_name);
    }


    public void MIPSme() {
        MipsGenerator.getInstance().getGlobalAddress(dst, nameToLoad);
    }


    @Override
    public String toString() {
        return String.format("%s := address of global %s", dst, nameToLoad);
    }

}
