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
	int readyIndex; // index for ready queue
	Thread[] t; // runs CPUs as threads
	boolean isDone; // checks for completion of application
	boolean CPUDone;
	int doneIndex; // index of finished CPU
	int i, j;
	CPU[] c;
	PCB[] p;
	int[] rq;
	
	public Dispatcher(MemoryManager mgr, Scheduler sch, CPU[] c, PCB[] p, int[] rq)
	{
		this.mgr = mgr;
		this.sch = sch;
		t = new Thread[4];
		isDone = false;
		readyIndex = 0;
		CPUDone = false;
		this.c = c;
		this.p = p;
		this.rq = rq;
	}
	
	/**
	 * 
	 * @param c
	 * @param p
	 * @param rq
	 */
	public void MultiDispatch() throws InterruptedException
	{
		j=0;
		isDone = false;
		readyIndex = 0;
		CPUDone = false;
		for (int i=0; i<4; i++)
		{
			LoadData(c[i], p[rq[readyIndex++]-1], i);
			j++;
		}
		// TODO Add watch code on CPUs
		i = 0;
		while (!isDone)
		{
			if (!t[i].isAlive())
			{
				//threadMessage("CPU " + i + " is done");
				doneIndex = i;
				c[doneIndex].p.runEnd = System.nanoTime();
				ShortTermLoader.DataSwap(mgr, c[doneIndex], 1);
				MemoryDump.MemDump(sch.disk, mgr, c[doneIndex].p);
				if (j<15)
				{
					LoadData(c[doneIndex], p[rq[readyIndex++]-1], doneIndex);
					j++;
				}
				else
				{
					isDone = true;
				}
			}
			i++;
			i %= 4;
		}
		for (i=0; i<4; i++)
		{
			while (!CPUDone)
			{
				if (!t[i].isAlive())
				{
					//threadMessage("CPU " + i + " is done");
					doneIndex = i;
					ShortTermLoader.DataSwap(mgr, c[doneIndex], 1);
					MemoryDump.MemDump(sch.disk, mgr, c[doneIndex].p);
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
	private void LoadData(CPU comp, PCB p, int i) throws InterruptedException
	{
		p.readyEnd = System.nanoTime();
		comp.PC = 0;
		for (int j=0; j<16; j++)
			comp.registerBank[j]=p.registerBank[j];
		comp.p = p;
		comp.alpha = p.base_Register;
		comp.omega = comp.alpha + p.totalSize;
		ShortTermLoader.DataSwap(mgr, comp, 0);
		t[i] = new Thread(comp);
		//threadMessage("Starting CPU " + i);
        t[i].start();
	}
}
