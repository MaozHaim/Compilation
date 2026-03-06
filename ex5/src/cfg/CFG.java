package cfg;

import ir.*;
import java.util.*;
import temp.Temp;

public class CFG {

    public static final String TEMP_CHAR = "t";
    private static final int K = 10; // Number of colors

    // Liveness analysis + register allocation
    List<List<CFGBlock>> graph; // holds all sub-graphs
    // First Integer - the temp serial number, second - the color (val between 0-9)
    private HashMap<Integer, Integer> tempToVal = new HashMap<>(); // for the coloring
    private Set<Temp> temps = new HashSet<>(); // to later assign registers to temps


    public CFG(List<IrCommand> commands) {

        /* ----- Liveness Analysis + Register Allocation ----- */
        System.out.println("DEBUG: Starting CFG construction");
        this.graph = constructGraph(commands);

        // for each sub-graph, we need to run the live analysis
        for (List<CFGBlock> subGraph : this.graph){
            System.out.println("DEBUG: Running liveness analysis on new subgraph of size " + subGraph.size());
            liveness(subGraph);
        }

        /*
            On EACH sub-graph:
            1. Create an interference graph
            2. Run the coloring algorithm
                -> add to the map of TEMP(represented as their serial num) to color (val between 0-9)
            3. Assign colors to the registers (in TEMP we have the assignRegister method)
        */

        for (List<CFGBlock> subGraph : this.graph){
            System.out.println("DEBUG: Constructing interference graph...");
            HashMap<Integer, Set<Integer>> interferenceGraph = constructInterGraph(subGraph);
            System.out.println("DEBUG: Interference graph size: " + interferenceGraph.size());
            // run the coloring algorithm on the interference graph
            boolean success = coloringAlgorithm(interferenceGraph); // if succeeded - it added the colors of its temps to tempToVal
            if (!success) {
                throw new RuntimeException("Register Allocation Failed");
            }
        }

        // Now we have the colors of all temps in tempToVal, we can assign them to the actual temps
        System.out.println("DEBUG: Assigning registers to temps:");
        for (Temp temp : this.temps) {
            int serialNum = temp.getSerialNumber();
            if (tempToVal.containsKey(serialNum)) {
                int color = this.tempToVal.get(serialNum);
                temp.assignRegister(color);
                System.out.println("DEBUG: TEMP t" + serialNum + " -> color " + color);
            }
            else {
                System.out.println("Temp " + temp + " not found in tempToVal, something went wrong!");
            }
        }

        System.out.println("DEBUG: CFG process completed.");
    }


    // ==================== Liveness Analysis + Register Allocation ====================

    private HashMap<Integer, Set<Integer>> constructInterGraph(List<CFGBlock> subGraph) {
        HashMap<Integer, Set<Integer>> interGraph = new HashMap<>();
        // Create the interference graph from the out sets of the commands of the subGraph
        for (CFGBlock block : subGraph) {
            if (block.getBody() == null) {
                // It's the demi-block, we don't want to do anything with it
                continue;
            }

            // It's a real command
            IrCommand command = block.getBody().get(0);
            Set<Integer> outSet = block.getOut();

            for (int temp : outSet) {
                // For any command, we need to add the temps of its out set to the regular edges map
                if (!interGraph.containsKey(temp)) {
                    interGraph.put(temp, new HashSet<>());
                }
                interGraph.get(temp).addAll(outSet);
                interGraph.get(temp).remove(temp);
            }
            System.out.println("DEBUG: Block " + command + " OUT set = " + outSet);
        }


        // Add temps that weren't in any out set (they are nodes without edges)
        for (CFGBlock block : subGraph) {
            if (block.getBody() == null) {
                // It's the demi-block, we don't want to do anything with it
                continue;
            }

            // It's a real command
            IrCommand command = block.getBody().get(0);
            Set<Temp> commandTemps = new HashSet<>();
            command.addTemps(commandTemps);
            if (!commandTemps.isEmpty()) {
                for (Temp t: commandTemps) {
                    if (!interGraph.containsKey(t.getSerialNumber())) {
                        interGraph.put(t.getSerialNumber(), new HashSet<>());
                    }
                }
            }
        }

        return interGraph;
    }


    private boolean coloringAlgorithm(HashMap<Integer, Set<Integer>> interGraph) {
        HashMap<Integer, Set<Integer>> interCopy = copyGraph(interGraph);
        Stack<Integer> stack = new Stack<>();

        // Simplify - Keep going as long as we have temps to spill
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Integer> toRemove = new ArrayList<>();

            for (int temp : interGraph.keySet()) {
                Set<Integer> neighbors = interGraph.get(temp);
                if (neighbors.size() < K) {
                    toRemove.add(temp);
                }
            }

            if (!toRemove.isEmpty()) {
                changed = true;
                for (int temp : toRemove) {
                    stack.push(temp);
                    Set<Integer> neighbors = interGraph.get(temp);
                    for (int neighbor : neighbors) {
                        if (interGraph.containsKey(neighbor)) {
                            interGraph.get(neighbor).remove(temp);
                        }
                    }
                    interGraph.remove(temp);
                    System.out.println("DEBUG: Simplify - Pushed t" + temp + " to stack");
                }
            }
        }

        // We were only allowed to simplify, so if we don't have an empty graph by now, we fail
        if (!interGraph.isEmpty()) {
            System.out.println("DEBUG: Coloring failed, graph not empty after simplification");
            return false;
        }

        // Now we have a stack of temps to color, and we need to assign them colors (val between 0-9)
        // We add the <serial_number, color> to tempToVal - we don't overwrite it - every temp has a unique serial number
        while (!stack.isEmpty()) {
            int temp = stack.pop();
            Set<Integer> neighbors = interCopy.get(temp);
            Set<Integer> usedColors = new HashSet<>(); // colors already used by neighbors

            if (neighbors != null) {
                for (int neighbor : neighbors) {
                    if (this.tempToVal.containsKey(neighbor)) {
                        usedColors.add(this.tempToVal.get(neighbor));
                    }
                }
            }

            // Now we need to find a color that is not in usedColors
            boolean foundColor = false;
            for (int color = 0; color < K; color++) {
                if (!usedColors.contains(color)) {
                    this.tempToVal.put(temp, color);
                    foundColor = true;
                    System.out.println("DEBUG: Assigned t" + temp + " -> color " + color);
                    break;
                }
            }
            if (!foundColor) {
                System.out.println("DEBUG: No color found for t" + temp);
                return false; // No color found, we failed
            }
        }

        return true;
    }


    private HashMap<Integer, Set<Integer>> copyGraph(HashMap<Integer, Set<Integer>> original) {
        HashMap<Integer, Set<Integer>> copy = new HashMap<>();

        for (Map.Entry<Integer, Set<Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        return copy;
    }


    // We update the out set of each block in the sub-graph
    private void liveness(List<CFGBlock> subGraph) {

        List<CFGBlock> workList = new ArrayList<>(subGraph);
        // Add all parents of the demi-block tail to the worklist (backward analysis!)
        // demi-block is the first in the subGraph list
        System.out.println("DEBUG: Liveness analysis starting with workList size " + workList.size());

        while (!workList.isEmpty()) {
            CFGBlock block = workList.remove(0);

            boolean outChanged = block.updateOut(); // Update the out set of the block and check if it changed
            // If the out set changed, we need to add all parents of the block to the worklist (backward analysis!)
            if (outChanged) {
                workList.addAll(block.getParents());
                System.out.println("DEBUG: OUT changed in block " + block.getBody().get(0));
            }
        }
    }


    /** Builds sub-graphs per function for liveness analysis + register allocation. */
    private List<List<CFGBlock>> constructGraph(List<IrCommand> commands) {
        System.out.println("DEBUG: Creating graph with " + commands.size() + " commands");
        List<List<CFGBlock>> graph = new ArrayList<>();
        List<CFGBlock> blocks = getNodes(commands); // now we have a list of blocks, each with a single command
        Map<String, Integer> labelToIndex = findLabelsFromBlocks(blocks);

        // Construct the global sub-graph
        boolean global = true;
        List<CFGBlock> subGraph = constructSubGraph(0, blocks, global, labelToIndex);
        graph.add(subGraph); // add the subgraph to the graph

        // Then, construct the functions' sub-graphs
        global = false;
        for (int i = 0; i < blocks.size(); i++) {
            CFGBlock block = blocks.get(i);
            IrCommand command = block.getBody().get(0);
            command.addTemps(this.temps);
            if (command instanceof IrCommandFuncDec) {
                // will return the sub graph of the function
                subGraph = constructSubGraph(i, blocks, global, labelToIndex);
                graph.add(subGraph); // add the subgraph to the graph
                System.out.println("DEBUG: Function subgraph added with " + subGraph.size() + " blocks");
            }
        }

        System.out.println("ended cfg construction");
        return graph;
    }


    // Every sub graph is representing a function (or the global scope)
    private List<CFGBlock> constructSubGraph(int startIndex, List<CFGBlock> blocks, boolean global, Map<String, Integer> labelToIndex) {
        List<CFGBlock> subGraph = new ArrayList<>();
        // create a demi-block to be the tail of the graph - so we can start the live analysis from it
        CFGBlock demi = new CFGBlock();
        subGraph.add(demi); // add the demi-block to the subgraph

        // If we are not in global scope, we want to automatically add the function name to the subgraph
        int i = startIndex;
        if (!global && i < blocks.size() - 1) { // don't want it to be the last block
            CFGBlock block = blocks.get(i);
            System.out.println("here: " + block.getBody().get(0));
            block.addChild(blocks.get(i+1));
            blocks.get(i+1).addParent(block);
            subGraph.add(block);
            i++;
        }

        for (; i < blocks.size(); i++) {
            CFGBlock block = blocks.get(i);
            IrCommand command = block.getBody().get(0);

            // We probably break earlier in the prev command, but just in case
            if (command instanceof IrCommandFuncDec) {
                break;
            }
            if (command instanceof IrCommandReturn) {
                subGraph.add(block);
                block.addChild(demi);
                demi.addParent(block);
                continue;
            }
            if (command instanceof IrCommandJump && !((IrCommandJump) command).ignoreCFG) {
                IrCommandJump jumpCommand = (IrCommandJump) command;
                String jumpToLabel = jumpCommand.labelName;
                int jumpLabelIndex = labelToIndex.get(jumpToLabel);
                CFGBlock jumpToBlock = blocks.get(jumpLabelIndex);
                block.addChild(jumpToBlock);
                jumpToBlock.addParent(block);
            }
            if (!(command instanceof IrCommandJumpLabel || // not while loop, backward edge
                i == blocks.size() - 1)) { // Last node
                if (blocks.get(i+1).getBody().get(0) instanceof IrCommandFuncDec) {
                    break; // end of the function
                }
                block.addChild(blocks.get(i+1));
                blocks.get(i+1).addParent(block);
            }

            subGraph.add(block);
        }

        System.out.println("DEBUG: Subgraph created with " + subGraph.size() + " CFGblocks");
        for (CFGBlock block: subGraph){
            if (block.getBody() != null) {
                System.out.println("command: " + block.getBody().get(0));
            }
            else {
                System.out.println("demi block. its parents: ");
                List<CFGBlock> parents = block.getParents();
                for (CFGBlock parent: parents){
                    System.out.println(parent.getBody().get(0));
                }
            }
        }
        return subGraph;
    }


    // ==================== Shared Utility Methods ====================

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

    /** Maps all declared labels to their index, using blocks list */
    private static Map<String, Integer> findLabelsFromBlocks(List<CFGBlock> blocks) {
        Map<String, Integer> labelIndices = new HashMap<>();
        for (int i = 0; i < blocks.size(); i++) {
            IrCommand currentCommand = blocks.get(i).getBody().get(0);
            if (currentCommand instanceof IrCommandLabel) {
                IrCommandLabel curr = (IrCommandLabel) currentCommand;
                labelIndices.put(curr.labelName, i);
            }
        }
        return labelIndices;
    }
}
