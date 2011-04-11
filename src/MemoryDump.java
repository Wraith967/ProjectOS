/**
 * 
 */

/**
 * @author Ben
 * Created: 3/3/2011
 * Last Edit: 3/3/2011
 */
public class MemoryDump {
	
	public static void MemDump(char[][][] disk, MemoryManager mgr, PCB p)
	{
		for (int i=0; i<p.totalSize; i++)
		{
			disk[p.beginIndex+i] = mgr.ReadFrame(p.base_Register + i).clone();
		}
	}
}