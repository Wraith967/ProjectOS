/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class Scheduler {
	
	MemoryManager mgr;
	int jobBegin, i, j;
	char[][][] disk;
	PCB[] p;
	BlockingQueue rq;
	int size;
	
	public Scheduler(MemoryManager mgr, char[][][] disk, PCB[] p, BlockingQueue rq)
	{
		this.mgr = mgr;
		jobBegin = -1;
		i = -1;
		j = -1;
		this.disk = disk;
		this.p = p;
		this.rq = rq;
	}
	
	public void LoadMulti()
	{
		for (i=0; i<30; i++)
			rq.push(p[i]);
		rq.sort();
//		for (i=0; i<rq.length; i++)
//			System.out.println(rq[i]);
		j=0;
		for (i=0; i<30; i++)
		{
			p[i].base_Register=j;
			mgr.WriteFrame(j, disk[p[i].beginIndex]);
			p[i].pages[0] = j;
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].beginIndex+1]);
			p[i].pages[1 ]= j;
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].beginIndex+2]);
			p[i].pages[2] = j;
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].beginIndex+3]);
			p[i].pages[3] = j;
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].inputPage]);
			p[i].pages[p[i].numPages] = j;
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].outputPage]);
			p[i].pages[p[i].numPages+5] = j;
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
//			System.out.println(p[i].pages[p[i].numPages]);
//			System.out.println(p[i].pages[p[i].numPages+5]);
		}
		
//		p[0].p.PrintTable();
//		for (i=0; i<j; i++)
//			PrintMem(mgr.ReadFrame(i));
	}
	
	private void PrintMem(char[][] frame)
	{
		for (int i=0; i<4; i++)
			System.out.println(frame[i]);
		System.out.println();
	}
	
}
