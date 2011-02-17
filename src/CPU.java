/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 2/17/2011
 */
public class CPU {
	/** TODO Create this class
	 * I made this just so I can use it in OSDriver
	 */
	
	char[][] cache;
	int PC;
	int jobID;
	char[][] registerBank;
	
	public CPU()
	{
		cache = new char[72][8];
		PC=-1;
		jobID=-1;
		registerBank = new char[16][8];
	}
	
	
	
}
