package ast;

import java.util.List;
import java.util.Arrays;

public class AstExpVar extends AstExp
{
	public AstVar var;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpVar(AstVar var)
	{
		super("exp -> var");
		this.var = var;
	}

	@Override
	protected String GetNodeName() {
		return "EXP\nVAR";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(var);
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	// public void printMe()
	// {
	// 	/************************************/
	// 	/* AST NODE TYPE = EXP VAR AST NODE */
	// 	/************************************/
	// 	System.out.print("AST NODE EXP VAR\n");

	// 	/*****************************/
	// 	/* RECURSIVELY PRINT var ... */
	// 	/*****************************/
	// 	if (var != null) var.printMe();
		
	// 	/*********************************/
	// 	/* Print to AST GRAPHVIZ DOT file */
	// 	/*********************************/
	// 	AstGraphviz.getInstance().logNode(
	// 			serialNumber,
	// 		"EXP\nVAR");

	// 	/****************************************/
	// 	/* PRINT Edges to AST GRAPHVIZ DOT file */
	// 	/****************************************/
	// 	AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
			
	// }
}
