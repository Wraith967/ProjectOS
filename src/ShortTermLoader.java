/**
 * 
 */

/**
 * @author Ben
 * Created 3/8/2011
 * Last Edit 3/8/2011
 */
public class ShortTermLoader {

	/**
	 * 
	 * @param mgr - Memory module
	 * @param pc - CPU accessing RAM
	 * @param direction - 0 for RAM to CPU, non-zero for CPU to RAM
	 * @return
	 */
	public static boolean DataSwap(MemoryManager mgr, CPU pc, int num)
	{
		//Dispatcher.threadMessage("DataSwap " + direction);
		
		// TODO Modify DataSwap for frames
		
		for (int i=0; i<num; i++)
		{
		if (pc.p.pages[i] != -1)
			{
				if (pc.p.p.pTable[pc.p.pages[i]][0] == 1)
					pc.cache[i] = mgr.ReadFrame(pc.p.base_Register + i).clone();
			}
			else
				break;
		}
		return true;
	}
	
}
