/**
 * 
 */

/**
 * @author Ben
 * Created: 3/3/2011
 * Last Edit: 3/3/2011
 */
public class MemoryDump {
	
	public static void MemDump(char[][] disk, MemoryManager mgr, PCB p)
	{
		for (int i=0; i<p.totalSize; i++)
		{
			disk[p.beginIndex+i] = mgr.ReadInstruction(p.base_Register + i).clone();
			//System.out.println(mgr.ReadInstruction(p.base_Register + i));
		}
	}
	public static void BufferDump(MemoryManager mgr, int jobID, int codeSize, int totalSize)
	{
		for (int i=codeSize; i<totalSize; i++)
			System.out.println(mgr.ReadInstruction(i));
	}

}
