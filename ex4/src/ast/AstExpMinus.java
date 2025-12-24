package ast;

import ir.Ir;
import ir.IrCommand;
import ir.IrCommandMinus;
import temp.Temp;
import types.Type;
import types.TypeInt;

import java.util.Arrays;
import java.util.List;

public class AstExpMinus extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpMinus(AstExp left, AstExp right, int lineNum)
	{
		super("exp -> exp minus exp", lineNum);

		this.left = left;
		this.right = right;
		this.op = new AstBinop("-", lineNum);
	}


	@Override
	protected String GetNodeName() {
		return "ExpMinus";
	}


	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}


	@Override
	public Type SemantMe(){
		Type typeLeft, typeRight;

		typeLeft = left.SemantMe();
		typeRight = right.SemantMe();

		boolean typesMismatch = !typeLeft.equals(typeRight);
		boolean areIntegers = (typeLeft instanceof TypeInt);

		if (typesMismatch) {
			throwException("BINOP only works on identical types.");
		}
		if (!areIntegers) {
			throwException("MINUS - Operation defined only for integers.");
		}
		return typeLeft;
	}


	@Override
	public Temp IRme() {
		Temp t1 = null;
		Temp t2 = null;
		Temp dst = new Temp();

		if (left  != null) {
			t1 = left.IRme();
		}
		if (right != null) {
			t2 = right.IRme();
		}

		IrCommand command = new IrCommandMinus(dst, t1, t2);
		Ir.getInstance().AddIrCommand(command);

		return dst;
	}
}
