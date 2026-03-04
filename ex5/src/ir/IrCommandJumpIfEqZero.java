package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandJumpIfEqZero extends IrCommandJump {
    Temp t;


    public IrCommandJumpIfEqZero(Temp t, String label_name) {
        super(label_name);
        this.t = t;
    }


    @Override
    public String toString() {
        return "jmp " + label_name + " if " + t + " is zero";
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we only need to add t to the updated out set
        inSet.add(t.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(t);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().beqz(t,label_name);
    }
}
