package ir;

import temp.Temp;

public class IrCommandPrintInt extends IrCommand {
    Temp t;

    public IrCommandPrintInt(Temp t)
    {
        super();
        this.t = t;
    }

    @Override
    public String toString() {
        return "print " + t;
    }
}
