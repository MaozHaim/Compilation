package ast;

public class AstExpInt extends AstExp
{
	public int value;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpInt(int value)
	{
		super("exp -> INT( %d )");
		this.value = value;
	}

	@Override
	protected String GetNodeName() {
		return String.format("INT(%d)",value);
	}

	// /************************************************/
	// /* The printing message for an int exp AST node */
	// /************************************************/
	// public void printMe()
	// {
	// 	/*******************************/
	// 	/* AST NODE TYPE = AST INT EXP */
	// 	/*******************************/
	// 	System.out.format("AST NODE INT( %d )\n",value);

	// 	/*********************************/
	// 	/* Print to AST GRAPHVIZ DOT file */
	// 	/*********************************/
	// 	AstGraphviz.getInstance().logNode(
	// 			serialNumber,
	// 		String.format("INT(%d)",value));
	// }
}
