package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandEqStr extends IrCommandEq{
    public IrCommandEqStr(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }


    @Override
    public String toString() {
        return String.format("t%d := (t%d = t%d) (strings)", dst.getSerialNumber(), t1.getSerialNumber(), t2.getSerialNumber());
    }


    public void MIPSme() {
        String compare = getFreshLabel("compare_loop");
        String not_equal = getFreshLabel("not_equal");
        String end = getFreshLabel("end");

        MipsGenerator.getInstance().compareStr(dst, t1, t2, compare, not_equal, end);
    }
}
