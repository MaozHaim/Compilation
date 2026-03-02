package ast;

import symboltable.SymbolTable;
import types.Type;
import types.TypeClass;
import types.TypeFunction;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstExpMethod extends AstExp{
    public AstVar var;
    public String methodName;
    public List<AstExp> exps;

    public AstExpMethod(AstVar var, String methodName, int lineNum){
        super("exp -> var DOT ID LPAREN RPAREN", lineNum);
        this.methodName = methodName;
        this.var = var;
        exps = Arrays.asList();
    }

    public AstExpMethod(AstVar var, String methodName, NodeList<AstExp> expStar, int lineNum){
        super("exp -> var DOT ID LPAREN exp* RPAREN", lineNum);
        this.methodName = methodName;
        this.var = var;
        exps = expStar.unroll();
    }

    
    @Override
    protected List<? extends AstNode> GetChildren() {
        List<AstNode> children = new ArrayList<>(exps);
        children.add(0, var);
        return children;
    }

    
    @Override
    protected String GetNodeName() {
        return String.format("VAR.%s(%s)", methodName, exps != null ? "PARAMS" : "");
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
        SymbolTable table = SymbolTable.getInstance();
        Type data = var.SemantMe();
        if (!(data instanceof TypeClass)) {
            throwException("Attempting to call method on a non-class type.");
        }
        TypeClass classData = (TypeClass) data;
        Type methodType = table.findMemberType(classData, methodName);

        if (!(methodType instanceof TypeFunction)) {
            throwException("Attempting to call attribute as a method.");
        }

        TypeFunction functionData = (TypeFunction)methodType;
        Type returnType = functionData.returnType;
        List<Type> argsList = expsToList();
        if (!(Utils.matchTypesArgsParams(argsList, functionData.params))){
            throwException("Bad parameters or arguments.");
        }

        return returnType;
    }
}
