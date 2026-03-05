package ir;

import mips.MipsGenerator;

public class IrCommandWord extends IrCommand {
    String pointer;


    public IrCommandWord(String initialVal) {
        this.pointer = initialVal;
    }


    public IrCommandWord(){
        pointer = null;
    }


    @Override
    public String toString() {
        return ".word " + (pointer != null ? pointer : null);
    }


    @Override
    public void MIPSme() {
        if (pointer != null) {
            MipsGenerator.getInstance().word(pointer);
        } else {
            MipsGenerator.getInstance().word();
        }
    }
}
