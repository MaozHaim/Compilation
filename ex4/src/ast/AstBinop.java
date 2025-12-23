package ast;

import types.Type;

public class AstBinop extends AstNode {
  public String op;

  public AstBinop(String op, int lineNum){
    super("binop -> " + op, lineNum);

    this.op = op;
  }

  @Override
  public String GetNodeName(){
    return String.format("BINOP( %s )", op);
  }

  @Override
  public Type SemantMe(){ return null; }
}
