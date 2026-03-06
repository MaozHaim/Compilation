package ast;

import types.Type;
import types.TypeVoid;

import java.util.Arrays;
import java.util.List;

import ir.Ir;
import ir.IrCommandParameter;
import symboltable.Metadata;
import temp.Temp;

public class AstFuncParam extends AstNode {
    public AstType type;
    public String id;
    public Metadata metadata;

    public AstFuncParam(AstType type, String id, int lineNum) {
        super("funcDec parameter (type ID)", lineNum);
        this.type = type;
        this.id = id;
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(type);
    }

    @Override
    protected String GetNodeName() {
        return String.format("PARAMETER( %s )", id);
    }

    @Override
    public Type SemantMe() {
        return SemantMe(-1);
    }

    @Override
    public Type SemantMe(int offset) {
        Type paramType = type.SemantMe();
        if (paramType instanceof TypeVoid) {
            throwException("Cannot use void type for function parameters.");
        }
        constructMetadata(offset);
        tryTableEnter(id, paramType);
        return paramType;
    }

    private void constructMetadata(int offset) {
        this.metadata = new Metadata();
        try {
            metadata.setAsVariable();
            metadata.setParameter();
            metadata.setOffset(offset);
            System.out.println("Found parameter: " + id + " with offset " + offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Temp IRme() {
        Ir.getInstance().AddIrCommand(new IrCommandParameter(id, metadata.getOffset()));
        return null;
    }
}