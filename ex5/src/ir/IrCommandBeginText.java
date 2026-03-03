package ir;

import mips.MipsGenerator;

public class IrCommandBeginText extends IrCommand {
    @Override
    public String toString() {
        return "Begin text section";
    }

    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().text();
    }
}
