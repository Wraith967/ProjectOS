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
		if (direction == 0)
		{
			for (int i=0; i<pc.p.totalSize; i++)
			{
				pc.cache[i] = mgr.ReadInstruction(pc.alpha + i).clone();
			}
		}
		else
		{
			for (int i=0; i<pc.numChange; i++)
			{
				mgr.WriteInstruction(pc.alpha + pc.changeIndex[i], pc.cache[pc.changeIndex[i]].clone());
			}
		}
		return true;
	}
	
}
