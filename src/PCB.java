/**
 * 
 */

/**
 * @author Ben
 * Created: 2/15/2011
 * Last Edit: 2/15/2011
 */
public class PCB {
	
	int jobID;
	int codeSize;
	int priority;
	int inputBuffer;
	int outputBuffer;
	int tempBuffer;
	int beginIndex;
	int base_Register;
	
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
