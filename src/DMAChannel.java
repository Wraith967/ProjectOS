/**
 * 
 */

/**
 * @author Ben
 * Created: 3/8/2011
 * Last Edit: 3/8/2011
 */
public class DMAChannel implements Runnable{

	Thread t;
	int address, offset;
	BlockingQueue read, write, rdy;
	int power, sum;
	char[] inst;
	MemoryManager mgr;
	PageHandler PH;
	
	public DMAChannel(BlockingQueue r, BlockingQueue w, BlockingQueue rdy, MemoryManager m, PageHandler p)
	{
		address = 0;
		read = r;
		write = w;
		this.rdy = rdy;
		mgr = m;
		PH = p;
	}
	
	public void Read() throws InterruptedException
	{
		PCB temp = read.pop();
		Dispatcher.threadMessage("Read called for PCB: " + temp.jobID + " at FC: " + temp.FC + " at PC: " + temp.PC);
		sum = 0;
		//Dispatcher.threadMessage("Reading with numPages = " + temp.numPages + " and ioFrame = " + temp.ioFrame);
		if (temp.pages[temp.numPages+temp.ioFrame] != -1)
		{
			inst = mgr.ReadFrame(temp.pages[temp.numPages+temp.ioFrame])[temp.ioOffset];
			for (int i=0; i<8; i++)
			{
				power = 1;
				for (int j=0; j<7-i; j++)
				{
					power *=16;
				}
				sum += (HexToInt.convertHextoInt(inst[i]))*power;
			}
			//Dispatcher.threadMessage("sum = " + sum + " for register " + temp.readInst[2]);
			temp.registerBank[temp.readInst[2]] = sum;
			rdy.push(temp);
			//rdy.sort();
		}
		else
		{
			PH.LoadInputPage(temp);
		}
	}
	
	public void Write() throws InterruptedException
	{
		PCB temp = write.pop();
		if (temp.pages[temp.numPages+temp.ioFrame] != -1)
		{
			Dispatcher.threadMessage("Write called for PCB: " + temp.jobID + " at FC: " + temp.FC + " at PC: " + temp.PC);
			char [][] tempF = mgr.ReadFrame(temp.pages[temp.numPages+temp.ioFrame]);
//			for (int i=0; i<4; i++)
//			{
//				for (int j=0; j<8; j++)
//				{
//					System.out.print(tempF[i][j]);
//				}
//				System.out.println();
//			}
			tempF[temp.ioOffset] = temp.writeInst;
//			for (int i=0; i<4; i++)
//			{
//				for (int j=0; j<8; j++)
//				{
//					System.out.print(tempF[i][j]);
//				}
//				System.out.println();
//			}
			mgr.WriteFrame(temp.pages[temp.numPages+temp.ioFrame], tempF);
			temp.p.pTable[temp.pages[temp.numPages+temp.ioFrame]][1] = 1;
			rdy.push(temp);
			//rdy.sort();
		}
		else
		{
			PH.LoadOutputPage(temp);
		}			
	}

	public void go()
	{
		t = new Thread(this);
		t.start();
	}
	
	public void kill()
	{
		t.interrupt();
	}
	
	@Override
	public void run() {
		Dispatcher.threadMessage("DMAChannel starting up");
		while (true)
		{
			if (t.isInterrupted())
			{
				Dispatcher.threadMessage("DMAChannel shutting down");
				break;
			}
			if (!read.isEmpty())
				try {
					Read();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (!write.isEmpty())
				try {
					Write();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
}
