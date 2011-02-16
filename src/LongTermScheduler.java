/**
 * 
 */

/**
 * @author Ben
 * Created: 2/15/2011
 * Last Edit: 2/15/2011
 */
public class LongTermScheduler {
	
	int diskIndex;
	
	
	/**
	 * 
	 * @param disk -- hard drive space
	 * @param memory -- RAM space
	 * @param arr -- PCB block
	 * @param run -- Only 0 or 1, based upon current run
	 */
	public void LoadMemory(char[][] disk, char[][] memory, PCB[] arr, int run)
	{
		for (int i = run*15; i < (run+1)*15; i++)
		{
			for (int j = arr[i].beginIndex; j < (arr[i].beginIndex + arr[i].codeSize); j++)
			{
				//System.out.println(disk[j]);
				memory[diskIndex++] = disk[j];
				//System.out.println(memory[diskIndex++]);
			}
			
		}
	}

}
