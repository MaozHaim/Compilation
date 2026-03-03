package ir;

import temp.Temp;

import java.util.Set;

public abstract class IrCommand {
	protected static int labelCounter = 0; /* Label Factory */
	private static int index_counter = 0;
	public int index;


	public IrCommand() { index = index_counter++; }


	public static String getFreshLabel(String msg) {
		return String.format("Label_%d_%s", labelCounter++, msg);
	}


	@Override
	public String toString() {
		return "PLACEHOLDER: " + super.toString();
	}


	public String toString(String log) {
		return toString(log, "");
	}


	public String toString(String log, String metadata) {
		return String.format("%-32s %-32s", log, metadata);
	}


	// Default implementation
	public void calcOut(Set<Integer> inSet) {}


	// Default implementation
	public void addTemps(Set<Temp> temps) {}


	public abstract void MIPSme();
}
