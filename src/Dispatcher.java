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
	int finished;
	int i;
	CPU[] c;
	PCB[] p;
	BlockingQueue rq, read, write;
	
	public Dispatcher(MemoryManager mgr, Scheduler sch, CPU[] c, PCB[] p, BlockingQueue rq, BlockingQueue r, BlockingQueue w)
	{
		this.mgr = mgr;
		this.sch = sch;
		isDone = false;
		finished = -1;
		this.c = c;
		this.p = p;
		this.rq = rq;
		read = r;
		write = w;
	}
	
	public void MultiDispatch(PageHandler PH) throws InterruptedException
	{
		isDone = false;
		for (int i=0; i<4; i++)
		{
			LoadData(c[i], rq.pop(), 6);
		}
		
		i = 0;
		while (!read.isEmpty() || !write.isEmpty() || !rq.isEmpty())
		{
			if (!c[i].t.isAlive())
			{
				if (c[i].p.finished)
				{
					finished++;
					//threadMessage("Jobs finished " + finished);
					ContextSwitch.SwitchOut(c[i],c[i].p);
					MemoryDump.MemDump(sch.disk, mgr, c[i].p, PH);
					if (!rq.isEmpty())
					{
						PCB temp = rq.pop();
						LoadData(c[i], temp, temp.numPages);
					}
				}
				else 
				{
					c[i].p.ComputeTime();
					if (c[i].p.running)
					{
						//threadMessage("Job running, loading more pages");
						ContextSwitch.SwitchOut(c[i],c[i].p);
						PH.LoadInstPage(c[i].p);
						LoadData(c[i],c[i].p, c[i].p.numPages);
					}
					else if (c[i].p.reading || c[i].p.writing)
					{
						ContextSwitch.SwitchOut(c[i],c[i].p);
						if (c[i].p.reading)
							read.push(c[i].p);
						else if (c[i].p.writing)
							write.push(c[i].p);
						if (!rq.isEmpty())
						{
							//threadMessage("Loading new job");
							PCB temp = rq.pop();
							LoadData(c[i], temp, temp.numPages);
						}
					}
				}
			}
			i++;
			i %= 4;
		}
		for (i=0; i<4; i++)
		{
			while (c[i].t.isAlive());
			ContextSwitch.SwitchOut(c[i], c[i].p);
			if (c[i].p.finished)
			{
				finished++;
				//threadMessage("Jobs finished " + finished);
				MemoryDump.MemDump(sch.disk, mgr, c[i].p, PH);
			}
		}
		i=0;
		while (finished < 30)
		{
			if (!c[i].t.isAlive())
			{
				if (c[i].p.finished)
				{
					finished++;
					//threadMessage("Jobs finished " + finished);
					ContextSwitch.SwitchOut(c[i],c[i].p);
					MemoryDump.MemDump(sch.disk, mgr, c[i].p, PH);
					if (!rq.isEmpty())
					{
						PCB temp = rq.pop();
						LoadData(c[i], temp, temp.numPages);
					}
				}
				else 
				{
					c[i].p.ComputeTime();
					if (c[i].p.running)
					{
						//threadMessage("Job running, loading more pages");
						ContextSwitch.SwitchOut(c[i],c[i].p);
						PH.LoadInstPage(c[i].p);
						LoadData(c[i],c[i].p, c[i].p.numPages);
					}
					else if (c[i].p.reading || c[i].p.writing)
					{
						ContextSwitch.SwitchOut(c[i],c[i].p);
						if (c[i].p.reading)
							read.push(c[i].p);
						else if (c[i].p.writing)
							write.push(c[i].p);
						if (!rq.isEmpty())
						{
							//threadMessage("Loading new job");
							PCB temp = rq.pop();
							LoadData(c[i], temp, temp.numPages);
						}
					}
				}
			}
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