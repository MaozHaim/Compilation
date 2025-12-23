package ast;

import types.Type;

import java.util.Arrays;
import java.util.List;

public class AstExpVar extends AstExp
{
	public AstVar var;

	public AstExpVar(AstVar var, int lineNum)
	{
		super("exp -> var", lineNum);
		this.var = var;
	}

	public Type SemantMe() {
		return this.var.SemantMe();
	}

	@Override
	protected String GetNodeName() {
		return "EXP\nVAR";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(var);
	}
}
