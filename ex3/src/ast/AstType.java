package ast;

import types.Type;

public class AstType extends AstNode {

  public String type;

  public AstType(String type, int lineNum){
    super("type -> " + type, lineNum);
    this.type = type;
  }

  @Override public String GetNodeName(){
    return "TYPE " + type;
  }

  @Override public Type SemantMe() {
    return tryTableFind(type);
  }

}
