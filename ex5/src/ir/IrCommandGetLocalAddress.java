package ir;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandGetLocalAddress extends IrCommandGetAddress {
    int offset;


    public IrCommandGetLocalAddress(Temp dst, String id, int offset) {
        super(dst, id);
        this.offset = offset;
    }


    @Override
    public String toString() {
        return String.format("%s := address of local %s (offset %d)", dst, nameToLoad, offset);
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().getLocalAddress(dst, offset);
    }

}
