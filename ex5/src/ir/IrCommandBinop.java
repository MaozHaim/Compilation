package ir;

import temp.Temp;

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
}
