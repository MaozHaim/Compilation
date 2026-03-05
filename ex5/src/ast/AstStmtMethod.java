package ast;

import types.Type;
import temp.Temp;

import java.util.Arrays;
import java.util.List;

public class AstStmtMethod extends AstStmt {
    public AstExpMethod method;

    public AstStmtMethod(AstVar var, String methodName, NodeList<AstExp> args, int lineNum) {
        super("stmt -> var DOT ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // x.method(exp, exp...);
        this.method = new AstExpMethod(var, methodName, args, lineNum);
    }

    public AstStmtMethod(AstVar var, String methodName, int lineNum) {
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // x.method();
        this.method = new AstExpMethod(var, methodName, lineNum);
    }

    @Override
    protected String GetNodeName() {
        return "STMT\nMETHOD";
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(method);
    }

    public Type SemantMe() {
        method.SemantMe();

        return null;
    }

    @Override
    public Temp IRme() {
        method.IRme();
        return null;
    }
}
