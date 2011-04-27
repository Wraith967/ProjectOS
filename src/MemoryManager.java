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
	
	public synchronized void WriteFrame(int address, char[][] frame)
	{
		synchronized(memory){
			memory[address] = frame.clone();
			memory.notify();
		}
	}
	
	public synchronized char[][] ReadFrame(int address)
	{
		char [][] temp;
		synchronized(memory){
			temp = memory[address].clone();
			memory.notify();
		}
		return temp;
	}
}