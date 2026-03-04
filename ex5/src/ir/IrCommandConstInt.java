package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandConstInt extends IrCommand{
    public Temp t;
    int value;


    public IrCommandConstInt(Temp t,int value) {
        this.t = t;
        this.value = value;
    }


    @Override
    public String toString() {
        return t + " := constval " + value;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do t := value, so we need to remove t from the updated out set
        inSet.remove(t.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(t);
    }


    public void MIPSme() {
        MipsGenerator.getInstance().li(t, value);
    }
}
