package ast;

import types.Type;
import types.TypeArray;
import types.TypeVoid;
import symboltable.SymbolTable;

import java.util.Arrays;
import java.util.List;

public class AstDecArraytypedef extends AstDec{
    public String id;
    public AstType type;

    public AstDecArraytypedef(String id, AstType type, int lineNum) {
        super("arrayTypeDef -> ARRAY ID EQ type LBRACK RBRACK SEMICOLON", lineNum);
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


    @Override
    public Type SemantMe() {
        SymbolTable symbolTable = SymbolTable.getInstance();

        if (!symbolTable.inGlobalScope()) { throwException("Array must be in global scope."); }
        Type typeOfElements = type.SemantMe();
        if (typeOfElements instanceof TypeVoid) {
            throwException("Cannot define array over void type.");
        }
        TypeArray thisType = new TypeArray(id, typeOfElements);
        tryTableEnter(id, thisType);

        return thisType; // Not really necessary.
    }
}