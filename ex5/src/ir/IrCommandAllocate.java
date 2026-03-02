package ir;

import temp.*;

/**
 * Responsible for defining variables: x = t1
 * Used when defining a fresh variable in L-code: int x = y
 * Usage in skeleton: VARDEC
 */
public class IrCommandAllocate extends IrCommand {
    public String var_name;

    public IrCommandAllocate(String var_name) {
        this.var_name = var_name;
    }

    @Override
    public String toString() {
        return "allocate(" + var_name + ")";
    }
}