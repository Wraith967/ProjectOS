/**
 * @author Ben
 *
 */
public class PageHandler {

	PageTable pT;
	int ptr;
	MemoryManager mgr;
	char[][][] disk;
	
	public PageHandler(PageTable pTable, MemoryManager m, char[][][] disk)
	{
		pT = pTable;
		mgr = m;
		this.disk = disk;
		ptr = -1;
	}
	
	public synchronized void UnLoadFrames(PCB p)
	{
		for (int i=0; i<p.numPages+8; i++)
		{
			if (p.pages[i] != -1)
			{
				pT.pTable[p.pages[i]][0] = 0;
				pT.pTable[p.pages[i]][1] = 0;
				pT.numPagesRemain++;
			}
		}
	}

	private synchronized void UpdatePtr()
	{
		synchronized(pT){
			for (int i=0; i<256; i++)
			{
				if (pT.pTable[i][0] == 0)
				{
					pT.tblPtr = i;
					break;
				}
			}
			pT.notify();
		}
	}
	
	public synchronized boolean LoadInstPage(PCB p)
	{
		//Dispatcher.threadMessage("Number of pages remaining " + pT.numPagesRemain);
		synchronized(pT){
			if (pT.numPagesRemain > 0)
			{
				for (int i=0; i<p.numPages; i++)
				{
					if (p.pages[i] == -1)
					{
						ptr = i;
						break;
					}
				}
				if (ptr != -1)
				{
					mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr]);
					pT.pTable[pT.tblPtr][0] = 1;
					p.pages[ptr] = pT.tblPtr;
					UpdatePtr();
					pT.numPagesRemain--;
				}
				pT.notify();
				return true;
			}
			else
			{
				pT.notify();
				return false;
			}
		}
	}
	
	public synchronized boolean LoadInputPage(PCB p)
	{
		//Dispatcher.threadMessage("Pages Remaining = " + pT.numPagesRemain + " for job " + p.jobID);
		if (pT.numPagesRemain !=0)
		{
			for (int i=p.numPages; i<p.numPages+5; i++)
			{
				if (p.pages[i] == -1)
				{
					ptr = i;
					break;
				}
			}
			mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr]);
			pT.pTable[pT.tblPtr][0] = 1;
			p.pages[ptr] = pT.tblPtr;
			UpdatePtr();
			pT.numPagesRemain--;
			return true;
		}
		else
			return false;
	}
	
	public synchronized boolean LoadOutputPage(PCB p)
	{
		//Dispatcher.threadMessage("Pages Remaining = " + pT.numPagesRemain + " for job " + p.jobID);
		if (pT.numPagesRemain !=0)
		{
			for (int i=p.numPages+5; i<p.numPages+9; i++)
			{
				if (p.pages[i] == -1)
				{
					ptr = i;
					break;
				}
			}
			mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr]);
			pT.pTable[pT.tblPtr][0] = 1;
			p.pages[ptr] = pT.tblPtr;
			UpdatePtr();
			pT.numPagesRemain--;
			return true;
		}
		else
			return false;
	}
}