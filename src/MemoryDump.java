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
		int size = p.totalSize;
		if (size%4 !=0)
			size = size%4 + 1;
		else
			size = size%4;
		for (int i=0; i<size; i++)
		{
			disk[p.beginIndex+i] = mgr.ReadFrame(p.pages[i]).clone();
		}
	}
}