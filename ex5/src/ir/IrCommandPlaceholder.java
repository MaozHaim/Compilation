package ir;

public class IrCommandPlaceholder extends IrCommand {
    String message;


    public IrCommandPlaceholder() {
        this.message = "No message :(";
    }


    public IrCommandPlaceholder(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "PLACEHOLDER:" + this.message;
    }


    @Override
    public void MIPSme() {}
}
