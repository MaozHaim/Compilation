package ast;

import types.type;

import java.util.Arrays;
import java.util.List;

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
	public AstStmtList(AstStmt head, AstStmtList tail, int lineNum)
	{
		super("stmtlist -> " + (tail != null ? "stmt stmtlist" : "stmt"), lineNum);
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

	public Type SemantMe() {
		AstStmtList current = this;
		while (current != null) {
			current.head.SemantMe();
			current = current.tail;
		}
		return null; // A sequence of statements doesn't have a type.
	}	
}
