/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 2/17/2011
 */
public class MemoryManager {

	char[][] memory; // subset of instructions on "RAM"
	
	public MemoryManager()
	{
		memory = new char[1024][8];
	}
	
	public void WriteInstruction(int address, char[] instruction)
	{
		memory[address]=instruction;
	}
	
	public char[] ReadInstruction(int address)
	{
		return memory[address];
	}
	
}