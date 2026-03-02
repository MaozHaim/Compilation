package ir;

public class IrCommandJumpLabel extends IrCommandJumpType{

    public IrCommandJumpLabel(String label_name) {
        super(label_name);
    }

    @Override
    public String toString() {
        return "jmp " + label_name;
    }

}
