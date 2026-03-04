package ir;

import mips.MipsGenerator;

public class IrCommandGenerateBuiltinFuncs extends IrCommand {
    @Override
    public void MIPSme() {
        generateErrorSections();
        MipsGenerator.getInstance().builtInFuncs();
    }


    public static void generateErrorSections(){
        MipsGenerator mips = MipsGenerator.getInstance();

        mips.errorSection(IrPatterns.INVALID_DEREF_LABEL, "string_invalid_ptr_dref");
        mips.errorSection(IrPatterns.DIV_BY_ZERO_LABEL, "string_illegal_div_by_0");
        mips.errorSection(IrPatterns.OUT_OF_BOUNDS_LABEL, "string_access_violation");
    }


    @Override
    public String toString() {
        return "generate all built-ins (error sections, string functions, printing)";
    }
}