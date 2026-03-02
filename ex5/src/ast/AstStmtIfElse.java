package ast;

import symboltable.SymbolTable;
import types.Type;
import types.TypeInt;

import temp.Temp;
import ir.Ir;
import ir.IrCommand;
import ir.IrCommandLabel;
import ir.IrCommandJumpIfEqZero;
import ir.IrCommandJumpLabel;

import java.util.Arrays;
import java.util.List;

public class AstStmtIfElse extends AstStmt
{
	public AstExp cond;
	public AstStmtList ifBody;
    public AstStmtList elseBody;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIfElse(AstExp cond, AstStmtList ifBody, AstStmtList elseBody, int lineNum)
	{
		super("IF LPAREN exp RPAREN LBRACE stmtlist RBRACE ELSE LBRACE stmtlist RBRACE", lineNum);
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

	public Type SemantMe() {
		SymbolTable symbolTable = SymbolTable.getInstance();
		Type conditionType = cond.SemantMe();

		if (!(conditionType instanceof TypeInt)) {
			throwException("Conditions must be of type INT.");
		}

		symbolTable.beginScope();

		if (ifBody != null) { 
			ifBody.SemantMe(); 
		}

		symbolTable.endScope();
		symbolTable.beginScope();

        if (elseBody != null) {
            elseBody.SemantMe();
        }

        symbolTable.endScope();
		return null;
	}

	public Temp IRme() {
		Ir irList = Ir.getInstance();
		String startLabel = IrCommand.getFreshLabel("start");
		String elseLabel = IrCommand.getFreshLabel("else");
		String endLabel = IrCommand.getFreshLabel("end");

		irList.AddIrCommand(new IrCommandLabel(startLabel));
		irList.AddIrCommand(new IrCommandJumpIfEqZero(cond.IRme(), elseLabel));
		if (ifBody != null) { ifBody.IRme(); }
		irList.AddIrCommand(new IrCommandJumpLabel(endLabel));

		irList.AddIrCommand(new IrCommandLabel(elseLabel));
		if (elseBody != null) { elseBody.IRme(); }

		irList.AddIrCommand(new IrCommandLabel(endLabel));

		return null;
	}

}