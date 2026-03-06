package ast;

import types.Type;
import types.TypeArray;
import types.TypeClass;
import types.TypeNil;
import types.TypeVoid;
import utils.Pair;
import temp.Temp;
import ir.InitialConstVal;
import ir.Ir;
import ir.IrCommandGetLocalAddress;
import ir.IrCommandStore;
import symboltable.Metadata;
import symboltable.SymbolTable;

import java.util.Arrays;
import java.util.List;

public class AstVardec extends AstDec {
  public AstType type;
  public String id;
  public AstExp exp;
  public Metadata metadata;

  public AstVardec(AstType type, String id, int lineNum) {
    super("varDec -> type ID SEMICOLON", lineNum); // int x;
    this.type = type;
    this.id = id;
    this.exp = null;
  }

  // this also accepts newExp (since it inherits from exp) - surely this will have
  // no consequences whatsoever
  public AstVardec(AstType type, String id, AstExp exp, int lineNum) {
    super("varDec -> type ID ASSIGN exp SEMICOLON", lineNum); // int x := exp;
    this.type = type;
    this.id = id;
    this.exp = exp;
  }

  @Override
  public String GetNodeName() {
    return String.format("VARDEC\nTYPE ID( %s )", id);
  }

  @Override
  public List<? extends AstNode> GetChildren() {
    if (exp == null)
      return Arrays.asList(type);
    return Arrays.asList(type, exp);
  }

  @Override
  public Type SemantMe() {
    Type vartype = type.SemantMe();
    if (vartype instanceof TypeVoid) {
      throwException("Cannot declare void type variables.");
    }

    constructVariableMetadata();

    tryTableEnter(id, vartype);

    Type exptype = null;

    if (exp != null) {
      exptype = exp.SemantMe();
    }

    if (metadata.getRole() == Metadata.VAR_ROLE.GLOBAL) {
      InitialConstVal init = new InitialConstVal(exp);
      Pair<String, InitialConstVal> global = new Pair<>(id, init);
      AstProgram.addGlobal(global);
    }

    if (exp == null) {
      return vartype;
    }

    if (exptype.equals(vartype)) {
      return vartype;
    }

    if (vartype.isArray()) {
      arraySemantCheck((TypeArray) vartype, exptype);
      return vartype;
    }

    if (vartype instanceof TypeClass) {
      classSemantCheck((TypeClass) vartype, exptype);
      return vartype;
    }

    if (exptype.equals(vartype)) {
      return vartype;
    }

    throwException("Type mismatch");
    return null; // never reaches this point
  }

  private void arraySemantCheck(TypeArray leftArr, Type rightType) {
    if (!(rightType.isArray())) {
      if (!(rightType instanceof TypeNil)) {
        throwException("expression must be of array type");
      } // nil is valid to array
      else
        return;
    }

    TypeArray rightArr = (TypeArray) rightType;

    if (!leftArr.typeOfElements.equals(rightArr.typeOfElements)) {
      throwException("Assignment of differing array types");
    }
  }

  private void classSemantCheck(TypeClass leftClass, Type rightType) {
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

  private void constructVariableMetadata() {
    this.metadata = new Metadata();
    try { // Parameters aren't a VARDEC!
      metadata.setAsVariable();
      if (SymbolTable.getInstance().inGlobalScope()) {
        metadata.setGlobal();
        System.out.println("Found global variable: " + id);
      } else if (SymbolTable.getInstance().getScopeCounter() == 1 && SymbolTable.getInstance().currentClass != null) {
        metadata.setAttribute();
        metadata.setOffset(++attributeCounter);
        metadata.setClassName(SymbolTable.getInstance().currentClass.name);
        System.out.println("Found attribute: " + id);
      } else {
        vardecCounter++;
        metadata.setLocal();
        metadata.setOffset(vardecCounter);
        System.out.println("Found local variable: " + id + " with offset: " + vardecCounter);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Temp IRme() {
    Ir ir = Ir.getInstance();
    // only interests us if it is local and has an initial assignment, since all
    // others are handled elsewhere
    if (metadata.getRole() == Metadata.VAR_ROLE.LOCAL && exp != null) {
      Temp tempExp = exp.IRme();
      Temp dst = new Temp();
      ir.AddIrCommand(new IrCommandGetLocalAddress(dst, id, metadata.getOffset()));
      ir.AddIrCommand(new IrCommandStore(tempExp, dst, 0));
    }
    return null;
  }

}
