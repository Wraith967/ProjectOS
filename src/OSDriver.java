import java.io.IOException;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/9/2011
 * Last Edit: 2/17/2011
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
		for (int i=0; i<30; i++)
			PCBarr[i] = new PCB();
		
		//Modules
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		CPU comp = new CPU(); // handles processing
		MemoryManager mgr = new MemoryManager(); // handles RAM
		Scheduler sched = new Scheduler(mgr); // moves job to CPU
		Dispatcher disp = new Dispatcher(); // moves job data to CPU
		
		//Method Calls
		control.runLoad(disk, "DataFile2.txt", PCBarr); // reads in datafile
		for (int i=0; i<30; i++)
		{
			sched.LoadJob(i, comp, PCBarr[i], disk);
			disp.LoadData(i, comp, PCBarr[i]);
			comp.runJob();
		}
		
		
		// TODO Add remaining driver code
		
		
		// PC Cache size needed: 72 words		
	}
}