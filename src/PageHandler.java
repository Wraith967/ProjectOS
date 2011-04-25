/**
 * 
 */

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
	
	public void UnLoadFrames(PCB p)
	{
		Dispatcher.threadMessage("Unloading job");
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
	
	public boolean LoadInstPage(PCB p)
	{
		Dispatcher.threadMessage("Pages Remaining = " + pT.numPagesRemain);
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
				Dispatcher.threadMessage("ptr found at " + ptr);
				mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr]);
				p.pages[ptr] = pT.tblPtr++;
				pT.numPagesRemain--;
			}
			return true;
		}
		else
			return false;
	}
	
	public boolean LoadInputPage(PCB p)
	{
		Dispatcher.threadMessage("Loading Input");
		if (pT.numPagesRemain !=0)
		{
			for (int i=0; i<5; i++)
			{
				if (p.pages[p.numPages+i] == -1)
				{
					ptr = i;
					break;
				}
			}
			if (ptr > p.numPages)
			{
				mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr+p.numPages]);
				p.pages[ptr] = pT.tblPtr++;
				pT.numPagesRemain--;
			}
			return true;
		}
		else
			return false;
	}
	
	public boolean LoadOutputPage(PCB p)
	{
		Dispatcher.threadMessage("Loading Output");
		if (pT.numPagesRemain !=0)
		{
			for (int i=0; i<4; i++)
			{
				if (p.pages[p.numPages+5+i] == -1)
				{
					ptr = i;
					break;
				}
			}
			if (ptr > p.numPages+5)
			{
				mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr+p.numPages+5]);
				p.pages[ptr] = pT.tblPtr++;
				pT.numPagesRemain--;
			}
			return true;
		}
		else
			return false;
	}
		
}
