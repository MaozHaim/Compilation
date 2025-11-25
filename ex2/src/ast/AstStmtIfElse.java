package ast;

import java.util.List;
import java.util.Arrays;

public class AstStmtIfElse extends AstStmt
{
	public AstExp cond;
	public AstStmtList ifBody;
    public AstStmtList elseBody;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIfElse(AstExp cond, AstStmtList ifBody, AstStmtList elseBody)
	{
		super("IF LPAREN exp RPAREN LBRACE stmtlist RBRACE ELSE LBRACE stmtlist RBRACE");
		this.cond = cond;
		this.ifBody = ifBody;
        this.elseBody = elseBody;
	}

	@Override
	protected String GetNodeName() {
		return "IF\n(EXP) {STMTLIST}\nELSE\n{STMTLIST}";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(cond, ifBody, elseBody);
	}
}