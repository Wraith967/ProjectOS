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
	Decode dec;
	Execute exe;
	char[] inst;
	
	public CPU()
	{
		cache = new char[72][8];
		PC=-1;
		jobID=-1;
		registerBank = new char[16][8];
		dec = new Decode();
		exe = new Execute();
		inst = new char[8];
	}
	
	public void runJob()
	{
		Fetch();
		//Run dec
		//Run exe
	}
	
	public void Fetch()
	{
		inst = cache[PC++];
	}
	
}
