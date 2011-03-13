import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/9/2011
 * Last Edit: 3/13/2011
 */


public class OSDriver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		
		//Data
		char[][] disk = new char[2048][8]; // holds all instructions as 8 chars
		PCB[] PCBarr = new PCB[30];
		long avgWaitTime=0, avgRunTime=0;
		long totalRunTime, runStart, runEnd;
		
		for (int i=0; i<30; i++)
			PCBarr[i] = new PCB();
		
		//Modules
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		MemoryManager mgr = new MemoryManager(); // handles RAM
		Scheduler sched = new Scheduler(mgr); // moves job to CPU
		Dispatcher disp = new Dispatcher(); // moves job data to CPU
		CPU comp = new CPU(mgr); // handles processing
		
		//Method Calls
		runStart = System.nanoTime();
		control.runLoad(disk, "DataFile2.txt", PCBarr); // reads in datafile
		for (int i=0; i<30; i++)
		{
			sched.LoadJob(i, comp, PCBarr[i], disk);
			disp.LoadData(i, comp, PCBarr[i]);
			comp.runJob();
			PCBarr[i].runEnd = System.nanoTime();
			MemoryDump.MemDump(disk, mgr, i, PCBarr[i]);
			//System.out.println();
		}
		runEnd = System.nanoTime();
		totalRunTime = runEnd - runStart;
		CoreDump(PCBarr, disk);
		
		for (int i=0; i<30; i++)
		{
			PCBarr[i].ComputeTime();
			avgWaitTime += PCBarr[i].waitTime;
			avgRunTime += PCBarr[i].runTime;
		}
		avgWaitTime /= 30;
		avgRunTime /= 30;
		
		System.out.println("Average time on disk = " + avgWaitTime + "ns");
		System.out.println("Average time on CPU = " + avgRunTime + "ns");
		System.out.println("Total system time = " + totalRunTime + "ns");
		
		// TODO Add remaining driver code
		
		
		// PC Cache size needed: 72 words		
	}
	
	private static void CoreDump(PCB[] p, char[][] disk) throws IOException
	{
	BufferedWriter outputStream = new BufferedWriter(new FileWriter("outputPartI.txt"));
	for (int i=0; i<30; i++)
	for (int j=0; j<p[i].totalSize; j++)
	{
	outputStream.write(disk[p[i].beginIndex+j]);
	outputStream.newLine();
	}
	outputStream.close();
	}
}