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


	public void MIPSme(){
		for (IrCommand command : commands) {
			command.MIPSme();
		}
	}
}
