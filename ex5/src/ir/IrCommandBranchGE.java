package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandBranchGE extends IrCommandJump {
    private Temp t1;
    private Temp t2;
    private String label;


    public IrCommandBranchGE(Temp t1, Temp t2, String label, boolean ignoreCFG) {
        super(label, ignoreCFG);
        this.t1 = t1;
        this.t2 = t2;
        this.label = label;
    }


    public IrCommandBranchGE(Temp t1, Temp t2, String label) {
        this(t1, t2, label, false);
    }


    @Override
    public String toString() {
        return super.toString() + " if " + t1 + " >= " + t2;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // In branchGE, we have: if (t1 >= t2) goto label
        // so, we only need to add t1 and t2 to the updated out set
        inSet.add(t1.getSerialNumber());
        inSet.add(t2.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(t1);
        temps.add(t2);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().bge(t1, t2, label);
    }
}
