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
	int pcb; // index for PCBs
	int readyIndex; // index for ready queue
	Thread[] t; // runs CPUs as threads
	boolean isDone; // checks for completion of application
	boolean CPUDone;
	int doneIndex; // index of finished CPU
	int i;
	
	public Dispatcher(MemoryManager mgr, Scheduler sch)
	{
		this.mgr = mgr;
		this.sch = sch;
		t = new Thread[4];
		isDone = false;
		pcb = -1;
		readyIndex = 0;
		CPUDone = false;
	}
	
	/**
	 * 
	 * @param c
	 * @param p
	 * @param rq
	 */
	public void MultiDispatch(CPU[] c, PCB[] p, int[] rq) throws InterruptedException
	{
		for (int i=0; i<4; i++)
		{
			LoadData(c[i], p[++pcb], rq[readyIndex++], i);
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
				//System.out.println();
				if (pcb < 29)
				{
					sch.LoadSingle(rq, p[++pcb], (readyIndex));
					LoadData(c[doneIndex], p[pcb], rq[readyIndex++], doneIndex);
					readyIndex %= 14;
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
	private void LoadData(CPU comp, PCB p, int index, int i) throws InterruptedException
	{
		p.readyEnd = System.nanoTime();
		comp.PC = 0;
		for (int j=0; j<16; j++)
			comp.registerBank[j]=p.registerBank[j];
		comp.p = p;
		comp.alpha = index;
		comp.omega = comp.alpha + p.totalSize;
		ShortTermLoader.DataSwap(mgr, comp, 0);
		t[i] = new Thread(comp);
		//threadMessage("Starting CPU " + i);
        t[i].start();
	}
	
}
