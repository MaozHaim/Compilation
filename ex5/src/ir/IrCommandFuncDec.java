package ir;

import mips.MipsGenerator;

public class IrCommandFuncDec extends IrCommand {
    String funcName;
    int localCount; // Amount of local variables inside the func


    public IrCommandFuncDec(String funcName, int localCount) {
        this.funcName = funcName;
        this.localCount = localCount;
    }


    @Override
    public String toString() {
        return "\nFuncDec(" + funcName + ") with " + localCount + " local vars.";
    }


    public void MIPSme() {
        MipsGenerator mips = MipsGenerator.getInstance();

        mips.label(funcName);
        if (!funcName.equals("main")) {
            mips.pushFrame();
        } else {
            mips.wildcard("move $fp, $sp");
        }

        if (localCount > 0) {
            mips.stackAlloc(localCount);
        }
    }
}
