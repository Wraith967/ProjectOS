/**
 * 
 */

/**
 * @author Ben
 *
 */
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class OSDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		// Testing input and conversion methods 
		
		BufferedReader in = null; // input buffer
		String line; // input from file
		int jobID, codeSize, pri; // used for non-instructions, as parsers
		char[][] disk = new char[2048][8]; // holds instructions as 8 chars
		String[] tokens; // used for non-instructions
		int x = 0; // index for disk, explicit declaration at 0 (may not be needed, but safer)
		in = new BufferedReader(new FileReader("DataFile1.txt"));
		while ((line = in.readLine()) != null)
		{
			//System.out.println(b);
			if (line.charAt(0) == '0') // checks for instruction
			{
				for (int i=0;i<8;i++) 
				{
					//c[i]=b[i+2];
					disk[x][i] = line.charAt(i+2); // ignores "0x"
				}
				//System.out.println(c);
				x++;
			}
			else
			{
				tokens = line.split(" ");
				if (tokens[1].equals("JOB")) // if JOB, pulls out job data
				{
					jobID = Integer.parseInt(tokens[2],16);
					codeSize = Integer.parseInt(tokens[3],16);
					pri = Integer.parseInt(tokens[4],16);
					System.out.println("JobID: " + jobID);
					System.out.println("CodeSize: " + codeSize);
					System.out.println("Priority: " + pri);
				}
				else if (tokens[1].equals("Data")) // if Data, pulls out buffer data
				{
					jobID = Integer.parseInt(tokens[2],16);
					codeSize = Integer.parseInt(tokens[3],16);
					pri = Integer.parseInt(tokens[4],16);
					System.out.println("InputBufferSize: " + jobID);
					System.out.println("OutputBufferSize: " + codeSize);
					System.out.println("TempBufferSize: " + pri);
				}			
			}	
		}
		System.out.println(x);
		
		// PC Cache size needed: 72 words		
	}
}

/* Integer equivalents of characters in DataFile:
 * '/' = 47
 * ' ' = 32
 * 'J' = 74
 * 'O' = 79
 * 'B' = 66
 * '1' = 49
 * '7' = 55
 * '2' = 50
 * '0' = 48
 * 'x' = 120
*/
