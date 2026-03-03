package ir;

import temp.Temp;

import java.util.Set;

public abstract class IrCommandBinop extends IrCommand {
    public Temp t1;
    public Temp t2;
    public Temp dst;


    public IrCommandBinop(Temp dst,Temp t1,Temp t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }


    @Override
    public String toString() {
        return dst + " := " + t1 + " BINOP " + t2;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // In binop, we have: dst := t1 op t2
        // so, we need to remove dst, and add t1 and t2 to the updated out set
        inSet.remove(dst.getSerialNumber());
        inSet.add(t1.getSerialNumber());
        inSet.add(t2.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
        temps.add(t1);
        temps.add(t2);
    }
}
