package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandNewArray extends IrCommand {
    Temp dst;
    Temp size;
    String typeName;


    public IrCommandNewArray(Temp dst, Temp size, String typeName) {
        this.dst = dst;
        this.size = size;
        this.typeName = typeName;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we need to remove dst and add size
        inSet.add(size.getSerialNumber());
        inSet.remove(dst.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
        temps.add(size);
    }


    public String toString() {
        return dst + " := " + "newArray[" + size + "]";
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().allocateArray(dst, size);
    }
}
