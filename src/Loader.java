import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 2/15/2011
 */
public class Loader {
	
	BufferedReader inputStream; // input buffer
	String line; // input from buffer
	int[] jobData; // parsed from input
	String[] tokens; // used for non-instructions
	int x, y; // index values: x for disk, y for PCB
	public Loader()
	{
		jobData = new int[3];
		x = 0;
		y = 0;
	}
	
	/**
	 * 
	 * @param arr -- disk memory for instructions
	 * @param text -- name of data file
	 * @throws IOException
	 */
	public void runLoad(char[][] arr, String text, PCB[] pArr) throws IOException
	{
		inputStream = new BufferedReader(new FileReader(text));
		while ((line = inputStream.readLine()) != null)
		{
			if (line.charAt(0) == '0') // checks for instruction
			{
				for (int i=0; i<8; i++)
					arr[x][i] = line.charAt(i+2); // ignores "0x"
				//System.out.print(arr[x++]);
				x++;
			}
			else
			{
				tokens = line.split(" ");
				if (tokens[1].equals("JOB")) // if JOB, pulls out job data
				{
					for (int i=0; i<3; i++)
					{
						jobData[i] = Integer.parseInt(tokens[i+2],16);
					}
					pArr[y].beginIndex = x;
					pArr[y].jobID = jobData[0];
					pArr[y].codeSize = jobData[1];
					pArr[y].priority = jobData[2];
				}
				else if (tokens[1].equals("Data"))
				{
					for (int i=0; i<3; i++)
					{
						jobData[i] = Integer.parseInt(tokens[i+2],16);
					}
					pArr[y].inputBuffer = jobData[0];
					pArr[y].outputBuffer = jobData[1];
					pArr[y].tempBuffer = jobData[2];
				}
				else System.out.println(pArr[y++]);
			}
		}
	}
	
}