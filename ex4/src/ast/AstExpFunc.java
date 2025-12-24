package ast;

import ir.Ir;
import temp.Temp;
import types.Type;
import types.TypeFunction;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class AstExpFunc extends AstExp {
    public String id;
    public List<AstExp> exps;

    public AstExpFunc(String id, int lineNum) {
        super(String.format("exp -> ID( %s ) LBRACK RBRACK", id), lineNum);
        this.id = id;
        exps = Arrays.asList();
    }

    public AstExpFunc(String id, NodeList<AstExp> expStar, int lineNum){
        super(String.format("exp -> ID( %s ) LBRACK exp* RBRACK", id), lineNum);
        this.id = id;
        exps = expStar.unroll();
    }

    
    @Override
    protected List<? extends AstNode> GetChildren() {
        return exps;
    }

    
    @Override
    protected String GetNodeName() {
        return String.format("%s(%s)", id, exps != null ? "PARAMS" : "");
    }


    private List<Type> expsToList() {
        List<Type> argumentTypes = new ArrayList<>();
        for (AstExp expression : exps) {
            argumentTypes.add(expression.SemantMe());
        }
        return argumentTypes;
    }
    
    
    @Override
    public Type SemantMe() {
        List<Type> argumentTypes = expsToList();
        Type type = tryTableFind(this.id);
        if (!(type instanceof TypeFunction)) { throwException("Attempt to call to something that's not a function."); }
        TypeFunction functionData = (TypeFunction)type;

        List<Type> functionParamTypes = functionData.params;
        boolean argsAreValid = Utils.matchTypesArgsParams(argumentTypes, functionParamTypes);
        if (!argsAreValid) { throwException("Mismatch between arguments and parameters."); }

        return functionData.returnType;
    }


    @Override
    public Temp IRme() {
        Temp t = null;

        if (!exps.isEmpty()) {
            // grab only the first parameter of the function
            t = exps.get(0).IRme();
            for (int i = 1; i < exps.size(); i++){
                exps.get(i).IRme();
            }
        }

        Ir.getInstance().AddIrCommand(new IrCommandPrintInt(t));

        return null;
    }
}
