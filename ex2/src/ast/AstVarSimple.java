package ast;

public class AstVarSimple extends AstVar
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSimple(String name)
	{
		super("var -> ID( %s )");
		this.name = name;
	}

	@Override
	protected String GetNodeName() {
		return String.format("SIMPLE\nVAR(%s)",name);
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
//	public void printMe()
//	{
//		/**********************************/
//		/* AST NODE TYPE = AST SIMPLE VAR */
//		/**********************************/
//		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);
//
//		/*********************************/
//		/* Print to AST GRAPHVIZ DOT file */
//		/*********************************/
//		AstGraphviz.getInstance().logNode(
//				serialNumber,
//			String.format("SIMPLE\nVAR\n(%s)",name));
//	}
}
