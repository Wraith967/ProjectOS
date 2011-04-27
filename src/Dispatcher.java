/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class Dispatcher {
	
	/**
	 * This method is for debugging purposes
	 * @param message
	 */
	static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
	
	MemoryManager mgr;
	Scheduler sch;
	boolean isDone; // checks for completion of application
	boolean CPUDone;
	int i;
	CPU[] c;
	PCB[] p;
	BlockingQueue rq, read, write, block;
	
	public Dispatcher(MemoryManager mgr, Scheduler sch, CPU[] c, PCB[] p, BlockingQueue rq, BlockingQueue r, BlockingQueue w, BlockingQueue b)
	{
		this.mgr = mgr;
		this.sch = sch;
		isDone = false;
		CPUDone = false;
		this.c = c;
		this.p = p;
		this.rq = rq;
		read = r;
		write = w;
		block = b;
	}
	
	public void MultiDispatch(PageHandler PH) throws InterruptedException
	{
		isDone = false;
		CPUDone = false;
		for (int i=0; i<1; i++)
		{
			LoadData(c[i], rq.pop(), 6);
			c[i].go();
		}
		
		i = 0;
		while (!read.isEmpty() || !write.isEmpty() || !rq.isEmpty())
		{
			//threadMessage("Queues not empty");
			while (!rq.isEmpty())
			{
				//threadMessage("Ready Queue not empty");
				if (!c[i].t.isAlive())
				{
					//threadMessage("Running? " + c[i].p.running);
					//threadMessage("Finished? " + c[i].p.finished);
					if (c[i].p.running)
					{
						//threadMessage("Attempting to load new pages");
						if(!(PH.LoadInstPage(c[i].p)))
						{
							//threadMessage("No pages left");
							block.push(c[i].p);
							//rq.print();
							//threadMessage("Current size of ready queue " + rq.size());
							PCB temp = rq.pop();
							LoadData(c[i], temp, temp.numPages);
							c[i].go();
						}
						else
						{
							//threadMessage("New page loaded, restarting");
							ShortTermLoader.DataSwap(mgr, c[i], c[i].p.numPages);
							c[i].go();
						}
					}
					else
					{
						if (c[i].p.reading)
						{
							c[i].p.registerBank = c[i].registerBank.clone();
							read.push(c[i].p);
							PCB temp = rq.pop();
							//threadMessage("Loading job " + temp.jobID);
							LoadData(c[i], temp, temp.numPages);
							c[i].go();
						}
						else if (c[i].p.writing)
						{
							c[i].p.registerBank = c[i].registerBank.clone();
							write.push(c[i].p);
							PCB temp = rq.pop();
							//threadMessage("Loading job " + temp.jobID);
							LoadData(c[i], temp, temp.numPages);
							c[i].go();
						}
						else if (c[i].p.finished)
						{
							//threadMessage("Job " + c[i].p.jobID + " finished");
							//c[i].p.runEnd = System.nanoTime();
							MemoryDump.MemDump(sch.disk, mgr, c[i].p, PH);
							if(!block.isEmpty())
								rq.push(block.pop());
							//rq.print();
							//threadMessage("Current size of ready queue " + rq.size());
							PCB temp = rq.pop();
							//threadMessage("Loading job " + temp.jobID);
							LoadData(c[i], temp, temp.numPages);
							c[i].go();
						}
						else
						{
							//c[i].p.runEnd = System.nanoTime();
							//rq.push(c[i].p);
							//rq.print();
							//threadMessage("Current size of ready queue " + rq.size());
							PCB temp = rq.pop();
							LoadData(c[i], temp, temp.numPages);
							c[i].go();
						}
					}
					
				}
//				i++;
//				i %= 4;
			}
		}
		for (i=0; i<1; i++)
		{
			while (!CPUDone)
			{
				if (!c[i].t.isAlive())
				{
					//threadMessage("CPU " + i + " is done");
					MemoryDump.MemDump(sch.disk, mgr, c[i].p, PH);
					CPUDone = true;
				}
			}
			CPUDone = false;
		}
	}
	
	/**
	 * 
	 * @param comp
	 * @param p
	 * @param index
	 */
	private void LoadData(CPU comp, PCB p, int num) throws InterruptedException
	{
		p.readyEnd = System.nanoTime();
		//threadMessage("Loading job " + p.jobID + " on CPU " + comp.cpuID);
		comp.p = null;
		comp.p = p;
		comp.registerBank = p.registerBank.clone();
		p.running = true;
//		comp.alpha = p.base_Register;
//		comp.omega = comp.alpha + p.totalSize;
		ShortTermLoader.DataSwap(mgr, comp, num);
		//threadMessage("Starting CPU " + i);
		//comp.go();
		
	}
}
