package ast;

import java.util.List;
import java.util.Arrays;

public class AstStmtAssign extends AstStmt
{
	/***************/
	/*  var := exp */
	/***************/
	public AstVar var;
	public AstExp exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtAssign(AstVar var, AstExp exp)
	{
		super("stmt -> var ASSIGN exp SEMICOLON");
		this.var = var;
		this.exp = exp;
	}

	@Override
	protected String GetNodeName() {
		return "ASSIGN\nleft := right";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(var, exp);
	}
	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
//	public void printMe()
//	{
//		/********************************************/
//		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
//		/********************************************/
//		System.out.print("AST NODE ASSIGN STMT\n");
//
//		/***********************************/
//		/* RECURSIVELY PRINT VAR + EXP ... */
//		/***********************************/
//		if (var != null) var.printMe();
//		if (exp != null) exp.printMe();
//
//		/***************************************/
//		/* PRINT Node to AST GRAPHVIZ DOT file */
//		/***************************************/
//		AstGraphviz.getInstance().logNode(
//				serialNumber,
//			"ASSIGN\nleft := right\n");
//
//		/****************************************/
//		/* PRINT Edges to AST GRAPHVIZ DOT file */
//		/****************************************/
//		AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
//		AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	//}
}
