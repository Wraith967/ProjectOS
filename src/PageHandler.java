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
	
	public synchronized void UnLoadFrames(PCB p)
	{
		//Dispatcher.threadMessage("Unloading job");
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
			//Dispatcher.threadMessage("Updating ptr at " + pT.tblPtr);
			boolean found = false;
			for (int i=pT.tblPtr; i<256; i++)
			{
				//Dispatcher.threadMessage("Checking for ptr at " + i + " with valid bit of " + pT.pTable[i][0]);
				if (pT.pTable[i][0] == 0)
				{
					//Dispatcher.threadMessage("Ptr found at " + i);
					pT.tblPtr = i;
					found = true;
					break;
				}
			}
			if (!found)
			{
				for (int i=0; i<pT.tblPtr; i++)
				{
					if (pT.pTable[pT.tblPtr][0] == 0)
					{
						pT.tblPtr = i;
						break;
					}
				}
			}
			//Dispatcher.threadMessage("ptr updated to: " + pT.tblPtr);
			pT.notify();
		}
	}
	
	public synchronized boolean LoadInstPage(PCB p)
	{
		//Dispatcher.threadMessage("Pages Remaining = " + pT.numPagesRemain + " for job " + p.jobID);
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
					//Dispatcher.threadMessage("ptr found at " + ptr);
					//Dispatcher.threadMessage("tblPtr found at " + pT.tblPtr);
					mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr]);
//					String msg = "";
//					char[][] temp = mgr.ReadFrame(pT.tblPtr);
//					for (int i=0; i<4; i++)
//					{
//						for (int j=0; j<8; j++)
//							msg += temp[i][j];
//						msg += "\n";
//					}
//					Dispatcher.threadMessage("Moving Frame: " + msg);
					pT.pTable[pT.tblPtr][0] = 1;
					//Dispatcher.threadMessage("PageTable at " + (pT.tblPtr) + " = " + pT.pTable[pT.tblPtr][0] + pT.pTable[pT.tblPtr][1]);
					//Dispatcher.threadMessage("PageTable at " + (pT.tblPtr+1) + " = " + pT.pTable[pT.tblPtr+1][0] + pT.pTable[pT.tblPtr+1][1]);
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
			for (int i=0; i<5; i++)
			{
				if (p.pages[p.numPages+i] == -1)
				{
					ptr = i;
					break;
				}
			}
			//Dispatcher.threadMessage("Loading Input for job " + p.jobID + " at ptr " + ptr);
			mgr.WriteFrame(pT.tblPtr, disk[p.beginIndex+ptr+p.numPages]);
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
					//Dispatcher.threadMessage("Output ptr found at " + i);
					ptr = i;
					break;
				}
			}
			//Dispatcher.threadMessage("Loading Output for job " + p.jobID + " at ptr " + ptr);
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