package ast;

import java.util.Arrays;
import java.util.List;

public class AstNewexp extends AstExp{
  public AstType type;
  public AstExp exp;

  public AstNewexp(AstType type){
    super("NewExp -> NEW type");
    this.type = type;
  }

  public AstNewexp(AstType type, AstExp exp){
    super("NewExp -> NEW type LBRACE exp RBRACE");
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
}
