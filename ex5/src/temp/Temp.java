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

	public void assignRegister(int val){
		if (val < 0 || val > 9)
			throw new RuntimeException("yeah we don't have a $t" + val + " register that is just not a thing");

		serial = val;
	}
}
