package ast;

import types.Type;
import types.TypeArray;
import types.TypeInt;

import java.util.Arrays;
import java.util.List;

public class AstVarSubscript extends AstVar
{
	public AstVar var;
	public AstExp subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSubscript(AstVar var, AstExp subscript, int lineNum)
	{
		super("var -> var [ exp ]", lineNum); // x[index]
		this.var = var;
		this.subscript = subscript;
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(var, subscript);
	}

	@Override
	protected String GetNodeName() {
		return "SUBSCRIPT\nVAR\n...[...]";
	}

	public Type SemantMe() { // TODO: Has to be an array, right?
		Type subscriptType = subscript.SemantMe();

		if (!(subscriptType instanceof TypeInt)) { // Note: doesn't seem like you need to validate indices.
			throwException("Index must be an integer.");
		}

		Type varType = var.SemantMe();

		if (!(varType.isArray())) {
			throwException("Accessing an index is only valid with arrays.");
		}

		TypeArray arrType = (TypeArray)varType;
		return arrType.typeOfElements;
	}
}
