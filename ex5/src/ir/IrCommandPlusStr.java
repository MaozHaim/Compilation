package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandPlusStr extends IrCommandBinop {
    public IrCommandPlusStr(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }


    @Override
    public String toString() {
        return dst + " := " + t1 + " + " + t2 + "\t (Strings)";
    }


    @Override
    public void MIPSme() {
        System.out.println("***************************");
        System.out.println(dst + " := " + t1 + " + " + t2 + "\t (Strings)");
        MipsGenerator.getInstance().appendStrs(dst, t1, t2);
    }
}
