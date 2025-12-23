package ast;

import symboltable.SymbolTable;
import types.Type;
import types.TypeClass;

import java.util.Arrays;
import java.util.List;

public class AstVarField extends AstVar
{
	public AstVar var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarField(AstVar var, String fieldName, int lineNum)
	{
		super("var -> var DOT ID( %s )", lineNum); // x.attribute
		this.var = var;
		this.fieldName = fieldName;
	}

	@Override
	protected String GetNodeName() {
		return String.format("FIELD\nVAR...->%s", fieldName);
	}

	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(var);
	}

	public Type SemantMe() {
		SymbolTable table = SymbolTable.getInstance();
		Type type = var.SemantMe();

		if (!(type instanceof TypeClass)) {
			throwException("Object is not of type class, has no fields");
		}

		TypeClass classType = (TypeClass) type;
		Type fieldType = table.findMemberType(classType, fieldName);

		if (fieldType == null) { 
			throwException("undefined field " + fieldName); 
		}
		
		return fieldType;
	}

}
