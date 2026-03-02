package ast;

public enum Primitive {
  INT("int"),
  STRING("string"),
  VOID("void");

  public String label;

  private Primitive(String label){
    this.label = label;
  }
}
