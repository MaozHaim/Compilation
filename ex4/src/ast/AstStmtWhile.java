package ast;

import symboltable.SymbolTable;
import types.Type;
import types.TypeInt;

import java.util.Arrays;
import java.util.List;

public class AstStmtWhile extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtWhile(AstExp cond, AstStmtList body, int lineNum)
	{
		super("stmt -> WHILE LPAREN exp RPAREN LBRACE stmtlist RBRACE", lineNum); // while (exp) {...}
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

	public Type SemantMe() {
		/* COPIED FROM StmtIf */
		SymbolTable symbolTable = SymbolTable.getInstance();
		Type conditionType = cond.SemantMe();

		if (!(conditionType instanceof TypeInt)) {
			throwException("Conditions must be of type INT.");
		}

		symbolTable.beginScope();

		if (body != null) { 
			body.SemantMe(); 
		}

		symbolTable.endScope();
		return null;
	}
}