package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandMove extends IrCommand {
    private Temp dst;
    private Temp src;


    public IrCommandMove(Temp dst, Temp src) {
        this.dst = dst;
        this.src = src;
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().move(dst, src);
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do dst := src
        // so, remove dst and add src
        inSet.add(src.getSerialNumber());
        inSet.remove(dst.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
        temps.add(src);
    }


    @Override
    public String toString() {
        return String.format("t%d := t%d", dst.getSerialNumber(), src.getSerialNumber());
    }
}
