/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class Dispatcher {
	
	MemoryManager mgr;
	Scheduler sch;
	int pcb; // index for PCBs
	
	public Dispatcher(MemoryManager mgr, Scheduler sch)
	{
		this.mgr = mgr;
		this.sch = sch;
	}
	
	/**
	 * 
	 * @param c
	 * @param p
	 * @param rq
	 */
	public void MultiDispatch(CPU[] c, PCB[] p, int[] rq)
	{
		for (int i=0; i<4; i++)
		{
			LoadData(c[i], p[pcb++], rq[i]);
		}
		// TODO Add watch code on CPUs
	}
	
	/**
	 * 
	 * @param comp
	 * @param p
	 * @param index
	 */
	private void LoadData(CPU comp, PCB p, int index)
	{
		comp.PC = 0;
		for (int i=0; i<16; i++)
			comp.registerBank[i]=p.registerBank[i];
		comp.jobSize = p.codeSize;
		comp.totalSize = p.totalSize;
		comp.alpha = index;
		comp.omega = comp.alpha + comp.totalSize;
		ShortTermLoader.DataSwap(mgr, comp, 0);
		// TODO Load remaining data from PCB to CPU
	}
	
}
