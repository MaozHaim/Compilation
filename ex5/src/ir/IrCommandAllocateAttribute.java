package ir;

public class IrCommandAllocateAttribute extends IrCommand {
    String varName;
    String className;
    int offset;


    public IrCommandAllocateAttribute(String var_name, String className, int offset) {
        this.varName = var_name;
        this.className = className;
        this.offset = offset;
    }


    @Override
    public String toString() {
        return super.toString("Attribute Declaration",
                "name: " + varName + ", Class: " + className + ", offset: " + offset);
    }


    @Override
    public void MIPSme() {}
}
