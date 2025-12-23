package ast;

import types.Type;
import types.TypeInt;

import java.util.Arrays;
import java.util.List;

public class AstExpTimes extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpTimes(AstExp left, AstExp right, int lineNum)
	{
		super("exp -> exp times exp", lineNum);

		this.left = left;
		this.right = right;
		this.op = new AstBinop("*", lineNum);
	}


	@Override
	protected String GetNodeName() {
		return "ExpTimes";
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
			throwException("TIMES - Operation defined only for integers.");
		}
		return typeLeft;
	}
}
