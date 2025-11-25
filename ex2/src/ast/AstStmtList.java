package ast;

import java.util.List;
import java.util.Arrays;

public class AstStmtList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstStmt head;
	public AstStmtList tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstStmtList(AstStmt head, AstStmtList tail)
	{
		super("stmtlist -> " + (tail != null ? "stmt stmtlist" : "stmt"));
		this.head = head;
		this.tail = tail;
	}

	@Override
	protected String GetNodeName() {
		return "STMT\nLIST";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		if (tail == null){
			return Arrays.asList(head);
		}
		return Arrays.asList(head, tail);
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
//	public void printMe()
//	{
//		/**************************************/
//		/* AST NODE TYPE = AST STATEMENT LIST */
//		/**************************************/
//		System.out.print("AST NODE STMT LIST\n");
//
//		/*************************************/
//		/* RECURSIVELY PRINT HEAD + TAIL ... */
//		/*************************************/
//		if (head != null) head.printMe();
//		if (tail != null) tail.printMe();
//
//		/**********************************/
//		/* PRINT to AST GRAPHVIZ DOT file */
//		/**********************************/
//		AstGraphviz.getInstance().logNode(
//				serialNumber,
//			"STMT\nLIST\n");
//
//		/****************************************/
//		/* PRINT Edges to AST GRAPHVIZ DOT file */
//		/****************************************/
//		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
//		if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
//	}
	
}
