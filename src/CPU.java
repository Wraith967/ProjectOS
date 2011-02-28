/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 2/28/2011
 */
public class CPU {
	/** TODO Create this class
	 * I made this just so I can use it in OSDriver
	 */
	
	char[][] cache; // Interior CPU cache
	int PC; // Program counter
	int jobID, jobSize; // ID and code length for jobs
	static int cpuNum; // Total number of CPUs created
	int cpuID; // Unique ID for each CPU
	char[][] registerBank; // holds all registers for CPU
	Decode dec; // decodes instructions
	Execute exe; // performs instructions
	char[] inst; // array for each instruction
	char[][] inputBuffer; // holds values of job input buffers
	char[][] outputBuffer; // holds values of job output buffers
	char[][] tempBuffer; // holds values of job temporary buffers
	MemoryManager mgr;
	String decodeInst;
	
	public CPU(MemoryManager mgr)
	{
		cache = new char[28][8]; // 28 is maximum instruction length 
		PC = -1;
		jobID = -1;
		jobSize = -1;
		registerBank = new char[16][8];
		dec = new Decode();
		exe = new Execute(this);
		inst = new char[8];
		this.mgr = mgr;
		cpuID = cpuNum++;
		decodeInst = null;
	}
	
	public void runJob()
	{
		while (PC < jobSize)
		{
			Fetch();
			decodeInst = dec.DecodeInst(inst[0], inst[1]);
			exe.ExecInst(decodeInst, inst);
		}
	}
	
	public void Fetch()
	{
		inst = mgr.ReadInstruction(PC++);
		//System.out.println(inst);
	}
	
}
