package ast;

import ir.Ir;
import ir.IrCommand;
import ir.IrCommandEq;
import temp.Temp;
import types.Type;
import types.TypeArray;
import types.TypeClass;
import types.TypeInt;
import types.TypeNil;

import java.util.Arrays;
import java.util.List;

public class AstExpEq extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;


	public AstExpEq(AstExp left, AstExp right, int lineNum)
	{
		super("exp -> exp EQ exp", lineNum);

		this.left = left;
		this.right = right;
		this.op = new AstBinop("=", lineNum);
	}


	@Override
	protected String GetNodeName() {
		return "ExpEq";
	}


	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}


	@Override
	public Type SemantMe() {
		Type typeLeft, typeRight;

		typeLeft = left.SemantMe();
		typeRight = right.SemantMe();

		boolean oneIsClass = typeLeft instanceof TypeClass || typeRight instanceof TypeClass;
		boolean oneIsArray = typeLeft instanceof TypeArray || typeRight instanceof TypeArray;
		boolean oneIsObject = oneIsArray || oneIsClass;
		boolean oneIsNil = typeLeft instanceof TypeNil || typeRight instanceof TypeNil;

		if (typeLeft.equals(typeRight)) {
			return TypeInt.getInstance();
		}
		if (oneIsObject && oneIsNil) {
			return TypeInt.getInstance();
		}
		if (oneIsClass) {
			if (typeLeft instanceof TypeClass && typeRight instanceof TypeClass) {
				TypeClass leftClass = (TypeClass) typeLeft;
				TypeClass rightClass = (TypeClass) typeRight;
				if (leftClass.isSubtypeOf(rightClass) || rightClass.isSubtypeOf(leftClass)) {
					return TypeInt.getInstance();
				}
			}
		}
		throwException("Illegal EQ operation");
		return null; // Unreachable
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

		IrCommand command = new IrCommandEq(dst, t1, t2);
		Ir.getInstance().AddIrCommand(command);

		return dst;
	}
}
