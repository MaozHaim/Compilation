package ast;

import errors.SemanticException;
import symboltable.SymbolTable;
import types.Type;

import java.util.Arrays;
import java.util.List;

public abstract class AstNode
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int serialNumber;
	protected int lineNum; // the start of the line number it was encountered in.


	public AstNode(String derivation, int lineNum) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.println("====================== " + derivation);

		this.lineNum = lineNum;
	}

	// Abstract functions
	public abstract Type SemantMe();
	protected abstract String GetNodeName();


	protected List<? extends AstNode> GetChildren() { return Arrays.asList(); }


	//TODO: is it in use?
	/** Attempts to enter a table-entry {id, type} into the symbol_table, use throwException on failure.*/
	protected final void tryTableEnter(String id, Type type){
		SymbolTable table = SymbolTable.getInstance();
		if (table.isInCurrentScope(id)){
			throwException("Name " + id + " already defined in current scope");
		}
		table.enter(id, type);
	}


	//TODO: is it in use?
	/**
	 * find() method from SymbolTable, which automatically throws an error if the object wasn't found.
	 * return The Type-class of the object if found.
	 */
	protected Type tryTableFind(String ID) {
		SymbolTable symbolTable = SymbolTable.getInstance();
		Type type = symbolTable.find(ID);
		if (type == null) {
			throwException("Name " + ID + " not found");
		}
		return type;
	}


	/**
	 * Throw SemanticException and set the global failure line in SymbolTable to the line of the current command.
	 */
	protected final void throwException(String info){
		throw new SemanticException(info, lineNum + 1); // +1 since cup's line counter starts on 0
	}

	public final void PrintMe(){
		// print me, add me as a node, do the same to my children, log the edges to them
		System.out.println("next node: \n***\n" + GetNodeName() + "\n***");

		AstGraphviz.getInstance().logNode( serialNumber, GetNodeName());

		for (AstNode child : GetChildren()){
			child.PrintMe();
			AstGraphviz.getInstance().logEdge(serialNumber, child.serialNumber);
		}
	}
	// TODO: combine those 'PrintMe' functions
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	//	public void printMe()
	//	{
	//		System.out.print("AST NODE UNKNOWN\n");
	//	}
}
