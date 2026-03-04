package ir;

public abstract class IrCommandJumpType extends IrCommand {
    public String labelName;
    public boolean ignoreCFG; // If it is a jump to an error we ignore the child (since it contains no temps anyway)


    public IrCommandJumpType(String labelName) {
        this(labelName, false);
    }


    public IrCommandJumpType(String labelName, boolean ignoreCFG) {
        this.labelName = labelName;
        this.ignoreCFG = ignoreCFG;
    }


    @Override
    public String toString() {
        return "jump to " + labelName + (ignoreCFG ? " (ignore in cfg)" : "");
    }
}
