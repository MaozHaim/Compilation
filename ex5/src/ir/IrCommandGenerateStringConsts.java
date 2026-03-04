package ir;

import mips.MipsGenerator;

import java.util.Set;

public class IrCommandGenerateStringConsts extends IrCommand {
    Set<String> strs;


    public IrCommandGenerateStringConsts(Set<String> strs) {
        this.strs = strs;
    }


    @Override
    public String toString() {
        String returnStr = "Generating the following String definitions:\n";
        for (String str : strs){
            returnStr += "str_" + str + ", ";
        }
        return returnStr;
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().builtInStrs();
        for (String str : strs){
            MipsGenerator.getInstance().asciiz(str);
        }
    }
}
