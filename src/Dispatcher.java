/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class Dispatcher {
	
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
	int doneIndex; // index of finished CPU
	boolean jobDone; // checks for CPU finished with job
	
	public Dispatcher(MemoryManager mgr, Scheduler sch)
	{
		this.mgr = mgr;
		this.sch = sch;
		t = new Thread[4];
		isDone = false;
		jobDone = false;
		pcb = 0;
		readyIndex = 0;
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
			LoadData(c[i], p[pcb++], rq[readyIndex++], i);
		}
		// TODO Add watch code on CPUs
		while (!isDone)
		{
			while (!jobDone)
			{
				for (int i=0; i<4; i++)
				{
					if (c[i].done)
					{
						jobDone = true;
						doneIndex = i;
					}
				}
			}
			ShortTermLoader.DataSwap(mgr, c[doneIndex], 1);
			MemoryDump.MemDump(sch.disk, mgr, pcb, p[pcb]);
			System.out.println();
			sch.LoadSingle(rq, p[pcb], (readyIndex));
			jobDone = false;
			LoadData(c[doneIndex], p[pcb++], rq[readyIndex++], doneIndex);
			readyIndex %= 14;
			if (pcb == 30)
				isDone = true;
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
		comp.PC = 0;
		for (int j=0; j<16; j++)
			comp.registerBank[j]=p.registerBank[j];
		comp.jobSize = p.codeSize;
		comp.totalSize = p.totalSize;
		comp.alpha = index;
		comp.omega = comp.alpha + comp.totalSize;
		ShortTermLoader.DataSwap(mgr, comp, 0);
		t[i] = new Thread(comp);
        t[i].start();
		// TODO Load remaining data from PCB to CPU
	}
	
}
