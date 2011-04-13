/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class CPU implements Runnable{
	
	char[][][] cache; // Interior CPU cache
	static int cpuNum; // Total number of CPUs created
	int cpuID; // Unique ID for each CPU
	Decode dec; // decodes instructions
	Execute exe; // performs instructions
	char[] inst; // array for each instruction
	MemoryManager mgr; // for access to memory
	int[] decodeInst; // array for decoded instruction
	int alpha, omega; // begin/end indices for memory usage
	PCB p;
	Thread t;
	
	public CPU(MemoryManager mgr, DMAChannel dm)
	{
		cache = new char[18][4][8]; // 72 is maximum total length 
		dec = new Decode();
		exe = new Execute(this, mgr, dm);
		inst = new char[8];
		this.mgr = mgr;
		cpuID = cpuNum++;
		decodeInst = new int[5];
		
	}
	
	public void go()
	{
		t = new Thread(this);
		t.start();
	}
	
	public void run()
	{
		p.runStart = System.nanoTime();
		Dispatcher.threadMessage("working");
		while (true)
		{
			if (t.isInterrupted())
				break;
			inst = cache[p.FC][p.PC];
			decodeInst = dec.DecodeInst(inst);
			exe.ExecInst(decodeInst);
		}
		p.runEnd = System.nanoTime();
	}	
}
