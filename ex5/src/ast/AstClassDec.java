package ast;

import symboltable.Metadata;
import symboltable.SymbolTable;
import temp.Temp;
import types.Type;
import types.TypeClass;
import types.TypeClassMemberDec;
import types.TypeFunction;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstClassDec extends AstDec{
    public String id;
    public String superclass;
    public List<AstCfield> fields;
    public TypeClass typeObject;

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
            members.addAll(father.dataMembers);
        }

        System.out.println("\nClass name: " + this.id);
        TypeClass typeClass = new TypeClass(father, id, members);
        tryTableEnter(id, typeClass);

        symbolTable.currentClass = typeClass;
        symbolTable.beginScope();
        convertNodesToMembers(typeClass, this.fields, members);
        this.typeObject = typeClass;
        this.initMembersList();
        symbolTable.endScope();
        symbolTable.currentClass = null;

        System.out.println("\nClass " + this.id + ":" +
                "\nhas a method list:\n" +
                Arrays.toString(typeObject.methodsInfo.toArray()) +
                "\nAnd an attribute list:\n" +
                Arrays.toString(typeObject.attributes.toArray())
        );

        AstProgram.addClass(this);

        return typeClass;
    }


    @Override
    public Temp IRme() {
        for (AstCfield field : fields) {
            field.IRme();
        }
        return null;
    }


    private void convertNodesToMembers(TypeClass mytype, List<AstCfield> cFields, List<TypeClassMemberDec> members){
        SymbolTable table = SymbolTable.getInstance();
        Type memberType = null;

        attributeCounter = 0;
        for (TypeClassMemberDec member : members) { // Account for inherited attributes
            if (!(member.t instanceof TypeFunction)) { attributeCounter++; }
        }

        for (AstCfield cField : cFields) {
            String memberID = null;
            if (cField instanceof AstCfieldFunc field) {
                memberType = cField.SemantMe();
                memberID = field.funcDec.id;
                Type found = table.findMemberType(mytype, memberID);
                if (found != null && !found.equals(memberType)) cField.throwException("overriding method with differing types");
            }
            else if (cField instanceof AstCfieldVar field) {
                memberType = field.SemantMe();
                memberID = field.varDec.id;
                Type found = table.findMemberType(mytype, memberID);
                if (found != null) cField.throwException("Field already defined in superclass");
            }
            else { throwException("cField in class declaration is not a function or a variable."); }

            Metadata metadata = tryTableFindMetadata(memberID); // Fetch the member metadata you just Semanted.
            TypeClassMemberDec member = new TypeClassMemberDec(memberType, memberID, metadata);
            if (member.metadata.isVariable())
                System.out.println(
                        "Class: " + member.metadata.getClassName() + ", defined an attribute: " + memberID + ", with an offset: " + member.metadata.getOffset()
                );

            members.add(member);
        }
    }


    /**
     * Initializes the method-list and attribute-list for a non-elder class.
     */
    private void initMembersList() {
        if (typeObject.father != null) { deepCopyFather();}

        for (TypeClassMemberDec member : this.typeObject.dataMembers) {
            if (typeObject.father != null && typeObject.father.dataMembers.contains(member)) { continue; }
            if (member.t instanceof TypeFunction) {
                if (typeObject.father == null) { // Eldest class, can't inherit list
                    typeObject.methodsInfo.add(new Pair<>(typeObject, (TypeFunction)member.t));
                }
                else {
                    insertAndHandleOverride(member);
                }
            }
            else { typeObject.attributes.add(member.name); }
        }
    }


    /**
     * Add a method to its list, and handle overriding accordingly.
     * @param member The method to add.
     */
    private void insertAndHandleOverride(TypeClassMemberDec member) {
        Type memberType = member.t;
        String memberName = member.name;
        Pair<TypeClass, TypeFunction> pairToAdd = new Pair<>(typeObject, (TypeFunction) memberType);
        boolean added = false;
        for (int i = 0; i < typeObject.methodsInfo.size(); i++) {
            Pair<TypeClass, TypeFunction> methodInfo = typeObject.methodsInfo.get(i);
            if (memberName.equals(methodInfo.second.name)) { // isOverride
                typeObject.methodsInfo.set(i, pairToAdd);
                added = true;
                System.out.println("Class: " + this.id + ", has replaced: " + memberName + ", with its own method.");
            }
        }
        if (!added) {
            typeObject.methodsInfo.add(pairToAdd);
        }
    }


    /** Copies father as well as the pairs within it. */
    private void deepCopyFather() {
        typeObject.methodsInfo = new ArrayList<>();
        for (Pair<TypeClass, TypeFunction> methodInfo : typeObject.father.methodsInfo) {
            typeObject.methodsInfo.add(new Pair<>(methodInfo.first, methodInfo.second));
        }
        typeObject.attributes = new ArrayList<>(typeObject.father.attributes);
    }
}
