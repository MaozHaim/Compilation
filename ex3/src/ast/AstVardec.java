package ast;

import types.Type;
import types.TypeArray;
import types.TypeClass;
import types.TypeNil;
import types.TypeVoid;

import java.util.Arrays;
import java.util.List;

public class AstVardec extends AstDec{
  public AstType type;
  public String id;
  public AstExp exp;

  public AstVardec(AstType type, String id, int lineNum){
    super("varDec -> type ID SEMICOLON", lineNum); // int x;
    this.type = type;
    this.id = id;
    this.exp = null;
  }

  // this also accepts newExp (since it inherits from exp) - surely this will have no consequences whatsoever
  public AstVardec(AstType type, String id, AstExp exp, int lineNum){
    super("varDec -> type ID ASSIGN exp SEMICOLON", lineNum); // int x := exp;
    this.type = type;
    this.id = id;
    this.exp = exp;
  }

  @Override
  public String GetNodeName(){
    return String.format("VARDEC\nTYPE ID( %s )", id);
  }

  @Override
  public List<? extends AstNode> GetChildren(){
    if (exp == null) return Arrays.asList(type);
    return Arrays.asList(type, exp);
  }

  @Override
  public Type SemantMe() {
    Type vartype = type.SemantMe();
    if (vartype instanceof TypeVoid) {
      throwException("Cannot declare void type variables.");
    } 

    tryTableEnter(id, vartype);

    if (exp == null) {
      return vartype;
    }

    Type exptype = exp.SemantMe();

    if (vartype.isArray()){
      arraySemantCheck((TypeArray)vartype, exptype);
      return vartype;
    }

    if (vartype instanceof TypeClass){
      classSemantCheck((TypeClass)vartype, exptype);
      return vartype;
    }

    if (exptype.equals(vartype)) {
      return vartype;
    } 

    throwException("Type mismatch");
    return null; // never reaches this point
  }

  private void arraySemantCheck(TypeArray leftArr, Type rightType){
    if (!(rightType.isArray())) {
      if (!(rightType instanceof TypeNil)) {
        throwException("expression must be of array type");
      } // nil is valid to array
      else return;
    }

    TypeArray rightArr = (TypeArray)rightType;

    if (!leftArr.typeOfElements.equals(rightArr.typeOfElements)) {
      throwException("Assignment of differing array types");
    }
  }

  private void classSemantCheck(TypeClass leftClass, Type rightType){
    if (rightType instanceof TypeNil) {
        return;
    }

    if (!(rightType instanceof TypeClass)) {
        throwException("expression must be of class type");
    }

    TypeClass rightClass = (TypeClass) rightType;

    if (!rightClass.isSubtypeOf(leftClass)) {
        throwException("Expression does not inherit from variable's class");
    }
}
}
