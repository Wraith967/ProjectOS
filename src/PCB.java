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
	
	double waitTime; // Length of time job has waited on disk
	double runTime; // Length of time job was on CPU
	
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

}
