package ast;

import types.Type;
import types.TypeArray;
import types.TypeInt;
import types.TypeClass;
import ir.Ir;
import ir.IrCommandLoadWithOffset;
import ir.IrCommandBranchLTZ;
import ir.IrCommandBranchGE;
import ir.IrCommandAddImmediate;
import ir.IrCommandSLL;
import ir.IrCommandBinopAddIntegers;
import ir.IrPatterns;

import temp.Temp;

import java.util.Arrays;
import java.util.List;

import symboltable.Metadata;

public class AstVarSubscript extends AstVar {
	public AstVar var;
	public AstExp subscript;
	public Metadata metadata;
	public String varName;
	public TypeClass classType;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSubscript(AstVar var, AstExp subscript, int lineNum) {
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

	public Type SemantMe() {
		Type subscriptType = subscript.SemantMe();

		if (!(subscriptType instanceof TypeInt)) { // Note: doesn't seem like you need to validate indices.
			throwException("Index must be an integer.");
		}

		if (subscript instanceof AstExpInt && ((AstExpInt) subscript).value < 0) {
			throwException("Array index cannot be negative.");
		}

		Type varType = var.SemantMe();

		if (!(varType.isArray())) {
			throwException("Accessing an index is only valid with arrays.");
		}

		TypeArray arrType = (TypeArray) varType;
		return arrType.typeOfElements;
	}

	public Temp IRme() {
		Ir ir = Ir.getInstance();

		System.out.println("for " + id + ": calculating " + var.id + "[" + subscript + "]");
		Temp ptrToArray = var.IRme();
		Temp index = subscript.IRme();
		Temp array = new Temp();
		Temp size = new Temp();
		Temp indexPlus1 = new Temp();
		Temp shiftedOffset = new Temp();
		Temp dst = new Temp();

		ir.add(new IrCommandLoadWithOffset(array, ptrToArray, 0)); // get address saved in address
		ir.add(new IrCommandLoadWithOffset(size, array, 0)); // get size of array
		ir.add(new IrCommandBranchLTZ(index, IrPatterns.OUT_OF_BOUNDS_LABEL, true)); // index < 0
		ir.add(new IrCommandBranchGE(index, size, IrPatterns.OUT_OF_BOUNDS_LABEL, true)); // index >= size

		ir.add(new IrCommandAddImmediate(indexPlus1, index, 1)); // index + 1
		ir.add(new IrCommandSLL(shiftedOffset, indexPlus1, 2)); // multiply by 4
		ir.add(new IrCommandBinopAddIntegers(dst, shiftedOffset, array)); // array[shiftedOffset]

		return dst;
	}
}
