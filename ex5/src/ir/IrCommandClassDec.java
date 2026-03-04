package ir;

import mips.MipsGenerator;

public class IrCommandClassDec extends IrCommand {
    String className;


    public IrCommandClassDec(String className) {
        this.className = className;
    }


    @Override
    public String toString() {
        return "---------------------------\n" +
                "ClassDec(" + className + ")";
    }


    public void MIPSme() {
        MipsGenerator.getInstance().label("vtable_" + className);
    }
}
