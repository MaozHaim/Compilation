package ast;

import ir.IrPatterns;
import temp.Temp;
import types.Type;
import types.TypeInt;

import java.util.Arrays;
import java.util.List;

public class AstExpDivide extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	public Type type;
	
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
	public String toString() {
		return "ExpBinop " + op.op + " " + left.toString() + " " + right.toString() + " " + type.toString();
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
		if (right instanceof AstExpInt) {
			AstExpInt rightInt = (AstExpInt) right;
			if (rightInt.value == 0) {
				throwException("DIVIDE - Explicit division by zero.");
			}
		}

		this.type = typeLeft;
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

		IrPatterns.divide(dst, t1, t2);
		IrPatterns.clampInteger(dst); // -32768 / -1 overflows the 16-bit range; saturate to 32767
		return dst;
	}
}
