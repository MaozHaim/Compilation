package ir;

import temp.Temp;

import java.util.Set;

public abstract class IrCommandGetAddress extends IrCommand {
    public Temp dst;
    public String nameToLoad;


    public IrCommandGetAddress(Temp dst, String var_name) {
        this.dst = dst;
        this.nameToLoad = var_name;
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do t := load(var_name), so we need to remove t from the updated out set
        inSet.remove(dst.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
    }
}
