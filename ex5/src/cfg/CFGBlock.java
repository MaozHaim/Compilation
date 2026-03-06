package cfg;

import ir.*;
import java.util.*;


public class CFGBlock {

    private final List<IrCommand> body;
    private final List<CFGBlock> parents;
    private final List<CFGBlock> children;
    private Set<Integer> out;


    public CFGBlock(List<IrCommand> commands) {
        if (commands == null || commands.isEmpty()) { // Are empty blocks fine? o_O
            System.err.println("Cannot make a CFGBlock with no instructions.");
            throw new IllegalArgumentException();
        }
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.body = List.copyOf(commands);
        this.out = new HashSet<>();
    }


    // This constructor is used specifically for the demi-block
    public CFGBlock(){
        this.body = null;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.out = new HashSet<>();
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
     * Changes out in-place!
     * @return true if the update changed the previously calculated out.
     */
    public boolean updateOut(){

        // Compute current in set - union all out sets of the children (backward analysis)
        Set<Integer> inSet = new HashSet<>();
        for (CFGBlock child : this.children) {
            inSet.addAll(child.getOut());
        }

        // Update our out set according to the in set (change inSet in place - this is the updated out set)
        if (this.body != null) {
            IrCommand command = this.body.get(0);
            command.calcOut(inSet); // Update the out set of the command (changes inSet in place)
        }

        // Check if the out set after update (it is inSet - was changed in place - yeah the name is confusing)
        // has changed from the previous out set (this.out)
        System.out.println("old out set: " + this.out);
        System.out.println("new out set: " + inSet);
        boolean changed = !(inSet.equals(this.out));
        if (changed) this.out = inSet;

        return changed;
    }


    public List<IrCommand> getBody() {
        return body;
    }


    public Set<Integer> getOut() {
        return out;
    }


    public List<CFGBlock> getChildren() {
        return children;
    }


    public List<CFGBlock> getParents() {
        return parents;
    }
}
