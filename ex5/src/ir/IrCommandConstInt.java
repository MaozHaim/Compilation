package ir;

import temp.Temp;

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
}
