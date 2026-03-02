package ir;

import ast.AstExp;
import ast.AstExpInt;
import ast.AstExpString;

// holds information about the initial constant value that a variable gets on declaration
public class InitialConstVal {
    private String value = "0";
    private boolean isString = false;

    public String getValue() {
        return value;
    }

    public boolean isString() {
        return isString;
    }

    public InitialConstVal(AstExp exp){
        if (exp == null) return; // empty initialization

        if (!exp.isConstant()) throw new RuntimeException("Tried to initialize variable with non-const value");

        if (exp instanceof AstExpInt){
            AstExpInt eint = (AstExpInt)exp;
            value = String.valueOf(eint.value);
        }
        else if (exp instanceof AstExpString) {
            AstExpString estr = (AstExpString)exp;
            value = estr.value;
            isString = true;
        }
        // else must be nil, default values
    }
}
