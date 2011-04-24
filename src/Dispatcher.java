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
	BlockingQueue rq, read, write;
	
	public Dispatcher(MemoryManager mgr, Scheduler sch, CPU[] c, PCB[] p, BlockingQueue rq, BlockingQueue r, BlockingQueue w)
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
	}
	
	public void MultiDispatch(PageHandler PH) throws InterruptedException
	{
		isDone = false;
		CPUDone = false;
		for (int i=0; i<4; i++)
		{
			LoadData(c[i], rq.pop(), 6);
			c[i].go();
		}
		
		i = 0;
		while (!read.isEmpty() || !write.isEmpty() || !rq.isEmpty())
		{
			while (!rq.isEmpty())
			{
				if (!c[i].t.isAlive())
				{
					if (c[i].p.running)
					{
						if(!(PH.LoadInstPage(c[i].p)))
						{
							c[i].p.runEnd = System.nanoTime();
							ShortTermLoader.DataSwap(mgr, c[i], 1, 0);
							PCB temp = rq.pop();
							rq.push(c[i].p);
							LoadData(c[i], temp, temp.numPages);
							c[i].go();
						}
						else
							c[i].go();
					}
					else if (c[i].p.finished)
					{
						threadMessage("Job finished");
						c[i].p.runEnd = System.nanoTime();
						ShortTermLoader.DataSwap(mgr, c[i], 1, 0);
						MemoryDump.MemDump(sch.disk, mgr, c[i].p);
						PCB temp = rq.pop();
						LoadData(c[i], temp, temp.numPages);
						c[i].go();
					}
					else
					{
						c[i].p.runEnd = System.nanoTime();
						ShortTermLoader.DataSwap(mgr, c[i], 1, 0);
						PCB temp = rq.pop();
						LoadData(c[i], temp, temp.numPages);
						c[i].go();
					}
					
				}
				i++;
				i %= 4;
			}
		}
		for (i=0; i<4; i++)
		{
			while (!CPUDone)
			{
				if (!c[i].t.isAlive())
				{
					//threadMessage("CPU " + i + " is done");
					ShortTermLoader.DataSwap(mgr, c[i], 1, 0);
					MemoryDump.MemDump(sch.disk, mgr, c[i].p);
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
		comp.p = p;
		p.running = true;
//		comp.alpha = p.base_Register;
//		comp.omega = comp.alpha + p.totalSize;
		ShortTermLoader.DataSwap(mgr, comp, 0, num);
		//threadMessage("Starting CPU " + i);
		//comp.go();
		
	}
}
