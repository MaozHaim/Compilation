package ast;

import ir.Ir;
import temp.Temp;
import types.Type;
import types.TypeInt;

public class AstExpInt extends AstExp
{
	public int value;

	public AstExpInt(int value, int lineNum)
	{
		super("exp -> INT( %d )", lineNum);
		this.value = value;
	}


	@Override
	protected String GetNodeName() {
		return String.format("INT(%d)", value);
	}


	@Override
	public boolean isConstant() {
		return true;
	}


	@Override
	public Type SemantMe() {
		return TypeInt.getInstance();
	}


	@Override
	public Temp IRme() {
		Temp t = new Temp();
		Ir.getInstance().AddIrCommand(new IRcommandConstInt(t,value));
		return t;
	}
}
