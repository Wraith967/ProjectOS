import java.io.IOException;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/9/2011
 * Last Edit: 2/15/2011
 */


public class OSDriver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		
		//Data
		char[][] disk = new char[2048][8]; // holds all instructions as 8 chars
		char[][] memory = new char[1024][8]; // subset of instructions on "RAM"
		PCB[] PCBarr = new PCB[30];
		for (int i=0; i<30; i++)
			PCBarr[i] = new PCB();
		
		//Modules
		LongTermScheduler LTS = new LongTermScheduler(); // loads jobs into memory
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		Scheduler sched = new Scheduler(); // determines Ready Queue
		CPU comp = new CPU(); // handles processing
		Dispatcher disp = new Dispatcher(); // moves jobs from RQ to CPU
		
		//Method Calls
		control.runLoad(disk, "DataFile2.txt", PCBarr); // reads in datafile
		for (int i=0; i<2; i++)
		{
			LTS.LoadMemory(disk, memory, PCBarr, i);
		}
		
		// TODO Add remaining driver code
		
		
		// PC Cache size needed: 72 words		
	}
}