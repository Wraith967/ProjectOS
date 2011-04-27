/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class CPU implements Runnable{
	
	char[][][] cache; // Interior CPU cache
	char[][][] tempCache;
	int[] registerBank;
	static int cpuNum; // Total number of CPUs created
	int cpuID; // Unique ID for each CPU
	Decode dec; // decodes instructions
	Execute exe; // performs instructions
	char[] inst; // array for each instruction
	MemoryManager mgr; // for access to memory
	int[] decodeInst; // array for decoded instruction
	PCB p;
	Thread t;
	PageHandler PH;
	
	public CPU(MemoryManager mgr, BlockingQueue read, BlockingQueue write, PageHandler p)
	{
		cache = new char[7][4][8]; // 72 is maximum total length 
		tempCache = new char[3][4][8];
		registerBank = new int[16];
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
		p.runStart = System.nanoTime();
		p.running = true;
		while (true)
		{
			if (p.finished)
			{
				p.running = false;
				break;
			}
			else if ((p.FC < 0) || (p.FC >= p.numPages))
			{
				Dispatcher.threadMessage("Error with job " + p.jobID + " FC out of bounds");
				p.finished = true;
				p.running = false;
				t.interrupt();
				break;
			}
			else if (t.isInterrupted())
			{
				break;
			}
			else if (p.pages[p.FC] != -1)
			{
				inst = cache[p.FC][p.PC];
				decodeInst = dec.DecodeInst(inst);
				exe.ExecInst(decodeInst);
				if (!exe.jumped)
				{
					p.PC++;
					if (p.PC == 4)
					{
						p.PC = 0;
						p.FC++;
					}
				}
			}
			else
			{
				p.running = true;
				t.interrupt();
				
			}
		}
		p.runEnd = System.nanoTime();
	}
}