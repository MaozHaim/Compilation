package ast;

import types.*;
import symboltable.*;

import java.util.ArrayList;
import java.util.List;

public class AstClassDec extends AstDec{
    public String id;
    public String superclass;
    public List<AstCfield> fields;

    public AstClassDec(String id, String superclass, NodeList<AstCfield> fields, int lineNum){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE", lineNum);
        this.id = id;
        this.superclass = superclass;
        this.fields = fields.unroll();
    }

    public AstClassDec(String id, NodeList<AstCfield> fields, int lineNum){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE", lineNum);
        this.id = id;
        this.superclass = null;
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


    @Override
    public Type SemantMe() {
        SymbolTable symbolTable = SymbolTable.getInstance();
        if (!symbolTable.inGlobalScope()) throwException("Class not defined in global scope");

        List<TypeClassMemberDec> members = new ArrayList<>();
        TypeClass father = null;
        if (superclass != null) {
            Type found = tryTableFind(this.superclass);
            if (!(found instanceof TypeClass)) { throwException(found.name + " is not a class"); }
            father = (TypeClass)found;
        }

        TypeClass typeClass = new TypeClass(father, id, members);
        tryTableEnter(id, typeClass);
        symbolTable.currentClass = typeClass;

        symbolTable.beginScope();
        convertNodesToMembers(typeClass, this.fields, members);
        symbolTable.endScope();

        symbolTable.currentClass = null;
        return typeClass;
    }


    private void convertNodesToMembers(TypeClass mytype, List<AstCfield> cFields, List<TypeClassMemberDec> members) {
        SymbolTable table = SymbolTable.getInstance();
        for (AstCfield cField : cFields) {
            Type memberType = cField.SemantMe();
            String memberID = null;
            if (cField instanceof AstCfieldFunc) {
                memberID = ((AstCfieldFunc)cField).funcDec.id;
                Type found = table.findMemberType(mytype, memberID);
                if (found != null && !found.equals(memberType)) cField.throwException("overriding method with differing types");
            }
            else if (cField instanceof AstCfieldVar) {
                memberID = ((AstCfieldVar)cField).varDec.id;
                Type found = table.findMemberType(mytype, memberID);
                if (found != null) cField.throwException("Field already defined in superclass");
            }
            else { throwException("cField in class declaration is not a function or a variable."); }
            TypeClassMemberDec member = new TypeClassMemberDec(memberType, memberID);
            members.add(member);
        }
    }
}
