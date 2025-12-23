package ast;

import types.Type;

public class AstVarSimple extends AstVar
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSimple(String name, int lineNum)
	{
		super("var -> ID( %s )", lineNum);
		this.name = name;
	}

	@Override
	protected String GetNodeName() {
		return String.format("SIMPLE\nVAR(%s)",name);
	}

	public Type SemantMe() {
		return tryTableFind(this.name);
	}

}
