/**
 * 
 */

/**
 * @author Ben
 * Created: 2/15/2011
 * Last Edit: 2/23/2011
 */
public class PCB {
	
	int PC; // Program counter
	int FC; // Frame counter
	int cpuID; // ID of CPU running a certain job
	int jobID; // ID of a certain job
	int codeSize; // Number of instructions in a job
	int priority; // Priority of job
	int inputBuffer; // Size of input buffer
	int outputBuffer; // Size of output buffer
	int tempBuffer; // Size of temporary buffer
	int inputPage; // First page of input buffer
	int outputPage; // First page of output buffer
	int beginIndex; // Index of first page in disk
	int base_Register; // Index of first frame in memory
	int totalSize; // Total job space required
	int count; // Number of I/O requests made
	int[] registerBank = new int[16];
	PageTable p;
	int[] pages; // holds addresses of frames in page table
	int numPages; // number of frames for instruction space
	int ioFrame; // address of instructions for I/O requests
	int ioOffset;
	int[] readInst; // current decoded instruction
	char[] writeInst; // output number 
	boolean finished = false;
	boolean running = false;
	
	long runTime; // Length of time job was on CPU
	long readyTime; // Length of time on ready queue
	long runStart;
	long runEnd;
	long readyStart;
	long readyEnd;
	
	// TODO Add remaining PCB attributes
	
	public PCB(PageTable pTable)
	{
		p = pTable;
	}
	
	public String toString()
	{
		String ret = "Job ID: " + jobID;
		ret += " Code Size: " + codeSize;
		ret += " Priority: " + priority;
		ret += " Input Buffer Size: " + inputBuffer;
		ret += " Output Buffer Size: " + outputBuffer;
		ret += " Heap Size: " + tempBuffer;
		ret += " Start Index: " + beginIndex;
		ret += " Num Pages: " + numPages;
		
		return ret;
		
	}
	
	public void ComputeSize()
	{
		totalSize = codeSize + inputBuffer + outputBuffer + tempBuffer;
		for (int i=0; i<16; i++)
			registerBank[i]=0;
		numPages = codeSize/4;
		if (codeSize%4 != 0)
			numPages++;
		pages = new int[numPages+8];
		for (int i=0; i<numPages+8; i++)
			pages[i] = -1;
	}
	
	public void ComputeTime()
	{
		runTime = runEnd - runStart;
		readyTime = readyEnd - readyStart;
	}
}
