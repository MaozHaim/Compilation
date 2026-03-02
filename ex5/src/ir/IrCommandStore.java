package ir;

import temp.Temp;

public class IrCommandStore extends IrCommand{
    public String varName;
    public Temp src;

    public IrCommandStore(String var_name, Temp src) {
        this.src = src;
        this.varName = var_name;
    }

    @Override
    public String toString() {
        return varName +  " := " + src;
    }
}
