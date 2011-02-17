/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 2/17/2011
 */
public class Dispatcher {
	/** TODO Create this class
	 * I made this just so I can use it in OSDriver
	 */
	char[] zeroInitializer = {'0','0','0','0','0','0','0','0'};

	public Dispatcher()
	{
		
	}
	
	public void LoadData(int jobID, CPU comp, PCB p)
	{
		comp.PC = 0;
		comp.registerBank[0] = zeroInitializer;
		comp.registerBank[1] = zeroInitializer;
		comp.jobSize = p.codeSize;
		// TODO Load remaining data from PCB to CPU
	}
	
}
