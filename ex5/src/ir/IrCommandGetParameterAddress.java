package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandGetParameterAddress extends IrCommand{
    Temp dst;
    String id;
    int offset;


    public IrCommandGetParameterAddress(Temp dst, String id, int offset) {
        this.dst = dst;
        this.id = id;
        this.offset = offset;
    }


    @Override
    public String toString() {
        return String.format("%s := address of parameter %s (offset %d)", dst, id, offset);
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.remove(dst.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().getParameterAddress(dst, offset);
    }
}