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
	public static void main(String[] args) throws IOException, InterruptedException{
		
		//Data
		char[][] disk = new char[2048][8]; // holds all instructions as 8 chars
		PCB[] PCBarr = new PCB[30];
		int[] readyQueue = new int[14];
		long avgWaitTime=0, avgRunTime=0, avgReadyTime=0;
		//long totalRunTime, runStart, runEnd;
		
		for (int i=0; i<30; i++)
			PCBarr[i] = new PCB();
		
		//Modules
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		MemoryManager mgr = new MemoryManager(); // handles RAM
		DMAChannel dm = new DMAChannel(); // handles I/O requests
		Scheduler sched = new Scheduler(mgr, disk, PCBarr); // moves job to CPU
		Dispatcher disp = new Dispatcher(mgr, sched); // moves job data to CPU
		CPU[] comp = new CPU[4]; // handles processing
		for (int i=0; i<4; i++)
			comp[i] = new CPU(mgr, dm);
		
		//Method Calls
		//runStart = System.nanoTime();
		control.runLoad(disk, "DataFile2.txt", PCBarr); // reads in datafile
		sched.LoadMulti(readyQueue);
		disp.MultiDispatch(comp, PCBarr, readyQueue);
		//runEnd = System.nanoTime();
		CoreDump(PCBarr, disk);
		//totalRunTime = runEnd - runStart;

		for (int i=0; i<30; i++)
		{
			PCBarr[i].ComputeTime();
			avgWaitTime += PCBarr[i].waitTime;
			avgRunTime += PCBarr[i].runTime;
			avgReadyTime += PCBarr[i].readyTime;
			//System.out.println("Number of I/O for jobID: " + PCBarr[i].jobID + " = " + PCBarr[i].IOcount);
		}
		avgWaitTime /= 30;
		avgRunTime /= 30;
		avgReadyTime /= 30;
		//System.out.println("Average time on disk = " + avgWaitTime + "ns");
		//System.out.println("Average time on CPU = " + avgRunTime + "ns");
		//System.out.println("Average time in ready queue = " + avgReadyTime + "ns");
		//System.out.println("Total system time = " + totalRunTime + "ns");
		
		
		// PC Cache size needed: 72 words		
	}
	
	private static void CoreDump(PCB[] p, char[][] disk) throws IOException
	{
		BufferedWriter outputStream = new BufferedWriter(new FileWriter("outputPartII.txt"));
		for (int i=0; i<30; i++)
			for (int j=0; j<p[i].totalSize; j++)
			{
				outputStream.write(disk[p[i].beginIndex+j]);
				outputStream.newLine();
			}
		outputStream.close();
	}
}