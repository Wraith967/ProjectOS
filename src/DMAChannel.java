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
	
	public DMAChannel()
	{
		address = 0;
	}
	
	public char[] Read(int n, int m, CPU pc)
	{
		//System.out.println("Read from CPU " + pc.cpuID);
		address = EffectiveAddress.DirectAddress(n,m);
		offset = address % 4;
		address = address / 4;
		if (pc.p.codeSize%4 != 0)
			address = address - (pc.p.codeSize/4) + 1;
		else
			address = address - (pc.p.codeSize/4);
		return pc.inputCache[address][offset].clone();
	}
	
	public void Write(int n, int m, char[] c, CPU pc)
	{
		//System.out.println("Write from CPU " + pc.cpuID);
		address = EffectiveAddress.DirectAddress(n,m);
		offset = address % 4;
		address = address / 4;
		address = address - (pc.p.totalSize-24)/4;
		pc.outputCache[address][offset] = c;
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
		}
	}
	
}
