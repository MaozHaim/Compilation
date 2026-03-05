package ir;

import mips.MipsGenerator;

public class IrCommandLabel extends IrCommand{
    public String labelName;


    public IrCommandLabel(String label_name) {
        this.labelName = label_name;
    }


    @Override
    public String toString() {
        return labelName + ":";
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().label(labelName);
    }
}
