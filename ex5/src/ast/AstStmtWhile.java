package ast;

import symboltable.SymbolTable;
import types.Type;
import types.TypeInt;

import temp.Temp;
import ir.Ir;
import ir.IrCommand;
import ir.IrCommandJumpIfEqZero;
import ir.IrCommandLabel;
import ir.IrCommandJumpLabel;

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

	/**
	 * START: condition
	 * 		  <body>
	 * 		  goto START
	 * END: <rest of code>
	 */
	@Override
	public Temp IRme() {
		String label_end = IrCommand.getFreshLabel("end");
		String label_start = IrCommand.getFreshLabel("start");

		Ir.getInstance().AddIrCommand(new IrCommandLabel(label_start));
		Temp cond_temp = cond.IRme();
		Ir.getInstance().AddIrCommand(new IrCommandJumpIfEqZero(cond_temp,label_end)); /* [4] Jump conditionally to the loop end */
		body.IRme();
		Ir.getInstance().AddIrCommand(new IrCommandJumpLabel(label_start)); /* [6] Jump to the loop entry */
		Ir.getInstance().AddIrCommand(new IrCommandLabel(label_end)); /* [7] Loop end label */

		return null;
	}
}