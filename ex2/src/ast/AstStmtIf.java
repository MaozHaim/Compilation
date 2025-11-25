package ast;

import java.util.List;
import java.util.Arrays;

public class AstStmtIf extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIf(AstExp cond, AstStmtList body)
	{
		super("IF LPAREN exp RPAREN LBRACE stmtlist RBRACE");
		this.cond = cond;
		this.body = body;
	}

	@Override
	protected String GetNodeName() {
		return "IF\n(EXP) {STMTLIST}";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(cond, body);
	}
}