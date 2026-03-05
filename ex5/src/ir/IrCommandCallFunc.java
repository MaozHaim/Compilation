package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.List;
import java.util.Set;

public class IrCommandCallFunc extends IrCommand{
    String funcName;
    List<Temp> params;
    Temp dst;


    public IrCommandCallFunc(String funcName, List<Temp> params, Temp dst) {
        this.funcName = funcName;
        this.params = params;
        this.dst = dst;
    }


    @Override
    public String toString() {
        String str = String.format("t%d := call %s(", dst.getSerialNumber(), funcName);
        for (Temp t: params) str += " t" + t.getSerialNumber() + " ";
        str += ")";
        return str;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we need to remove dst and add all temps in params
        for (Temp t: params) inSet.add(t.getSerialNumber());
        inSet.remove(dst.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
        temps.addAll(params);
    }


    @Override
    public void MIPSme() {
        // backup temps -> push parameters -> jump -> pop parameters -> restore temps -> get return value from v0 to dst
        MipsGenerator mips = MipsGenerator.getInstance();
        mips.backupTemps();
        for (int i = params.size()-1; i >= 0; i--){
            mips.stackPush(params.get(i));
        }
        mips.jal(funcName);
        if (params.size() > 0) mips.stackDealloc(params.size());
        mips.restoreTemps();
        mips.getReturnVal(dst);
    }
}
