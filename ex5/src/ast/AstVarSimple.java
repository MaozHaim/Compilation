package ast;

import types.Type;
import types.TypeClass;
import temp.Temp;
import ir.Ir;
import ir.IrCommandLoad;
import ir.IrCommandPlaceholder;
import ir.IrCommandGetAttributeAddress;
import ir.IrCommandGetLocalAddress;
import ir.IrCommandGetGlobalAddress;
import ir.IrCommandGetParameterAddress;
import symboltable.SymbolTable;
import symboltable.Metadata;

public class AstVarSimple extends AstVar {
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public TypeClass classType; // Null if not in class
	public Metadata metadata;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSimple(String name, int lineNum) {
		super("var -> ID( %s )", lineNum);
		this.name = name;
	}

	@Override
	protected String GetNodeName() {
		return String.format("SIMPLE\nVAR(%s)", name);
	}

	public Type SemantMe() {
		Type type = tryTableFind(this.name);
		this.classType = SymbolTable.getInstance().currentClass;
		this.metadata = tryTableFindMetadata(this.name);
		if (this.metadata == null) {
			System.out.println("METADATA HASN'T BEEN INITIALIZED FOR: " + this.name);
		}
		return type;
	}

	public Temp IRme() {
		Temp dst = new Temp();

		if (this.metadata == null) {
			Ir.getInstance().AddIrCommand(new IrCommandPlaceholder("No metadata for: " + this.name));
		} else if (this.metadata.getRole() == Metadata.VAR_ROLE.ATTRIBUTE) {
			Ir.getInstance()
					.AddIrCommand(new IrCommandGetAttributeAddress(dst, name, metadata.getOffset(), metadata.getClassName()));
		} else if (this.metadata.getRole() == Metadata.VAR_ROLE.LOCAL) {
			Ir.getInstance().AddIrCommand(new IrCommandGetLocalAddress(dst, name, metadata.getOffset()));
		} else if (this.metadata.getRole() == Metadata.VAR_ROLE.GLOBAL) {
			Ir.getInstance().AddIrCommand(new IrCommandGetGlobalAddress(dst, name));
		} else if (this.metadata.getRole() == Metadata.VAR_ROLE.PARAMETER) {
			Ir.getInstance().AddIrCommand(new IrCommandGetParameterAddress(dst, name, metadata.getOffset()));
		} else {
			throwException("Failed to find metadata.");
		}
		return dst;
	}

}
