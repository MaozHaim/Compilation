package ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Ir {
	private List<IrCommand> commands;


	protected Ir() {
		commands = new ArrayList<>();
	}


	public List<IrCommand> getCommands() {
		return commands;
	}


	public void AddIrCommand(IrCommand cmd) { commands.add(cmd); }


	/*** Singleton Implementation ***/
	private static Ir instance = null;
	public static Ir getInstance() {
		if (instance == null) {
			instance = new Ir();
		}
		return instance;
	}
}

/**
 * IRcommand <-> IRcommand <-> IRcommand <-> ... <-> IRcommand
 * We have a list of IRcommands
 * Each label is associated with its own IRcommand
 * Each jump is also associated with its own IRcommand
 *
 * Do we really need an index?
 * For each label (string), keep a pointer to its associated IRcommand.
 * Each jump is literally just an IR command.
 *
 *
 * int i = 0;
 * while (i <= size) {
 *     IRcommand = list.get(i)
 *     if (IRcommand instanceof Label)
 * }
 */
