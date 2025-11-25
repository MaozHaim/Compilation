package ast;

import java.util.List;
import java.util.Arrays;

public class AstVarSubscript extends AstVar
{
	public AstVar var;
	public AstExp subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSubscript(AstVar var, AstExp subscript)
	{
		super("var -> var [ exp ]");
		this.var = var;
		this.subscript = subscript;
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(var, subscript);
	}

	@Override
	protected String GetNodeName() {
		return "SUBSCRIPT\nVAR\n...[...]";
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
//	public void printMe()
//	{
//		/*************************************/
//		/* AST NODE TYPE = AST SUBSCRIPT VAR */
//		/*************************************/
//		System.out.print("AST NODE SUBSCRIPT VAR\n");
//
//		/****************************************/
//		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
//		/****************************************/
//		if (var != null) var.printMe();
//		if (subscript != null) subscript.printMe();
//
//		/***************************************/
//		/* PRINT Node to AST GRAPHVIZ DOT file */
//		/***************************************/
//		AstGraphviz.getInstance().logNode(
//				serialNumber,
//			"SUBSCRIPT\nVAR\n...[...]");
//
//		/****************************************/
//		/* PRINT Edges to AST GRAPHVIZ DOT file */
//		/****************************************/
//		if (var       != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
//		if (subscript != null) AstGraphviz.getInstance().logEdge(serialNumber,subscript.serialNumber);
//	}
}
