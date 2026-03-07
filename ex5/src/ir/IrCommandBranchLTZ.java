package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandBranchLTZ extends IrCommandJump {
    Temp t1;


    public IrCommandBranchLTZ(Temp t1, String label_name) {
        super(label_name);
        this.t1 = t1;
    }


    public IrCommandBranchLTZ(Temp t1, String label_name,  boolean ignoreCFG) {
        super(label_name, ignoreCFG);
        this.t1 = t1;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.add(t1.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(t1);
    }


    @Override
    public String toString() {
        return super.toString() + " if "  + t1 + " < 0";
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().bltz(t1, labelName);
    }
}
