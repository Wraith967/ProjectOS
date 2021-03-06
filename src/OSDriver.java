import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
		char[][][] disk = new char[512][4][8]; // holds all instructions as 8 chars
		PageTable PTable = new PageTable();
		PCB[] PCBarr = new PCB[30];
		BlockingQueue readyQueue = new BlockingQueue(false, "ready");
		BlockingQueue readQueue = new BlockingQueue(false, "read");
		BlockingQueue writeQueue = new BlockingQueue(false, "write");
		BlockingQueue blockQueue = new BlockingQueue(false, "block");
		long avgRunTime=0, avgReadyTime=0;
		long totalRunTime, runStart, runEnd;
		String input, output;
		Scanner scan = new Scanner(System.in);
		
		for (int i=0; i<30; i++)
			PCBarr[i] = new PCB(PTable);
		
		//Modules
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		MemoryManager mgr = new MemoryManager(); // handles RAM
		PageHandler PH = new PageHandler(PTable, mgr, disk);
		DMAChannel dm = new DMAChannel(readQueue, writeQueue, readyQueue, blockQueue, mgr, PH); // handles I/O requests
		Scheduler sched = new Scheduler(mgr, disk, PCBarr, readyQueue); // moves job to CPU
		CPU[] comp = new CPU[4]; // handles processing
		for (int i=0; i<4; i++)
			comp[i] = new CPU(mgr, readQueue, writeQueue, PH);
		Dispatcher disp = new Dispatcher(mgr, sched, comp, PCBarr, readyQueue, readQueue, writeQueue); // moves job data to CPU
		
		//Method Calls
		System.out.println("Name of input file:");
		input = scan.nextLine();
		System.out.println("Name of output file:");
		output = scan.nextLine();
		runStart = System.nanoTime();
		dm.go();
		int endIndex = control.runLoad(disk, input, PCBarr); // reads in datafile
		if (endIndex == -1)
		{
			System.out.println("System failure, shutting down");
		}
		else
		{
			sched.LoadMulti(PTable);
			disp.MultiDispatch(PH);
		}
		dm.kill();
		runEnd = System.nanoTime();
		CoreDump(PCBarr, disk, output, endIndex);
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
	}
	
	private static void CoreDump(PCB[] p, char[][][] disk, String output, int end) throws IOException
	{
		BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
		int page = 0, set = 0;
		for (int i=0; i<30; i++)
		{
			outputStream.write("-------------- Instructions for job " + p[i].jobID + ", I/O Count = " + p[i].count);
			outputStream.newLine();
			
			for (int j=0; j<p[i].codeSize; j++)
			{
				outputStream.write(disk[page][set]);
				outputStream.newLine();
				set++;
				if (set == 4)
				{
					set = 0;
					page++;
				}
			}
			if (p[i].codeSize%4 != 0)
			{
				set = 0;
				page++;
			}
			outputStream.write("--------------- Data for job " + p[i].jobID);
			outputStream.newLine();
			for (int j=0; j<44; j++)
			{
				outputStream.write(disk[page][set]);
				outputStream.newLine();
				set++;
				if (set == 4)
				{
					set = 0;
					page++;
				}
			}
			outputStream.newLine();
		}
	}
}