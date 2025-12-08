package ast;

import types.Type;

import java.util.Arrays;
import java.util.List;

public class AstFuncParam extends AstNode {
    public AstType type;
    public String id;


    public AstFuncParam(AstType type, String id, int lineNum) {
        super("funcDec parameter (type ID)", lineNum);
        this.type = type;
        this.id = id;
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(type);
    }

    @Override
    protected String GetNodeName() {
        return String.format("PARAMETER( %s )", id);
    }

    @Override
    public Type SemantMe() {
        Type paramType = type.SemantMe();
        tryTableEnter(id, paramType);
        return paramType;
    }
}
