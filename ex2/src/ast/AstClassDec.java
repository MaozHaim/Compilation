package ast;

import java.util.List;

public class AstClassDec extends AstDec{
    public String id;
    public String superclass;
    public List<AstCfield> fields;

    public AstClassDec(String id, String superclass, NodeList<AstCfield> fields){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE");
        this.id = id;
        this.superclass = superclass;
        this.fields = fields.unroll();
    }

    public AstClassDec(String id, NodeList<AstCfield> fields){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE");
        this.id = id;
        this.fields = fields.unroll();
    }

    @Override
    protected String GetNodeName() {
        return String.format("CLASS %s%s\nCFIELDS", id, (superclass != null ? " EXTENDS " + superclass : ""));
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return fields;
    }
}
