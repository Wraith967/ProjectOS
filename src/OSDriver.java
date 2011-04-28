import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/9/2011
 * Last Edit: 3/13/2011
 */


public class OSDriver {

	public static final int numCPUs = 4;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
		
		//Data
		char[][] disk = new char[2048][8]; // holds all instructions as 8 chars
		PCB[] PCBarr = new PCB[30];
		int[] readyQueue = new int[15];
		long avgRunTime=0, avgReadyTime=0;
		long totalRunTime, runStart, runEnd;
		String input="DataFile2.txt", output="output.txt";
		Scanner scan = new Scanner(System.in);
		
		for (int i=0; i<30; i++)
			PCBarr[i] = new PCB();
		
		//Modules
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		MemoryManager mgr = new MemoryManager(); // handles RAM
		DMAChannel dm = new DMAChannel(); // handles I/O requests
		Scheduler sched = new Scheduler(mgr, disk, PCBarr, readyQueue); // moves job to CPU
		CPU[] comp = new CPU[numCPUs]; // handles processing
		for (int i=0; i<numCPUs; i++)
			comp[i] = new CPU(mgr, dm);
		Dispatcher disp = new Dispatcher(mgr, sched, comp, PCBarr, readyQueue); // moves job data to CPU
		
		//Method Calls
//		System.out.println("Name of input file:");
//		input = scan.nextLine();
//		System.out.println("Name of output file:");
//		output = scan.nextLine();
		runStart = System.nanoTime();
		control.runLoad(disk, input, PCBarr); // reads in datafile
		for (int i=0; i<2; i++)
		{
			sched.LoadMulti(i);
			disp.MultiDispatch();
		}
		runEnd = System.nanoTime();
		CoreDump(PCBarr, disk, output);
		totalRunTime = runEnd - runStart;

		for (int i=0; i<30; i++)
		{
			PCBarr[i].ComputeTime();
			avgRunTime += PCBarr[i].runTime;
			avgReadyTime += PCBarr[i].readyTime;
		}
		
		avgRunTime /= 30;
		avgReadyTime /= 30;
		
		System.out.println("Average time on CPU = " + avgRunTime + "ns");
		System.out.println("Average time in ready queue = " + avgReadyTime + "ns");
		System.out.println("Total system time = " + totalRunTime + "ns");
		
		
		// PC Cache size needed: 72 words		
	}
	
	private static void CoreDump(PCB[] p, char[][] disk, String output) throws IOException
	{
		BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
		int set = 0;
		for (int i=0; i<30; i++)
		{
			outputStream.write("-------------- Instructions for job " + p[i].jobID + ", I/O Count = " + p[i].count);
			outputStream.newLine();
	
			for (int j=0; j<p[i].codeSize; j++)
			{
				outputStream.write(disk[set++]);
				outputStream.newLine();
			}
			outputStream.write("--------------- Data for job " + p[i].jobID);
			outputStream.newLine();
			for (int j=0; j<44; j++)
			{
				outputStream.write(disk[set++]);
				outputStream.newLine();
			}
			outputStream.newLine();
		}
		outputStream.close();
	}
}