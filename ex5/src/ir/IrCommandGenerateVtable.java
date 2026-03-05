package ir;

import mips.MipsGenerator;

import java.util.List;

public class IrCommandGenerateVtable extends IrCommand {
    public String classname;
    public List<String> methods;


    public IrCommandGenerateVtable(String classname, List<String> methods) {
        this.classname = classname;
        this.methods = methods;
    }


    @Override
    public void MIPSme() {
        MipsGenerator.getInstance().label("vtable_" + classname);
        for (String method : methods){
            MipsGenerator.getInstance().word(method);
        }
    }
}
