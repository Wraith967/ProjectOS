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
	static int cpuNum; // Total number of CPUs created
	int cpuID; // Unique ID for each CPU
	int[] registerBank; // holds all registers for CPU
	Decode dec; // decodes instructions
	Execute exe; // performs instructions
	char[] inst; // array for each instruction
	MemoryManager mgr; // for access to memory
	int[] decodeInst; // array for decoded instruction
	int alpha, omega; // begin/end indices for memory usage
	int[] changeIndex; // addresses of changes to memory
	int numChange; // amount of changes
	PCB p;
	
	public CPU(MemoryManager mgr, DMAChannel dm)
	{
		cache = new char[72][8]; // 72 is maximum total length 
		PC = -1;
		registerBank = new int[16];
		dec = new Decode();
		exe = new Execute(this, mgr, dm);
		inst = new char[8];
		this.mgr = mgr;
		cpuID = cpuNum++;
		decodeInst = new int[5];
	}
	
	public void run()
	{
		exe.count = 0;
		p.runStart = System.nanoTime();
		numChange = 0;
		changeIndex = new int[24];
		//Dispatcher.threadMessage("working");
		while (PC < p.codeSize)
		{
			inst = cache[PC++];
			decodeInst = dec.DecodeInst(inst);
			exe.ExecInst(decodeInst);
		}
		p.runEnd = System.nanoTime();
		p.IOcount = exe.count;
	}	
}
