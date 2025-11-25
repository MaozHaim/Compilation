package ast;

import java.util.List;
import java.util.Arrays;

public abstract class AstNode
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int serialNumber;

	public AstNode(String derivation) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.println("====================== " + derivation);
	}

	protected abstract String GetNodeName();

	protected List<? extends AstNode> GetChildren() { return Arrays.asList(); }

	public final void PrintMe(){
		// print me, add me as a node, do the same to my children, log the edges to them
		System.out.println("next node: \n***\n" + GetNodeName() + "\n***");

		AstGraphviz.getInstance().logNode( serialNumber, GetNodeName());

		for (AstNode child : GetChildren()){
			child.PrintMe();
			AstGraphviz.getInstance().logEdge(serialNumber, child.serialNumber);
		}
	}

	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void printMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}
}
