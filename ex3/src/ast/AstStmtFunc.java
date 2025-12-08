package ast;

import types.Type;
import types.TypeFunction;

import java.util.Arrays;
import java.util.List;

public class AstStmtFunc extends AstStmt {
    public String id;
    public List<AstExp> args;

    public AstStmtFunc(String id, NodeList<AstExp> args, int lineNum) {
        super("stmt -> ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // ID(exp, exp, ...);
        this.id = id;
        this.args = args.unroll();
    }

    public AstStmtFunc(String id, int lineNum){
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // ID();
        this.id = id;
        this.args = Arrays.asList();
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\n%s(%s)", id, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return args;
    }

    public Type SemantMe() {
        List<Type> argumentTypes = expsToList();
        Type type = tryTableFind(this.id);

        if (!(type instanceof TypeFunction)) { 
            throwException("Attempt to call to something that's not a function."); 
        }

        TypeFunction functionData = (TypeFunction)type;

        List<Type> functionParamTypes = functionData.params;
        boolean argsAreValid = matchTypesArgsParams(argumentTypes, functionParamTypes);

        if (!argsAreValid) { 
            throwException("Mismatch between arguments and parameters."); 
        }

        return null;
    }

    private List<Type> expsToList() {
        List<Type> argumentTypes = new ArrayList<>();
        for (AstExp expression : args) {
            argumentTypes.add(expression.SemantMe());
        }
        return argumentTypes;
    }
}
