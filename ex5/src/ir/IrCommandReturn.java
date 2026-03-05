package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

// The return value (null if none)
public class IrCommandReturn extends IrCommand {
    Temp temp;
    boolean isMain = false;


    public IrCommandReturn(boolean isMain){
        this.isMain = isMain;
    }


    public IrCommandReturn(Temp temp) {
        this.temp = temp;
    }


    public IrCommandReturn() {}


    @Override
    public String toString() {
        return "return " + (temp != null ? temp : "nothing");
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we return temp (or null)
        // so add temp to the updated out set if not null
        if (temp != null) inSet.add(temp.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        if (temp != null) temps.add(temp);
    }


    @Override
    public void MIPSme() {
        MipsGenerator mips = MipsGenerator.getInstance();

        if (isMain) {
            mips.jump(MipsGenerator.EOF);
            return;
        }

        if (temp != null) {
            mips.setReturnVal(temp);
        }

        mips.popFrameAndReturn();
    }
}
