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
	 * @param num - number of pages to be loaded
	 * @return
	 */
	public static boolean DataSwap(MemoryManager mgr, CPU pc, int num)
	{
		//Dispatcher.threadMessage("DataSwap " + direction);
		
		for (int i=0; i<num; i++)
		{
			if (pc.p.pages[i] != -1)
			{
				//Dispatcher.threadMessage("Loading page " + i);
				if (pc.p.p.pTable[pc.p.pages[i]][0] == 1)
					pc.cache[i] = mgr.ReadFrame(pc.p.pages[i]).clone();
			}
			else
				break;
		}
		return true;
	}
	
}
