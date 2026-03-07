package ast;

import types.Type;
import temp.Temp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstStmtList extends AstNode
{
	public AstStmt head;
	public AstStmtList tail;


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

	public Temp IRme() {
		if (head != null) head.IRme();
		if (tail != null) tail.IRme();

		return null;
	}


	public List<AstStmt> getStatements(){
		List<AstStmt> statements = new ArrayList<>();
		AstStmtList curr = this;
		do {
			statements.add(curr.head);
			curr = curr.tail;
		} while(curr != null);

		return statements;
	}
}
