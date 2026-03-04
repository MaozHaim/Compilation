package ir;


import temp.Temp;

// Utility class to generate common patterns
public class IRPatterns {
    private IRPatterns() {} // static class
    public static int MAX_INT = 32767;
    public static int MIN_INT = -32768;
    public static final String DIV_BY_ZERO_LABEL = "div_by_zero_err";
    public static final String INVALID_DEREF_LABEL = "invalid_deref_err";
    public static final String OUT_OF_BOUNDS_LABEL = "out_of_bounds_err";


    public static void clampInteger(Temp dst){
        Temp minval = new Temp();
        Temp maxval = new Temp();
        Temp comparisonResult = new Temp();
        String clmpLower = IrCommand.getFreshLabel("clamp_int_lower");
        String clmpHigher = IrCommand.getFreshLabel("clamp_int_higher");
        String clmpEnd = IrCommand.getFreshLabel("clamp_int_end");

        Ir ir = Ir.getInstance();

        // load max/min values for an integer
        ir.AddIrCommand(new IrCommandConstInt(minval, MIN_INT));
        ir.AddIrCommand(new IrCommandConstInt(maxval, MAX_INT));

        // jump to clamps if necessary
        ir.AddIrCommand(new IrCommandLt(comparisonResult, dst, maxval));
        ir.AddIrCommand(new IrCommandJumpIfEqZero(comparisonResult, clmpHigher));
        ir.AddIrCommand(new IrCommandGt(comparisonResult, dst, minval));
        ir.AddIrCommand(new IrCommandJumpIfEqZero(comparisonResult, clmpLower));
        ir.AddIrCommand(new IrCommandJumpLabel(clmpEnd));

        // clamp to max
        ir.AddIrCommand(new IrCommandLabel(clmpHigher));
        ir.AddIrCommand(new IrCommandMove(dst, maxval));
        ir.AddIrCommand(new IrCommandJumpLabel(clmpEnd));

        // clamp to min
        ir.AddIrCommand(new IrCommandLabel(clmpLower));
        ir.AddIrCommand(new IrCommandMove(dst, minval));

        // end
        ir.AddIrCommand(new IrCommandLabel(clmpEnd));
    }


    public static void divide(Temp dst, Temp t1, Temp t2){
        Ir ir = Ir.getInstance();

        ir.AddIrCommand(new IrCommandJumpIfEqZero(t2, DIV_BY_ZERO_LABEL, true));
        ir.AddIrCommand(new IrCommandDivide(dst, t1, t2));
    }


    public static void checkNullRef(Temp pointer){
        Ir ir = Ir.getInstance();

        ir.AddIrCommand(new IrCommandJumpIfEqZero(pointer, INVALID_DEREF_LABEL, true));
    }


    public static void checkArrBounds(Temp idx, Temp arrSize){
        Ir ir = Ir.getInstance();

        ir.AddIrCommand(new IrCommandBranchLTZ(idx, OUT_OF_BOUNDS_LABEL, true));
        ir.AddIrCommand(new IrCommandBranchGE(idx, arrSize, OUT_OF_BOUNDS_LABEL, true));
    }
}
