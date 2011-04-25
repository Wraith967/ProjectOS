/**
 * 
 */

/**
 * @author Ben
 * Created: 3/3/2011
 * Last Edit: 3/3/2011
 */
public class MemoryDump {
	
	public static void MemDump(char[][][] disk, MemoryManager mgr, PCB p, PageHandler PH)
	{
		Dispatcher.threadMessage("Memory Dump called on job " + p.jobID);
		int size = p.totalSize-12;
		if (size%4 !=0)
			size = size/4 + 1;
		else
			size = size/4;
		System.out.println("Dumping at size " + size);
		for (int i=0; i<size; i++)
		{
			if (p.pages[i] != -1)
			{
				System.out.println("Dumping page " + i);
				disk[p.beginIndex+i] = mgr.ReadFrame(p.pages[i]).clone();
				//System.out.println(disk[p.beginIndex+i]);
			}
		}
		PH.UnLoadFrames(p);
	}
}