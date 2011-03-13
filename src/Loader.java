import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;

/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/13/2011
 */
public class Loader {

	BufferedReader inputStream; // input buffer
	String line; // input from buffer
	int[] jobData; // parsed from input
	String[] tokens; // used for non-instructions
	int x, y; // index values: x for disk, y for PCB
	Pattern pattern1;
	Pattern pattern2;
	Matcher matcher1;
	Matcher matcher2;
	public Loader()
	{
		jobData = new int[3];
		x = 0;
		y = 0;
		pattern1 = Pattern.compile("// JOB [0-9A-F]{1,2} [0-9A-F]{2} [0-9A-F]{1}");
		pattern2 = Pattern.compile("// Data 14 C C");
	}

	/**
	*
	* @param arr -- disk memory for instructions
	* @param text -- name of data file
	* @throws IOException
	*/
	public void runLoad(char[][] arr, String text, PCB[] pArr) throws IOException
	{
		//System.out.println(pattern1.toString());
		//System.out.println(pattern2.toString());
		inputStream = new BufferedReader(new FileReader(text));
		while (y != 30)
		{
			line = inputStream.readLine();
			if (line.charAt(0) == '0') // checks for instruction
			{
				for (int i=0; i<8; i++)
					arr[x][i] = line.charAt(i+2); // ignores "0x"
				//System.out.print(arr[x++]);
				x++;
			}
			else
			{
				matcher1 = pattern1.matcher(line);
				matcher2 = pattern2.matcher(line);
				tokens = line.split(" "); // Modify for regex, maybe
				if (matcher1.find())
				{
					//System.out.println("Match pattern 1");
					for (int i=0; i<3; i++)
					{
						jobData[i] = Integer.parseInt(tokens[i+2],16);
					}
					pArr[y].beginIndex = x;
					pArr[y].jobID = jobData[0];
					pArr[y].codeSize = jobData[1];
					pArr[y].priority = jobData[2];
				}
				else if (matcher2.find())
				{
					//System.out.println("Match pattern 2");
					for (int i=0; i<3; i++)
					{
							jobData[i] = Integer.parseInt(tokens[i+2],16);
					}
					pArr[y].inputBuffer = jobData[0];
					pArr[y].outputBuffer = jobData[1];
					pArr[y].tempBuffer = jobData[2];
				}
				else
				{
					System.out.println(pArr[y]);
					pArr[y++].ComputeSize();
					//System.out.println(pArr[y++].totalSize);
				}
			}
		}
		inputStream.close();
	}
}