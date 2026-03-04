package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

// Stores the value at src to the address at dst
public class IrCommandStore extends IrCommand{
    Temp src;
    Temp dst;
    int offset;


    public IrCommandStore(Temp src, Temp dst, int offset) {
        this.src = src;
        this.dst = dst;
        this.offset = offset;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do dst[offset] := src
        // so, add src and dst to the updated out set
        inSet.add(src.getSerialNumber());
        inSet.add(dst.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(src);
        temps.add(dst);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().sw(src, dst, offset);
    }


    @Override
    public String toString() {
        return super.toString(String.format("t%d[%d] := t%d", dst.getSerialNumber(), offset, src.getSerialNumber()));
    }
}
