package ir;

public abstract class IrCommand {
	protected static int labelCounter = 0; /* Label Factory */
	private static int index_counter = 0;
	public int index;

	public IrCommand() { index = index_counter++; }


	public static String getFreshLabel(String msg)
	{
		return String.format("Label_%d_%s", labelCounter++, msg);
	}
}
