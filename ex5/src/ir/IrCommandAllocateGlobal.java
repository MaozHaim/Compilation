package ir;

import mips.MipsGenerator;
import symboltable.Metadata;

public class IrCommandAllocateGlobal extends IrCommand {
    public String varName;
    public String initVal;
    public boolean isStringVal;


    public IrCommandAllocateGlobal(String varName, String initVal, boolean isStringVal) {
        this.varName = varName;
        this.initVal = initVal;
        this.isStringVal = isStringVal;
    }


    @Override
    public String toString() {
        return super.toString("AllocateGlobal(" + varName + "), set to " + initVal,
                "name: " + varName + ", role: " + Metadata.VAR_ROLE.GLOBAL);
    }


    public void MIPSme() {
        MipsGenerator.getInstance().allocateGlobal(varName, initVal, isStringVal);
    }

}
