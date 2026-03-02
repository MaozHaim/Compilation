package ast;

import types.Type;
import types.TypeArray;
import types.TypeClass;
import types.TypeNil;

import temp.Temp;
import ir.Ir;
import ir.IrCommandStore;

import java.util.Arrays;
import java.util.List;

public class AstStmtAssign extends AstStmt
{
	/***************/
	/*  var := exp */
	/***************/
	public AstVar var;
	public AstExp exp;

	public AstStmtAssign(AstVar var, AstExp exp, int lineNum)
	{
		super("stmt -> var ASSIGN exp SEMICOLON", lineNum); // var := exp;
		this.var = var;
		this.exp = exp;
	}

	@Override
	protected String GetNodeName() {
		return "ASSIGN\nleft := right";
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(var, exp);
	}

	public Type SemantMe() {
		Type leftTypeData = var.SemantMe();
		Type rightTypeData = exp.SemantMe();

		if (leftTypeData.equals(rightTypeData)) return null;

		if (leftTypeData.isArray()) {
			arraySemantCheck((TypeArray)leftTypeData, rightTypeData, exp.isNewExp());
			return null;
		}

		if (leftTypeData instanceof TypeClass) {
			classSemantCheck((TypeClass)leftTypeData, rightTypeData);
			return null;
		}

		throwException("Type mismatch");
		return null; // can't reach this point
	}

	private void arraySemantCheck(TypeArray leftArr, Type rightType, boolean isNewExp){
		if (!(rightType.isArray())) {
			if(!(rightType instanceof TypeNil)) // nil is valid to array
				throwException("expression must be of array type");
			else return;
		}

		TypeArray rightArr = (TypeArray)rightType;
		// the reason for || !isNewExp is that otherwise the array types must be strictly the same, and that was already tested for
		if (!leftArr.typeOfElements.equals(rightArr.typeOfElements) || !isNewExp)
			throwException("Assignment of differing array types");

	}

	private void classSemantCheck(TypeClass leftClass, Type rightType){
		if (!(rightType instanceof TypeClass)) {
			if(!(rightType instanceof TypeNil)) {// nil is valid to class
				throwException("expression must be of class type");
			}
			else return;
		}

		TypeClass rightClass = (TypeClass) rightType;

		if (!rightClass.isSubtypeOf(leftClass)) {
			throwException("Expression does not inherit from variable's class");
		}
	}
	
	@Override
	public Temp IRme() {
		Temp src = exp.IRme();

		if (var instanceof AstVarSimple){
			AstVarSimple simplevar = (AstVarSimple) var;
			Ir.getInstance().AddIrCommand( new IrCommandStore(simplevar.name, src));
		}


		return null;
	}

}
