package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandPlusInt extends IrCommandBinop {
    public IrCommandPlusInt(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }


    @Override
    public String toString() {
        return dst + " := " + t1 + " + " + t2 + "\t (Integers)";
    }


    @Override
    public void MIPSme(){
        MipsGenerator.getInstance().add(dst, t1, t2);
    }
}
