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
	char[][][] tempCache;
	static int cpuNum; // Total number of CPUs created
	int cpuID; // Unique ID for each CPU
	Decode dec; // decodes instructions
	Execute exe; // performs instructions
	char[] inst; // array for each instruction
	MemoryManager mgr; // for access to memory
	int[] decodeInst; // array for decoded instruction
	//int alpha, omega; // begin/end indices for memory usage
	PCB p;
	Thread t;
	PageHandler PH;
	
	public CPU(MemoryManager mgr, BlockingQueue read, BlockingQueue write, PageHandler p)
	{
		cache = new char[7][4][8]; // 72 is maximum total length 
		tempCache = new char[3][4][8];
		dec = new Decode();
		exe = new Execute(this, mgr, read, write);
		inst = new char[8];
		this.mgr = mgr;
		cpuID = cpuNum++;
		decodeInst = new int[5];
		PH = p;
	}
	
	public void go()
	{
		t = new Thread(this);
		t.start();
	}
	
	public void run()
	{
		//p.runStart = System.nanoTime();
		//Dispatcher.threadMessage("working");
		p.running = true;
		while (true)
		{
			if (t.isInterrupted())
				break;
			if (p.pages[p.FC] == -1)
			{
				synchronized(PH){
					if(!(PH.LoadInstPage(p)))
					{
						p.running = false;
						t.interrupt();
					}
					PH.notify();
				}
				//Dispatcher.threadMessage("Need more pages, FC at " + p.FC);
				//Dispatcher.threadMessage("Interrupt status: " + t.isInterrupted());
			}
			else
			{
				//Dispatcher.threadMessage("Reading instruction from: " + p.FC + " at " + p.PC);
				inst = cache[p.FC][p.PC];
				decodeInst = dec.DecodeInst(inst);
				exe.ExecInst(decodeInst);
			}
		}
		//p.runEnd = System.nanoTime();
	}

}
