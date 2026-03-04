package ir;

import symboltable.Metadata;

public class IrCommandParameter extends IrCommand {
    String var_name;
    int offset;


    public IrCommandParameter(String var_name, int offset) {
        this.var_name = var_name;
        this.offset = offset;
    }


    public String toString() {
        return super.toString("parameter " + var_name,
                "name: " + var_name + ", offset: " + offset + ", role: " + Metadata.VAR_ROLE.PARAMETER);
    }


    @Override
    public void MIPSme() {}
}
