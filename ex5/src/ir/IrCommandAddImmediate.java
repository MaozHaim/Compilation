package ir;

import temp.Temp;

import java.util.Set;

public class IrCommandAddImmediate extends IrCommand {
    Temp dst;
    Temp op1;
    int immediate;

    public IrCommandAddImmediate(Temp dst, Temp op1, int immediate){
        this.dst = dst;
        this.op1 = op1;
        this.immediate = immediate;
    }

    public IrCommandAddImmediate(Temp dst, int immediate){
        this.dst = dst;
        this.op1 = null;
        this.immediate = immediate;
    }

    @Override
    public String toString() {
        Temp t = dst;
        if (op1 != null) {
            t = op1;
        }
        return String.format("%s := %s + %d", dst, t, immediate);
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.remove(dst.getSerialNumber());
        if (op1 != null) {
            inSet.add(op1.getSerialNumber());
        }
    }

    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
        if (op1 != null) {
            temps.add(op1);
        }
    }

    @Override
    public void MIPSme() {
        if (op1 != null) {
            MIPSGenerator.getInstance().addi(dst, op1, immediate);
        }
        else {
            MIPSGenerator.getInstance().addi(dst, dst, immediate);
        }
    }
}
