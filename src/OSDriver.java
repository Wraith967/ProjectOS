import java.io.IOException;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/9/2011
 * Last Edit: 2/10/2011
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
		
		//Modules
		Loader control = new Loader(); // handles reading in and parsing of all instructions
		Scheduler sched = new Scheduler(); // determines Ready Queue
		CPU comp = new CPU(); // handles processing
		Dispatcher disp = new Dispatcher(); // moves jobs from RQ to CPU
		
		//Method Calls
		control.runLoad(disk, "DataFile1.txt"); // reads in datafile
		
		// TODO Add remaining driver code, need PCB to run Long Term Scheduler
		
		
		// PC Cache size needed: 72 words		
	}
}