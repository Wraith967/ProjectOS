import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 2/10/2011
 */
public class Loader {
	
	BufferedReader inputStream; // input buffer
	String line; // input from buffer
	int[] jobData; // parsed from input
	String[] tokens; // used for non-instructions
	int x;
	public Loader()
	{
		jobData = new int[3];
		x = 0;
	}
	
	/**
	 * 
	 * @param arr -- disk memory for instructions
	 * @param text -- name of data file
	 * @throws IOException
	 */
	public void runLoad(char[][] arr, String text) throws IOException
	{
		inputStream = new BufferedReader(new FileReader(text));
		while ((line = inputStream.readLine()) != null)
		{
			if (line.charAt(0) == '0') // checks for instruction
			{
				for (int i=0; i<8; i++)
					arr[x][i] = line.charAt(i+2); // ignores "0x"
				System.out.print(arr[x++]);
			}
			else
			{
				tokens = line.split(" ");
				if (tokens[1].equals("JOB") || (tokens[1].equals("Data"))) // if JOB, pulls out job data
				{
					for (int i=0; i<3; i++)
					{
						// TODO move data to PCB 
						jobData[i] = Integer.parseInt(tokens[i+2],16);
						System.out.print(jobData[i] + " ");
					}
				}
			}
			System.out.println();
		}
	}
	
}
