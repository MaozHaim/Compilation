package ast;

import types.Type;
import types.TypeInt;
import symboltable.SymbolTable;

import java.util.Arrays;
import java.util.List;

public class AstStmtIf extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIf(AstExp cond, AstStmtList body, int lineNum)
	{
		super("IF LPAREN exp RPAREN LBRACE stmtlist RBRACE", lineNum); // if (exp) {...}
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

	public Type SemantMe() {
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