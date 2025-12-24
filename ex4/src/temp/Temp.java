/***********/
/* PACKAGE */
/***********/
package temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class Temp
{
	private int serial=0;
	private static int nextSerialNum = 0;
	
	public Temp(int serial)
	{
		this.serial = serial;
	}

	public Temp() {
		this.serial = nextSerialNum++;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}

	@Override
	public String toString() {
		return "t" + serial;
	}
}
