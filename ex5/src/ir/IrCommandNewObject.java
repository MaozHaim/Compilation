package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.List;
import java.util.Set;

public class IrCommandNewObject extends IrCommand{
    Temp dst;
    String className;
    List<InitialConstVal> initialVals;
    int totalFields;


    public IrCommandNewObject(Temp dst, String className, List<InitialConstVal> initialVals, int totalFields ) {
        this.dst = dst;
        this.className = className;
        this.initialVals = initialVals;
        this.totalFields =totalFields;
    }


    public String toString() {
        return dst + " := " + "newClassObject(" + className + ", size=" + totalFields + ")";
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we need to remove dst
        inSet.remove(dst.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().initializeObject(dst, "vtable_" + className, initialVals, totalFields);
    }
}
