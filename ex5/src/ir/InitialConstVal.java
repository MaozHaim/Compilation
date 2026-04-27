package ir;

import ast.AstExp;
import ast.AstExpInt;
import ast.AstExpString;

// holds information about the initial value that a variable gets on declaration.
// for class fields the initializer must be a constant literal; for globals it
// may be an arbitrary expression that is evaluated at runtime before main.
public class InitialConstVal {
    private String value = "0";
    private boolean isString = false;
    private AstExp runtimeInitExp = null;


    public InitialConstVal(AstExp exp){
        if (exp == null) return; // empty initialization

        if (!exp.isConstant()) {
            runtimeInitExp = exp;
            return;
        }

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


    public String getValue() {
        return value;
    }


    public boolean isString() {
        return isString;
    }


    public boolean needsRuntimeInit() {
        return runtimeInitExp != null;
    }


    public AstExp getRuntimeInitExp() {
        return runtimeInitExp;
    }
}