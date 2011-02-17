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
		Scheduler sched = new Scheduler(); // determines Ready Queue
		CPU comp = new CPU(); // handles processing
		Dispatcher disp = new Dispatcher(); // moves jobs from RQ to CPU
		MemoryManager mgr = new MemoryManager(); // handles RAM
		
		//Method Calls
		control.runLoad(disk, "DataFile2.txt", PCBarr); // reads in datafile
		
		
		// TODO Add remaining driver code
		
		
		// PC Cache size needed: 72 words		
	}
}