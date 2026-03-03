package cfg;

import ir.*;
import java.util.*;


public class CFGBlock {

    private final List<IrCommand> body;
    private final List<CFGBlock> parents;
    private final List<CFGBlock> children;
    private HashMap<String, Variable> out;
    private Set<Integer> liveOut;


    // TODO: Should all variables be uninitialized by default? Answer: nah probably not. Can't think what that's good for.
    public CFGBlock(List<IrCommand> commands) {
        if (commands == null || commands.isEmpty()) { // Are empty blocks fine? o_O
            System.err.println("Cannot make a CFGBlock with no instructions.");
            throw new IllegalArgumentException();
        }
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.body = List.copyOf(commands);
        this.liveOut = new HashSet<>();
    }

    // This constructor is used specifically for the demi-block instruction,
    // which marks the body as null.
    public CFGBlock(){
        body = null;
        parents = new ArrayList<>();
        children = new ArrayList<>();
        liveOut = new HashSet<>();
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


    // ==================== Ex4: Forward Uninitialized Variable Analysis ====================

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


    // ==================== Ex5: Backward Liveness Analysis ====================

    /*
        WHAT IR_COMMANDS SHOULD BE TAKEN INTO ACCOUNT WHEN UPDATING THE OUT SET?
        2. Array_Create - IGNORE?
        3. Array_Set - IGNORE?
        4. Binop - DONE
        5. BranchGE - DONE
        6. BranchLT - DONE
        7. Jump_If_Eq_To_Zero - DONE
        8. CallFunc - DONE
        9. CallMethod - DONE, BUT CHECK AGAIN!
        10. ConstInt - DONE
        11. ConstString - DONE
        12. Load - DONE
        13. LoadWithOffset - DONE
        14. Move - DONE
        15. NewArrayObject - DONE
        16. NewClassObject - DONE
        17. PrintInt - DONE
        18. Return - DONE
        19. Store - DONE
        20. StoreAt - DONE
    */

    /**
     * Calculates the liveness out set of the current node (backward analysis).
     * Unions children's liveOut sets, then applies the command's calcOut.
     * @return true if the liveOut set changed.
     */
    public boolean updateLiveOut(){

        // Compute current in set - union all out sets of the children (backward analysis)
        Set<Integer> inSet = new HashSet<>();
        for (CFGBlock child : this.children) {
            inSet.addAll(child.getLiveOut());
        }

        // Update our out set according to the in set (change inSet in place - this is the updated out set)
        if (this.body != null) {
            IrCommand command = this.body.get(0);
            command.calcOut(inSet); // Update the out set of the command (changes inSet in place)
        }

        // Check if the out set after update (it is inSet - was changed in place - yeah the name is confusing)
        // has changed from the previous out set (this.liveOut)
        System.out.println("old out set: " + this.liveOut);
        System.out.println("new out set: " + inSet);
        boolean changed = !(inSet.equals(this.liveOut));
        if (changed) this.liveOut = inSet;

        return changed;
    }


    // ==================== Getters ====================

    public List<IrCommand> getBody() {
        return body;
    }

    public HashMap<String, Variable> getOut() {
        return out;
    }

    public Set<Integer> getLiveOut() {
        return liveOut;
    }


    public List<CFGBlock> getChildren() {
        return children;
    }


    public List<CFGBlock> getParents() {
        return parents;
    }
}
