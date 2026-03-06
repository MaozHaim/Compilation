package ast;

import ir.IrCommandLoad;
import symboltable.SymbolTable;
import types.Type;
import types.TypeClass;
import temp.Temp;
import ir.Ir;
import ir.IrCommandAddImmediate;
import ir.IrPatterns;

import java.util.Arrays;
import java.util.List;

public class AstVarField extends AstVar {
	public AstVar var;
	public String fieldName;
	TypeClass classType;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarField(AstVar var, String fieldName, int lineNum) {
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

		classType = (TypeClass) type;
		Type fieldType = table.findMemberType(classType, fieldName);

		if (fieldType == null) {
			throwException("undefined field " + fieldName);
		}

		return fieldType;
	}

	@Override
	public Temp IRme() {
		Temp address = var.IRme();
		int offset = classType.getAttributeIndex(fieldName);
		Temp dst = new Temp();

		Ir ir = Ir.getInstance();

		ir.AddIrCommand(new IrCommandLoad(dst, address, 0)); // get address saved in address
		IrPatterns.checkNullRef(dst);
		ir.AddIrCommand(new IrCommandAddImmediate(dst, offset * 4)); // *4 cuz word bird bird bird bird is the word

		return dst;
	}

}
