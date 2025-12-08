package ast;

import types.Type;
import types.TypeInt;

import java.util.Arrays;
import java.util.List;

public class AstExpDivide extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpDivide(AstExp left, AstExp right, int lineNum)
	{
		super("exp -> exp divide exp", lineNum);

		this.left = left;
		this.right = right;
		this.op = new AstBinop("/", lineNum);
	}


	@Override
	protected String GetNodeName() {
		return "ExpDivide";
	}


	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op,  right);
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
			throwException("DIVIDE - Operation defined only for integers.");
		}
		if (right instanceof AstExpInt rightInt && rightInt.value == 0) {
			throwException("DIVIDE - Explicit division by zero.");
		}
		return typeLeft;
	}
}
