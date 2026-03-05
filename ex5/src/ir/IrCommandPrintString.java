package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandPrintString extends IrCommand {
    Temp addr;


    public IrCommandPrintString(Temp addr){
        this.addr = addr;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.add(addr.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(addr);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().printStr(addr);
    }


    @Override
    public String toString() {
        return String.format("PrintString(%s)", addr);
    }
}
