package ir;

import mips.MipsGenerator;
import temp.Temp;

import java.util.Set;

public class IrCommandLoad extends IrCommand{
    Temp dst;
    Temp src;
    Temp offset; // if we need to add some variable
    int immediateOffset; // if we need to add some constant


    public IrCommandLoad(Temp dst, Temp src, Temp offset, int immediateOffset) {
        this.dst = dst;
        this.src = src;
        this.offset = offset;
        this.immediateOffset = immediateOffset;
    }


    public IrCommandLoad(Temp dst, Temp src, Temp offset){
        this(dst, src, offset, 0);
    }


    public IrCommandLoad(Temp dst, Temp src, int immediateOffset){
        this(dst, src, null, immediateOffset);
    }


    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do dst := src[offset]
        // so we need to remove dst, and add src and offset to the updated out set
        inSet.remove(dst.getSerialNumber());
        inSet.add(src.getSerialNumber());
        if (offset != null) inSet.add(offset.getSerialNumber());
    }


    @Override
    public void addTemps(Set<Temp> temps) {
        temps.add(dst);
        temps.add(src);
        if (offset != null) temps.add(offset);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().loadAt(dst, src, offset, immediateOffset);
    }


    public String toString() {
        if (offset != null) {
            return super.toString(dst + " := " + src + "[" + offset + " + " + immediateOffset + "]","");
        }
        return super.toString(dst + " := " + src + "["+ immediateOffset + "]");
    }

}
