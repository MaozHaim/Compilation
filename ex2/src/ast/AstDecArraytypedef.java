package ast;

import java.util.Arrays;
import java.util.List;

public class AstDecArraytypedef extends AstDec{
    public String id;
    public AstType type;

    public AstDecArraytypedef(String id, AstType type) {
        super("arrayTypeDef -> ARRAY ID EQ type LBRACK RBRACK SEMICOLON");
        this.id = id;
        this.type = type;
    }

    @Override
    protected String GetNodeName() {
        return String.format("ARRAY_TYPEDEF( %s )\nTYPE", id);
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(type);
    }
}
