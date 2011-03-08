import java.io.IOException;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/9/2011
 * Last Edit: 3/8/2011
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
		int[] readyQueue = new int[14];
		//double avgWaitTime=0.0, avgRunTime=0.0;
		
		for (int i=0; i<30; i++)
			PCBarr[i] = new PCB();
		
		//Modules
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		MemoryManager mgr = new MemoryManager(); // handles RAM
		Scheduler sched = new Scheduler(mgr); // moves job to CPU
		Dispatcher disp = new Dispatcher(mgr, sched); // moves job data to CPU
		CPU[] comp = new CPU[4]; // handles processing
		for (int i=0; i<4; i++)
			comp[i] = new CPU(mgr);
		
		//Method Calls
		control.runLoad(disk, "DataFile2.txt", PCBarr); // reads in datafile
		sched.LoadMulti(readyQueue, PCBarr, disk);
		disp.MultiDispatch(comp, PCBarr, readyQueue);
		/*for (int i=0; i<30; i++)
		{
			sched.LoadJob(comp[0], PCBarr[i], disk);
			disp.LoadData(comp[0], PCBarr[i]);
			comp[0].runJob();
			MemoryDump.MemDump(disk, mgr, i, PCBarr[i]);
			System.out.println();
		}*/
		/*for (int i=0; i<30; i++)
		{
			for (int j=0; j<PCBarr[i].totalSize; j++)
				System.out.println(disk[PCBarr[i].beginIndex+j]);
			System.out.println();
		}*/
		
		/*for (int i=0; i<30; i++)
		{
			avgWaitTime += PCBarr[i].waitTime;
			avgRunTime += PCBarr[i].runTime;
		}
		avgWaitTime /= 30;
		avgRunTime /= 30;
		*/
		
		// TODO Add remaining driver code
		
		
		// PC Cache size needed: 72 words		
	}
}