/**
 * 
 */

/**
 * @author Ben
 * Created: 2/15/2011
 * Last Edit: 2/23/2011
 */
public class PCB {
	
	int cpuID; // ID of CPU running a certain job
	int jobID; // ID of a certain job
	int codeSize; // Number of instructions in a job
	int priority; // Priority of job
	int inputBuffer; // Size of input buffer
	int outputBuffer; // Size of output buffer
	int tempBuffer; // Size of temporary buffer
	int beginIndex; // Index of first instruction in disk
	int base_Register; // Index of first instruction in memory
	int totalSize;
	int[] registerBank = new int[16];
	
	long waitTime; // Length of time job has waited on disk
	long runTime; // Length of time job was on CPU
	long readyTime; // Length of time on ready queue
	long diskStart;
	long diskEnd;
	long runStart;
	long runEnd;
	long readyStart;
	long readyEnd;
	
	// TODO Add remaining PCB attributes
	
	public String toString()
	{
		String ret = "Job ID: " + jobID;
		ret += " Code Size: " + codeSize;
		ret += " Priority: " + priority;
		ret += " Input Buffer Size: " + inputBuffer;
		ret += " Output Buffer Size: " + outputBuffer;
		ret += " Heap Size: " + tempBuffer;
		ret += " Start Index: " + beginIndex;
		
		return ret;
		
	}
	
	public void ComputeSize()
	{
		totalSize = codeSize + inputBuffer + outputBuffer + tempBuffer;
		for (int i=0; i<16; i++)
			registerBank[i]=0;
	}
	
	public void ComputeTime()
	{
		waitTime = diskEnd - diskStart;
		runTime = runEnd - runStart;
		readyTime = readyEnd - readyStart;
	}

}
