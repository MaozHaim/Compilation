package ast;

import types.*;

import java.util.Arrays;
import java.util.List;

public class AstStmtReturn extends AstStmt {
public AstExp exp;

  public AstStmtReturn(AstExp exp, int lineNum){
    super("stmt -> RETURN exp", lineNum); // return exp
    this.exp = exp;
  }

  public AstStmtReturn(int lineNum){
    super("stmt -> RETURN", lineNum);
  }

  @Override
  public String GetNodeName(){
    return "RETURN";
  }

  @Override
  public List<? extends AstNode> GetChildren(){
    if (exp == null) return Arrays.asList();
    return Arrays.asList(exp);
  }

  public Type SemantMe() {
    SymbolTable symbolTable = SymbolTable.getInstance();
    Type expectedReturnType = symbolTable.getExpectedReturnType();

    // void function
    if (expectedReturnType instanceof typeVoid){
      if (exp == null) {
        return null;
      }
      else {
        throwException("Function defined as void can't have a return expression");
      }
    }

    // not void function
    Type returnType = exp.SemantMe();
    if (returnType instanceof TypeClass && expectedReturnType instanceof TypeClass) {
      if (((TypeClass)returnType).isSubtypeOf((TypeClass)expectedReturnType)) { // Check inheritance
        return null;
      }
      throwException("Invalid return inheritance type.");
    }
    // Check if return is object
    if (returnType instanceof TypeNil &&
      (expectedReturnType instanceof TypeClass || expectedReturnType instanceof TypeArray)) {
      return null;
    }
        // Check if matches
    if (!returnType.equals(expectedReturnType)) {
      throwException("Invalid return type.");
    }
    
    return null; // matching primitives
    }
}
