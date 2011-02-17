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
	int jobSize, jobBegin;
	
	public Scheduler(MemoryManager mgr)
	{
		this.mgr = mgr;
		jobSize = -1;
		jobBegin = -1;
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
			p.base_Register = 0; // modify for m-scheduler later
			comp.jobID = jobId;
			for (int i=0; i<jobSize; i++)
			{
				mgr.WriteInstruction(i,disk[jobBegin+i]);
			}
			for (int i=0; i<jobSize; i++)
			{
				comp.cache[i] = mgr.ReadInstruction(i);
			}
		}
	}
	
}
