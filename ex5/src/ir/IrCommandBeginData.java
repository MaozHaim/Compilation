package ir;

import mips.MipsGenerator;

public class IrCommandBeginData extends IrCommand {
    @Override
    public String toString() {
        return "Begin data section";
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().data();
    }
}
