package ir;

import temp.*;

public abstract class IrCommandEq extends IrCommandBinop {
    public IrCommandEq(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }
}
