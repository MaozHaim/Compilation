package ir;

import mips.MipsGenerator;

public class IrCommandMethodDec extends IrCommand {
    String className;
    String methodName;


    public IrCommandMethodDec(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }


    @Override
    public String toString() {
        return ".word " + className + "_" + methodName;
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().word(className + "_" + methodName);
    }
}
