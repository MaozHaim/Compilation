package ast;

import java.util.List;
import java.util.Arrays;

public class AstStmtWhile extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtWhile(AstExp cond, AstStmtList body)
	{
		super("stmt -> WHILE LPAREN exp RPAREN LBRACE stmtlist RBRACE");
		this.cond = cond;
		this.body = body;
	}

	@Override
	protected String GetNodeName() {
		return "WHILE\n(COND) {BODY}";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(cond, body);
	}
}