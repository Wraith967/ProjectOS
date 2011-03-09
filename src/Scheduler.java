/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class Scheduler {
	
	MemoryManager mgr;
	/**
	 * jobBegin = beginning index of a job in disk
	 * i,j = indices
	 * dest = used to find actual address for memory
	 * index = similar to Program Counter for memory
	 */
	int jobBegin, i, j, dest, index;
	char[][] disk;
	
	public Scheduler(MemoryManager mgr, char[][] disk)
	{
		this.mgr = mgr;
		jobBegin = -1;
		i = -1;
		j = -1;
		dest = -1;
		index = -1;
		this.disk = disk;
	}
	
	/**
	 * 
	 * @param rq
	 * @param p
	 * @param disk
	 */
	public void LoadMulti(int[] rq, PCB[] p)
	{
		index = 0;
		dest = 0;
			for (i=0; i<rq.length; i++)
			{
				jobBegin = p[i].beginIndex;
				if (jobBegin == -1)
				{
					System.out.println("Invalid PCB");
				}
				else
				{
					p[i].base_Register = index; // modify for m-scheduler later
					for (j=0; j<p[i].totalSize; j++)
					{
						mgr.WriteInstruction(dest+j,disk[jobBegin+j]);
					}
					dest += 72;
					index += 72;
				}
			}
	}
	
	/**
	 * 
	 * @param rq
	 * @param p
	 * @param disk
	 * @param index
	 */
	public void LoadSingle(int[] rq, PCB p, int index)
	{
		//Dispatcher.threadMessage("LoadSingle" + index);
		jobBegin = p.beginIndex;
		if (jobBegin == -1)
		{
			System.out.println("Invalid PCB");
		}
		else
		{
			p.base_Register = rq[index];
			for (i=0; i<p.totalSize; i++)
				mgr.WriteInstruction(rq[index] + i, disk[jobBegin+i]);
		}
	}
	
	/**
	 * 
	 * @param jobId -- number of job from PCB
	 * @param comp -- which computer is running job
	 */
	public void LoadJob(CPU comp, PCB p)
	{
		jobBegin = p.beginIndex;
		if (jobBegin == -1)
		{
			System.out.println("Invalid PCB");
		}
		else
		{
			p.base_Register = 0; // modify for m-scheduler later
			p.cpuID = comp.cpuID;
			comp.jobID = p.jobID;
			dest = EffectiveAddress.DirectAddress(jobBegin*4, 0);
			for (i=0; i<p.totalSize; i++)
			{
				mgr.WriteInstruction(dest+i,disk[jobBegin+i]);
			}
		}
	}
	
}
