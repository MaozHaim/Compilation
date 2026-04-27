package ast;

import types.Type;
import types.TypeClass;
import types.TypeFunction;
import utils.Pair;
import temp.Temp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ir.InitialConstVal;
import ir.Ir;
import ir.IrCommandLabel;
import ir.IrCommandWord;
import ir.IrCommandAllocateGlobal;
import ir.IrCommandBeginData;
import ir.IrCommandGenerateStringConsts;
import ir.IrCommandBeginText;
import ir.IrCommandGenerateBuiltinFuncs;

public class AstProgram extends AstNode {
    public List<AstDec> declarations;

    private static final Set<String> stringConstants = new HashSet<>();
    private static final List<AstClassDec> classes = new ArrayList<>();
    private static final List<Pair<String, InitialConstVal>> globals = new ArrayList<>();

    public static void addClass(AstClassDec cls) {
        classes.add(cls);
    }

    public static void addGlobal(Pair<String, InitialConstVal> name) {
        globals.add(name);
    }

    public static List<Pair<String, InitialConstVal>> getGlobals() {
        return globals;
    }

    public static void addStringConstant(String constant) {
        stringConstants.add(constant);
    }

    public AstProgram(NodeList<AstDec> declarations, int lineNum) {
        super("program -> dec+", lineNum);
        this.declarations = declarations.unroll();
    }

    @Override
    public Type SemantMe() {
        for (AstDec dec : this.declarations) {
            dec.SemantMe();
        }
        return null;
    }

    @Override
    protected String GetNodeName() {
        return "PROGRAM\nDECLARATIONS";
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return declarations;
    }

    @Override
    public Temp IRme() {
        Ir ir = Ir.getInstance();
        ir.AddIrCommand(new IrCommandBeginData());
        ir.AddIrCommand(new IrCommandGenerateStringConsts(stringConstants));
        generateGlobals();
        generateVtables();
        ir.AddIrCommand(new IrCommandBeginText());
        ir.AddIrCommand(new IrCommandGenerateBuiltinFuncs());

        for (AstDec dec : this.declarations)
            dec.IRme();
        return null;
    }

    private void generateGlobals() {
        for (Pair<String, InitialConstVal> name_init : globals) {
            Ir.getInstance().AddIrCommand(
                    new IrCommandAllocateGlobal(
                            name_init.first,
                            name_init.second.getValue(),
                            name_init.second.isString()));
        }
    }

    private void generateVtables() {
        Ir ir = Ir.getInstance();

        for (AstClassDec clsDec : classes) {
            List<Pair<TypeClass, TypeFunction>> methods = clsDec.typeObject.methodsInfo;
            ir.AddIrCommand(new IrCommandLabel("vtable_" + clsDec.id));
            for (Pair<TypeClass, TypeFunction> cls_method : methods) {
                ir.AddIrCommand(new IrCommandWord(cls_method.first.name + "_" + cls_method.second.name));
            }
        }
    }
}