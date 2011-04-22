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
	
	public DMAChannel(BlockingQueue r, BlockingQueue w, BlockingQueue rdy, MemoryManager m)
	{
		address = 0;
		read = r;
		write = w;
		this.rdy = rdy;
		mgr = m;
	}
	
	public void Read() throws InterruptedException
	{
		PCB temp = read.pop();
		Dispatcher.threadMessage("Read called for PCB: " + temp.jobID + " at FC: " + temp.FC + " at PC: " + temp.PC);
		sum = 0;
		Dispatcher.threadMessage("Reading with numPages = " + temp.numPages + " and ioFrame = " + temp.ioFrame);
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
		temp.registerBank[temp.curInst[2]] = sum;
		temp.PC++;
		if (temp.PC == 4)
		{
			temp.PC = 0;
			temp.FC++;
		}
		rdy.push(temp);
	}
	
	public void Write()
	{
		
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
				Write();
		}
	}
	
}
