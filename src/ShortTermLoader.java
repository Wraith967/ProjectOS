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
	public static boolean DataSwap(MemoryManager mgr, CPU pc, int direction, int num)
	{
		//Dispatcher.threadMessage("DataSwap " + direction);
		
		// TODO Modify DataSwap for frames
		
		if (direction == 0)
		{
			for (int i=0; i<num; i++)
			{
				if (pc.p.pages[i] != -1)
					if (pc.p.p.pTable[pc.p.pages[i]][0] == 1)
						pc.cache[i] = mgr.ReadFrame(pc.p.base_Register + i).clone();
			}
			for (int i=0; i<5; i++)
			{
				if (pc.p.pages[pc.p.numPages+i] != -1)
					if (pc.p.p.pTable[pc.p.pages[pc.p.numPages+i]][0] == 1)
						pc.inputCache[i] = mgr.ReadFrame(pc.p.pages[pc.p.numPages+i]);
			}
			for (int i=0; i<3; i++)
			{
				if (pc.p.pages[pc.p.numPages+5+i] != -1)
					if (pc.p.p.pTable[pc.p.pages[pc.p.numPages+5+i]][0] == 1)
						pc.inputCache[i] = mgr.ReadFrame(pc.p.pages[pc.p.numPages+5+i]);
			}
		}
		else
		{
			for (int i=0; i<pc.p.numChange; i++)
			{
				mgr.WriteFrame(pc.p.base_Register + pc.p.changeIndex[i], pc.outputCache[pc.p.changeIndex[i]].clone());
			}
		}
		return true;
	}
	
}
