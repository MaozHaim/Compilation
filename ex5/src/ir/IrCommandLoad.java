package ir;

import temp.Temp;

public class IrCommandLoad extends IrCommand{

    public Temp dst;
    public String varName;

    public IrCommandLoad(Temp dst,String var_name) {
        this.dst      = dst;
        this.varName = var_name;
    }

    @Override
    public String toString() {
        return dst + " := " + varName;
    }

}
