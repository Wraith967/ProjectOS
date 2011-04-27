/**
 * @author Ben
 * Created: 3/8/2011
 * Last Edit: 3/8/2011
 */
public class DMAChannel implements Runnable{

	Thread t;
	int address, offset;
	BlockingQueue read, write, rdy, block;
	int power, sum;
	char[] inst;
	MemoryManager mgr;
	PageHandler PH;
	
	public DMAChannel(BlockingQueue r, BlockingQueue w, BlockingQueue rdy, BlockingQueue b, MemoryManager m, PageHandler p)
	{
		address = 0;
		read = r;
		write = w;
		this.rdy = rdy;
		block = b;
		mgr = m;
		PH = p;
	}
	
	public void Read() throws InterruptedException
	{
		PCB temp = read.pop();	
		sum = 0;
		if (temp.pages[temp.ioFrame] != -1)
		{
			inst = mgr.ReadFrame(temp.pages[temp.ioFrame])[temp.ioOffset].clone();
			for (int i=0; i<8; i++)
			{
				power = 1;
				for (int j=0; j<7-i; j++)
				{
					power *=16;
				}
				sum += (HexToInt.convertHextoInt(inst[i]))*power;
			}
			temp.registerBank[temp.readInst[2]] = sum;
			synchronized(rdy)
			{
				temp.readyStart = System.nanoTime();
				rdy.push(temp);
				rdy.sort();
				rdy.notify();
			}
		}
		else
		{
			PH.LoadInputPage(temp);
			read.pushFront(temp);
			Read();
		}		
		temp.reading = false;
	}
	
	public void Write() throws InterruptedException
	{
		PCB temp = write.pop();
		if (temp.pages[temp.ioFrame] != -1)
		{
			char [][] tempF = mgr.ReadFrame(temp.pages[temp.ioFrame]).clone();
			tempF[temp.ioOffset] = temp.writeInst.clone();
			mgr.WriteFrame(temp.pages[temp.ioFrame], tempF.clone());
			temp.p.pTable[temp.pages[temp.ioFrame]][1] = 1;
			synchronized(rdy)
			{
				temp.readyStart = System.nanoTime();
				rdy.push(temp);
				rdy.sort();
				rdy.notify();
			}
		}
		else
		{
			PH.LoadOutputPage(temp);
			write.pushFront(temp);
			Write();
		}
		temp.writing = false;
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
		while (true)
		{
			if (t.isInterrupted())
			{
				break;
			}
			if (!read.isEmpty())
				try {
					Read();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			if (!write.isEmpty())
				try {
					Write();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}	
}
