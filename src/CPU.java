/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class CPU implements Runnable{
	
	char[][] cache; // Interior CPU cache
	int PC; // Program counter
	int jobID, jobSize; // ID and code length for jobs
	static int cpuNum; // Total number of CPUs created
	int cpuID; // Unique ID for each CPU
	int[] registerBank; // holds all registers for CPU
	Decode dec; // decodes instructions
	Execute exe; // performs instructions
	char[] inst; // array for each instruction
	MemoryManager mgr; // for access to memory
	int[] decodeInst; // array for decoded instruction
	int totalSize; // for debug purposes
	int alpha, omega; // begin/end indices for memory usage
	int[] changeIndex; // addresses of changes to memory
	int numChange; // amount of changes
	
	public CPU(MemoryManager mgr)
	{
		cache = new char[72][8]; // 72 is maximum total length 
		PC = -1;
		jobID = -1;
		jobSize = -1;
		registerBank = new int[16];
		dec = new Decode();
		exe = new Execute(this, mgr);
		inst = new char[8];
		this.mgr = mgr;
		cpuID = cpuNum++;
		decodeInst = new int[5];
	}
	
	public void run()
	{
		numChange = 0;
		changeIndex = new int[24];
		//Dispatcher.threadMessage("working");
		while (PC < jobSize)
		{
			Fetch();
			decodeInst = dec.DecodeInst(inst);
			exe.ExecInst(decodeInst);
		}
		//System.out.println();
	}
	
	public void Fetch()
	{
		inst = cache[PC++];
		//System.out.println(inst);
	}
	
}
