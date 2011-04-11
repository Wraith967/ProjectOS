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
	
	public PageTable()
	{
		pTable = new int[256][2]; //One index for each frame in memory, with a valid/invalid bit and a modified bit
	}
	
}
