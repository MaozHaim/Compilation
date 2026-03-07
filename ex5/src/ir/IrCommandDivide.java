package ir;

import mips.MipsGenerator;
import temp.*;

public class IrCommandDivide extends IrCommandBinop {
    public IrCommandDivide(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }


    @Override
    public String toString() {
        return dst + " := " + t1 + " / " + t2;
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().divTo(dst, t1, t2);
    }
}
