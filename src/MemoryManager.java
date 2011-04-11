/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 2/17/2011
 */
public class MemoryManager {

	private char[][][] memory; // subset of instructions on "RAM"
	
	public MemoryManager()
	{
		memory = new char[256][4][8];
	}
	
	public void WriteFrame(int address, char[][] frame)
	{
		memory[address] = frame;
	}
	
	public char[][] ReadFrame(int address)
	{
		return memory[address];
	}
	
//	public void WriteInstruction(int address, char[] instruction)
//	{
//		memory[address]=instruction;
//	}
//	
//	public char[] ReadInstruction(int address)
//	{
//		return memory[address];
//	}
	
}
