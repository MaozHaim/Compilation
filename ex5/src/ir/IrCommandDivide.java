package ir;

import temp.*;

// TODO do we need this class?
public class IrCommandDivide extends IrCommandBinop {
    public Temp t1;
    public Temp t2;
    public Temp dst;

    public IrCommandDivide(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }
}
