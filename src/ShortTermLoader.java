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
	public static boolean DataSwap(MemoryManager mgr, CPU pc, int direction)
	{
		//Dispatcher.threadMessage("DataSwap " + direction);
		
		// TODO Modify DataSwap for frames
		
		if (direction == 0)
		{
			for (int i=0; i<6; i++)
			{
				pc.cache[i] = mgr.ReadFrame(pc.alpha + i).clone();
			}
		}
		else
		{
			for (int i=0; i<pc.p.numChange; i++)
			{
				mgr.WriteFrame(pc.alpha + pc.p.changeIndex[i], pc.cache[pc.p.changeIndex[i]].clone());
			}
		}
		return true;
	}
	
}
