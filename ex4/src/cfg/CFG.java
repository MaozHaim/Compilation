package cfg;

import ir.*;
import java.util.*;

public class CFG {

    public static final String TEMP_CHAR = "t";

    // Edges are defined at the block-level
    List<CFGBlock> blocks;
    private final Set<String> varsAndTemps;
    public Set<String> uninitializedVars;


    public CFG(List<IrCommand> commands) {
        /* You can change the implementation to have a method that constructs varsAndTemps independently,
           it will be an additional traverse over the IR-commands but will look nicer, so do what you prefer */
        this.varsAndTemps = new HashSet<>();
        this.blocks = constructGraph(commands); // This will also add all vars and temps to varsAndTemps

        chaoticIterations();

        for (CFGBlock block: blocks){
            IrCommand command = block.getBody().get(0);
            HashMap<String, Variable> out = block.getOut();

            System.out.println("Command " + command);
            System.out.print("Parents:");
            for (CFGBlock parent: block.getParents()){
                IrCommand parentCommand = parent.getBody().get(0);
                System.out.print(parentCommand + ", ");
            }
            System.out.println();
            out.forEach((key, var) -> {
                System.out.printf("(%s, %s, %s),\n", var.name, var.state == State.INITIALIZED, var.lineNum);
            });
            System.out.println("\n");
        }
        // After that, traverse over the graph and print the uninitialized and used variables
        findUninitializedVars();
    }


    private void findUninitializedVars(){
        uninitializedVars = new HashSet<>();

        for (CFGBlock block: blocks){
            IrCommand command = block.getBody().get(0);
            if (command instanceof IrCommandLoad){
                IrCommandLoad load = (IrCommandLoad) command;
                Variable loaded = block.getOut().get(load.varName);
                if (loaded.state == State.UNINITIALIZED){
                    uninitializedVars.add(loaded.name);
                }
            }
        }
    }

    private void chaoticIterations() {
        // now that we have a set of names of variables, we can create a hashmap of them
        HashMap<String, Variable> initMap = new HashMap<>();
        for (String name: varsAndTemps){
            initMap.put(name, new Variable(name, State.UNINITIALIZED, Integer.MAX_VALUE));
        }
        // create a "demi-block" at the start to serve as a parent to the real first block
        // no need to actually add it to the list, gets reference removed after the function
        CFGBlock demi = new CFGBlock();
        demi.addChild(blocks.get(0));
        blocks.get(0).addParent(demi);

        // make sure all blocks have the same initial variable list, including demi-block of course
        // technically could set the out only for the demi-block instead, but this makes implementation a bit simpler later
        demi.setInitialOut(initMap);
        for (CFGBlock block : blocks){
            block.setInitialOut(initMap);
        }

        List<CFGBlock> workList = new ArrayList<>(blocks);

        while (!workList.isEmpty()) {
            CFGBlock block = workList.remove(0);

            boolean outChanged = block.updateOut(varsAndTemps);
            if (outChanged) workList.addAll(block.getChildren());
        }

        // remove demi-block shenanigans
        blocks.get(0).resetParents();
    }


    /** Breaks down program into single-IRcommand blocks, and connects the jumps together. */
    private List<CFGBlock> constructGraph(List<IrCommand> commands) {
        System.out.println("Creating graph with " + commands.size() + " commands");
        List<CFGBlock> blocks = getNodes(commands);
        Map<String, Integer> labelToIndex = findLabels(commands);

        for (int i = 0; i < blocks.size(); i++) {
            CFGBlock block = blocks.get(i);
            IrCommand command = block.getBody().get(0); // They're exclusively single commands as of now
            addVarOrTemp(command, varsAndTemps);
            if (command instanceof IrCommandJumpType) { // FOR PROJECT: handle functions. label "endmain"?
                IrCommandJumpType jumpCommand = (IrCommandJumpType) command;
                String jumpToLabel = jumpCommand.label_name;
                int jumpLabelIndex = labelToIndex.get(jumpToLabel);
                CFGBlock jumpToBlock = blocks.get(jumpLabelIndex);
                block.addChild(jumpToBlock);
                jumpToBlock.addParent(block);
            }
            if (!(command instanceof IrCommandJumpLabel || // not while loop, backward edge
                    i == blocks.size() - 1)) { // Last node
                block.addChild(blocks.get(i+1));
                blocks.get(i+1).addParent(block);
            }
        }
        return blocks;
    }


    private void addVarOrTemp(IrCommand command, Set<String> nameSet) {
        String name = null;
        if (command instanceof IrCommandStore)
            name = ((IrCommandStore) command).varName;
        else if (command instanceof IrCommandLoad)
            name = TEMP_CHAR + ((IrCommandLoad) command).dst.getSerialNumber();
        else if (command instanceof IrCommandBinop)
            name = TEMP_CHAR + ((IrCommandBinop) command).dst.getSerialNumber();
        else if (command instanceof IrCommandAllocate)
            name = ((IrCommandAllocate) command).var_name;
        else if (command instanceof IrCommandConstInt)
            name = TEMP_CHAR + ((IrCommandConstInt) command).t.getSerialNumber();


        if (name == null) return;

        nameSet.add(name);
    }


    private List<CFGBlock> getNodes(List<IrCommand> commands) {
        List<CFGBlock> nodes = new ArrayList<>();
        for (IrCommand command : commands) {
            System.out.println("adding block from command: " + command);
            List<IrCommand> curr = new ArrayList<>();
            curr.add(command);
            CFGBlock block = new CFGBlock(curr); // A list of single commands (will bite us in the arse later)
            nodes.add(block);
        }
        return nodes;
    }

    /** Maps all declared (not necessarily used!) labels to their index in commands */
    private static Map<String, Integer> findLabels(List<IrCommand> commands) {
        Map<String, Integer> labelIndices = new HashMap<>();
        for (int i = 0; i < commands.size(); i++) {
            IrCommand currentCommand = commands.get(i);
            if (currentCommand instanceof IrCommandLabel) {
                IrCommandLabel curr = (IrCommandLabel) currentCommand;
                labelIndices.put(curr.labelName, i);
            }
        }
        return labelIndices;
    }
}
