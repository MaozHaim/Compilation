package ir;

import temp.Temp;

public class IrCommandJumpIfEqZero extends IrCommandJumpType {

    Temp t;

    public IrCommandJumpIfEqZero(Temp t, String label_name) {
        super(label_name);
        this.t = t;
    }

    @Override
    public String toString() {
        return "jmp " + label_name + " if " + t + " is zero";
    }
}
