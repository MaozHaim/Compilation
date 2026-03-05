package ir;

public abstract class IrCommandJump extends IrCommand {
    public String labelName;
    public boolean ignoreCFG; // If it is a jump to an error we ignore the child (since it contains no temps anyway)


    public IrCommandJump(String labelName) {
        this(labelName, false);
    }


    public IrCommandJump(String labelName, boolean ignoreCFG) {
        this.labelName = labelName;
        this.ignoreCFG = ignoreCFG;
    }


    @Override
    public String toString() {
        return "jump to " + labelName + (ignoreCFG ? " (ignore in cfg)" : "");
    }
}
