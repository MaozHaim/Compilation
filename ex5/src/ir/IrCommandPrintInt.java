package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandPrintInt extends IrCommand {
    Temp t;


    public IrCommandPrintInt(Temp t)
    {
        super();
        this.t = t;
    }


    @Override
    public String toString() {
        return "print " + t;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do print t, so add t
        inSet.add(t.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(t);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().printInt(t);
    }
}
