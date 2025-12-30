package cfg;

import ir.*;
import java.util.*;


public class CFGBlock {

    private final List<IrCommand> body;
    private final List<CFGBlock> parents;
    private final List<CFGBlock> children;
    private HashMap<String, Variable> out;


    // TODO: Should all variables be uninitialized by default? Answer: nah probably not. Can't think what that's good for.
    public CFGBlock(List<IrCommand> commands) {
        if (commands == null || commands.isEmpty()) { // Are empty blocks fine? o_O
            System.err.println("Cannot make a CFGBlock with no instructions.");
            throw new IllegalArgumentException();
        }
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.body = List.copyOf(commands);
    }

    // This constructor is used specifically for the demi-block instruction at the very beginning,
    // which marks the body as null. This is fine, since the demi-block never runs updateOut.
    public CFGBlock(){
        body = null;
        parents = null;
        children = new ArrayList<>();
    }

    /**
     * Used specifically on the first block after chaotic iteration to remove reference to the demi-block
     * just used as a precaution, not important really
     */
    public void resetParents(){
        parents.clear();
    }

    /**
     * used to make sure all cfg blocks have the same variable structure
     * @param initialMap
     */
    public void setInitialOut(Map<String, Variable> initialMap){
        // duplicate initial map using duplicator constructor
        out = new HashMap<>();
        initialMap.forEach((name, var) -> out.put(name, new Variable(var)));
    }


    public void addChild(CFGBlock block) {
        if (block == null) { throw new IllegalArgumentException(); }
        if (children.size() >= 2) {
            System.err.println("Too many children");
            throw new IllegalArgumentException();
        }
        children.add(block);
    }


    public void addParent(CFGBlock block) {
        if (block == null) { throw new IllegalArgumentException(); }
        parents.add(block);
    }


    /**
     * Calculates the out of the current node in chaotic iterations.
     * Assumes parents have same out order and amount of variables.
     * Notice! Changes out in-place! (hee hee out in-place)
     * @return true if the update changed the previously calculated out.
     */
    public boolean updateOut(Set<String> varNames){
        boolean initialsChanged = false;
        for (String name: varNames){
            Variable newVar;
            if (parents.size() == 1)
                newVar = parents.get(0).getOut().get(name);
            else{
                Variable v1 = parents.get(0).getOut().get(name);
                Variable v2 = parents.get(1).getOut().get(name);

                newVar = v1.lineNum < v2.lineNum ? v1 : v2;
            }
            Variable oldVar = out.get(name);

            // technically calculateOutFromIn could revert the change found here, but this is complex enough i think
            initialsChanged = initialsChanged || (oldVar.lineNum != newVar.lineNum || oldVar.state != newVar.state);
            oldVar.state = newVar.state;
            oldVar.lineNum = newVar.lineNum;
        }

        boolean transformationChanged = recalcOutAfterIn();

        return initialsChanged || transformationChanged; // good enough heuristic
    }

    /**
     * After the parents in has been properly calculated, this function takes the changed inplace out and
     * applies the appropriate transformation given the IR command this block represents.
     * @return true iff out changed from the calculation or not
     */
    private boolean recalcOutAfterIn(){
        // Honestly we should've abandoned CFGblocks being lists instead of just single ir commands a while ago
        IrCommand command = body.get(0);

        boolean changed = false;

        if (command instanceof IrCommandBinop) updateWithBinop((IrCommandBinop) command);
        else if (command instanceof IrCommandLoad) updateWithLoad((IrCommandLoad) command);
        else if (command instanceof IrCommandStore) updateWithStore((IrCommandStore) command);
        else if (command instanceof IrCommandConstInt) updateWithConst((IrCommandConstInt) command);
        else if (command instanceof IrCommandAllocate) updateWithAlloc((IrCommandAllocate) command);

        return changed;
    }

    private boolean updateWithBinop(IrCommandBinop binop){
        // this... feels bad... eh
        String dstStr = CFG.TEMP_CHAR + binop.dst.getSerialNumber();
        String t1Str = CFG.TEMP_CHAR + binop.t1.getSerialNumber();
        String t2Str = CFG.TEMP_CHAR + binop.t2.getSerialNumber();

        Variable t1 = out.get(t1Str);
        Variable t2 = out.get(t2Str);
        Variable dstVar = out.get(dstStr);

        State newState = t1.state == State.INITIALIZED && t2.state == State.INITIALIZED ?
                State.INITIALIZED : State.UNINITIALIZED;

        boolean changed = dstVar.state != newState || dstVar.lineNum != binop.index;
        dstVar.state = newState;
        dstVar.lineNum = binop.index;

        return changed;
    }

    private boolean updateWithStore(IrCommandStore store){
        String srcName = CFG.TEMP_CHAR + store.src.getSerialNumber();
        String varName = store.varName;

        Variable src = out.get(srcName);
        Variable var = out.get(varName);

        boolean changed = var.state != src.state || var.lineNum != store.index;
        var.state = src.state;
        var.lineNum = store.index;

        return changed;
    }

    public boolean updateWithAlloc(IrCommandAllocate alloc){
        String varName = alloc.var_name;

        Variable toUninit = out.get(varName);

        boolean changed = toUninit.state != State.UNINITIALIZED || toUninit.lineNum != alloc.index;
        toUninit.state = State.UNINITIALIZED;
        toUninit.lineNum = alloc.index;

        return changed;
    }

    public boolean updateWithLoad(IrCommandLoad load){
        // just a copy of store with a switch to the cfg
        String varName = load.varName;
        String dstName = CFG.TEMP_CHAR + load.dst.getSerialNumber();

        Variable dst = out.get(dstName);
        Variable var = out.get(varName);

        boolean changed = var.state != dst.state || dst.lineNum != load.index;
        dst.state = var.state;
        dst.lineNum = load.index;

        return changed;
    }

    public boolean updateWithConst(IrCommandConstInt constCommand){
        // like update with alloc but in reverse
        String tempName = CFG.TEMP_CHAR + constCommand.t.getSerialNumber();

        Variable toInit = out.get(tempName);

        boolean changed = toInit.state != State.INITIALIZED || toInit.lineNum != constCommand.index;
        toInit.state = State.INITIALIZED;
        toInit.lineNum = constCommand.index;

        return changed;
    }

    public List<IrCommand> getBody() {
        return body;
    }

    public HashMap<String, Variable> getOut() {
        return out;
    }


    public List<CFGBlock> getChildren() {
        return children;
    }


    public List<CFGBlock> getParents() {
        return parents;
    }
}
