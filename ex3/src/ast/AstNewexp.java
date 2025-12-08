package ast;

import types.Type;
import types.TypeInt;
import types.TypeArray;

import java.util.Arrays;
import java.util.List;

public class AstNewexp extends AstExp{
  public AstType type;
  public AstExp exp;

  public AstNewexp(AstType type, int lineNum){
    super("NewExp -> NEW type", lineNum); // new type
    this.type = type;
  }

  public AstNewexp(AstType type, AstExp exp, int lineNum){
    super("NewExp -> NEW type LBRACE exp RBRACE", lineNum); // new type[exp]
    this.type = type;
    this.exp = exp;
  }

  @Override
  public String GetNodeName(){
    String nodename = "NEWEXP\nTYPE";
    if (exp != null) nodename += " EXP";
    return nodename;
  }

  @Override
  protected List<? extends AstNode> GetChildren() {
    if (exp == null) return Arrays.asList(type);
    return Arrays.asList(type, exp);
  }

  public Type SemantMe() {
    Type objectType = type.SemantMe();

    if (exp == null) { // If skipped: "NEW type" is the expression. No need to evaluate exp.
      return objectType;
    }
    // Object is an array
    Type sizeType = exp.SemantMe();

    if (!(sizeType instanceof TypeInt)) {
      throwException("Array size should be an integer.");
    }
    else if (exp instanceof AstExpInt && ((AstExpInt)exp).value <= 0) {
      throwException("Array size should be a positive integer.");
    }
    
    return new TypeArray(objectType.name + "[]", objectType);
  }

  @Override
  public boolean isNewExp() {
    return true;
  }
}
