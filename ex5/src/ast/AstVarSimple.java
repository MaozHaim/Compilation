package ast;

import types.Type;
import temp.Temp;
import ir.Ir;
import ir.IrCommandLoad;

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

	public Temp IRme() {
		Temp t = new Temp();
		Ir.getInstance().AddIrCommand(new IrCommandLoad(t,name));
		return t;
	}

}
