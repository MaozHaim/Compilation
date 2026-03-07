package ast;

import ir.Ir;
import ir.IrCommandLoad;
import types.Type;
import temp.Temp;

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
	public Temp IRme() {
		Temp addr = var.IRme();
		Temp dst = new Temp();

		Ir.getInstance().AddIrCommand(new IrCommandLoad(dst, addr, 0));
		return dst;
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
