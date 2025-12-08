package ast;

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
		return String.format("INT(%d)",value);
	}


	@Override
	public boolean isConstant() {
		return true;
	}


	public Type SemantMe() {
		return TypeInt.getInstance();
	}
}
