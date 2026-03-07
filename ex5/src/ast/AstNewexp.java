package ast;

import ir.IrCommandNewArray;
import ir.IrCommandNewObject;
import types.Type;
import types.TypeArray;
import types.TypeInt;
import types.TypeVoid;
import types.TypeClass;

import java.util.Arrays;
import java.util.List;

import ir.Ir;
import temp.Temp;

public class AstNewexp extends AstExp {
  public AstType type;
  public AstExp exp;
  public TypeClass classInfo;

  public AstNewexp(AstType type, int lineNum) {
    super("NewExp -> NEW type", lineNum); // new type
    this.type = type;
  }

  public AstNewexp(AstType type, AstExp exp, int lineNum) {
    super("NewExp -> NEW type LBRACE exp RBRACE", lineNum); // new type[exp]
    this.type = type;
    this.exp = exp;
  }

  @Override
  public String GetNodeName() {
    String nodename = "NEWEXP\nTYPE";
    if (exp != null)
      nodename += " EXP";
    return nodename;
  }

  @Override
  protected List<? extends AstNode> GetChildren() {
    if (exp == null)
      return Arrays.asList(type);
    return Arrays.asList(type, exp);
  }


  @Override
  public Type SemantMe() {
    Type objectType = type.SemantMe();

    if (exp == null) { // If skipped: "NEW type" is the expression. No need to evaluate exp.
      return objectType;
    }
    // Object is an array
    if (objectType instanceof TypeVoid) {
      throwException("Cannot allocate array over void type.");
    }
    Type sizeType = exp.SemantMe();

    if (!(sizeType instanceof TypeInt)) {
      throwException("Array size should be an integer.");
    } else if (exp instanceof AstExpInt && ((AstExpInt) exp).value <= 0) {
      throwException("Array size should be a positive integer.");
    }

    return new TypeArray(objectType.name + "[]", objectType);
  }


  @Override
  public boolean isNewExp() {
    return true;
  }


  @Override
  public Temp IRme() {
    Temp dst = new Temp();

    if (exp == null) { // Class object
      Ir.getInstance().AddIrCommand(new IrCommandNewObject(dst, classInfo.name, classInfo.getInitialValues()));
      System.out.println("Created a class object of type: " + classInfo.name);
    } else { // Array object
      Temp tempStoresSize = exp.IRme();
      Ir.getInstance().AddIrCommand(new IrCommandNewArray(dst, tempStoresSize, type.getName()));
      System.out.println("Created an array object of type: " + type.getName());
    }

    return dst;
  }
}