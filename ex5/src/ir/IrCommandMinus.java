package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandMinus extends IrCommandBinop {
    public Temp t1;
    public Temp t2;
    public Temp dst;


    public IrCommandMinus(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }


    @Override
    public String toString() {
        return dst + " := " + t1 + " - " + t2;
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().sub(dst, t1, t2);
    }
}
