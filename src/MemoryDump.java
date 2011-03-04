/**
 * 
 */

/**
 * @author Ben
 * Created: 3/3/2011
 * Last Edit: 3/3/2011
 */
public class MemoryDump {
	
	public static void MemDump(char[][] disk, MemoryManager mgr, int jobID, PCB p)
	{
		int size = p.codeSize+p.inputBuffer+p.outputBuffer+p.tempBuffer;
		for (int i=0; i<size; i++)
			disk[p.beginIndex+i] = mgr.ReadInstruction(i);
	}

}
