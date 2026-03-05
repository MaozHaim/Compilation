package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandSLL extends IrCommand {
    Temp dst;
    Temp src;
    int shift;


    public IrCommandSLL(Temp dst, Temp src, int shift) {
        this.dst = dst;
        this.src = src;
        this.shift = shift;
    }


    @Override
    public String toString() {
        return "dst := src << 2";
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
        temps.add(src);
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.remove(dst.getSerialNumber());
        inSet.add(src.getSerialNumber());
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().sll(dst, src, shift);
    }
}
