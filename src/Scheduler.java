/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 2/17/2011
 */
public class Scheduler {
	/** TODO Create this class
	 * I made this just so I can use it in OSDriver
	 */
	MemoryManager mgr;
	int jobSize, jobBegin, buffer[], i;
	
	public Scheduler(MemoryManager mgr)
	{
		this.mgr = mgr;
		jobSize = -1;
		jobBegin = -1;
		buffer = new int[3];
		i = -1;
	}
	
	/**
	 * 
	 * @param jobId -- number of job from PCB
	 * @param comp -- which computer is running job
	 */
	public void LoadJob(int jobId, CPU comp, PCB p, char[][] disk)
	{
		jobSize = p.codeSize;
		jobBegin = p.beginIndex;
		if ((jobSize == -1) || (jobBegin == -1))
		{
			System.out.println("Invalid PCB");
		}
		else
		{
			buffer[0] = p.inputBuffer;
			buffer[1] = p.outputBuffer;
			buffer[2] = p.tempBuffer;
			p.base_Register = 0; // modify for m-scheduler later
			comp.jobID = jobId;
			for (i=0; i<jobSize; i++)
			{
				mgr.WriteInstruction(i,disk[jobBegin+i]);
			}
			for (i=0; i<3; i++)
			{
				
			}
			/*for (i=0; i<jobSize; i++)
			{
				comp.cache[i] = mgr.ReadInstruction(i);
			}*/
		}
	}
	
}
