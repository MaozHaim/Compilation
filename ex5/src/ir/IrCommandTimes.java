package ir;

import mips.MipsGenerator;
import temp.*;

public class IrCommandTimes extends IrCommandBinop {
    public Temp t1;
    public Temp t2;
    public Temp dst;


    public IrCommandTimes(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }


    @Override
    public String toString() {
        return dst + " := " + t1 + " * " + t2;
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().mul(dst,t1,t2);
    }
}
