package ir;

import mips.MipsGenerator;

public class IrCommandJumpLabel extends IrCommandJump {
    public IrCommandJumpLabel(String label_name) {
        super(label_name);
    }


    public IrCommandJumpLabel(String label_name, boolean ignoreCFG){
        super(label_name, ignoreCFG);
    }


    public void MIPSme() {
        MipsGenerator.getInstance().jump(label_name);
    }


    @Override
    public String toString() {
        return "jmp " + label_name;
    }
}