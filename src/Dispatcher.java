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
		for (int i=0; i<1; i++)
		{
			LoadData(c[i], rq.pop(), 6);
		}
		
		i = 0;
		while (!read.isEmpty() || !write.isEmpty() || !rq.isEmpty())
		{
			while (!rq.isEmpty())
			{
				if (!c[i].t.isAlive())
				{
					if (c[i].p.finished)
					{
						MemoryDump.MemDump(sch.disk, mgr, c[i].p, PH);
						PCB temp = rq.pop();
						LoadData(c[i], temp, temp.numPages);
					}
					else 
					{
						c[i].p.ComputeTime();
						if (c[i].p.running)
						{
							PH.LoadInstPage(c[i].p);
							ShortTermLoader.DataSwap(mgr, c[i], c[i].p.numPages);
							c[i].go();
						}
						else if (c[i].p.reading)
						{
							ContextSwitch.SwitchOut(c[i],c[i].p);
							
							read.push(c[i].p);
							PCB temp = rq.pop();
							LoadData(c[i], temp, temp.numPages);
						}
						else if (c[i].p.writing)
						{
							ContextSwitch.SwitchOut(c[i],c[i].p);
							write.push(c[i].p);
							PCB temp = rq.pop();
							LoadData(c[i], temp, temp.numPages);
						}
						else
						{
							PCB temp = rq.pop();
							LoadData(c[i], temp, temp.numPages);
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
		comp.p = null;
		comp.p = p;
		ContextSwitch.SwitchIn(comp,p);
		p.running = true;
		ShortTermLoader.DataSwap(mgr, comp, num);
		comp.go();		
	}
}
