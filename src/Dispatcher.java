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
	

	public Dispatcher()
	{
		
	}
	
	public void LoadData(int jobID, CPU comp, PCB p)
	{
		comp.PC = 0;
		for (int i=0; i<16; i++)
			comp.registerBank[i]=0;
		comp.jobSize = p.codeSize;
		comp.totalSize = p.totalSize;
		// TODO Load remaining data from PCB to CPU
	}
	
}
