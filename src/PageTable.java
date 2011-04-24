/**
 * 
 */

/**
 * @author Ben
 * Created: 4/8/2011
 * Last Edit: 4/8/2011
 */
public class PageTable {

	int [][] pTable;
	int numPagesRemain;
	int tblPtr;
	
	
	public PageTable()
	{
		pTable = new int[256][2]; //One index for each frame in memory, with a valid/invalid bit, a modified bit
		numPagesRemain = 256;
		tblPtr = 0;
	}
	
	public void PrintTable()
	{
		for (int i=0; i<256; i++)
		{
			System.out.println("Page " + i + " is valid: " + pTable[i][0] + " has been modified: " + pTable[i][1]);
		}
	}
	
}
