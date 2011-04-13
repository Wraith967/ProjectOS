/**
 * 
 */

/**
 * @author Ben
 * Created: 2/10/2011
 * Last Edit: 3/8/2011
 */
public class Scheduler {
	
	MemoryManager mgr;
	int jobBegin, i, j;
	char[][][] disk;
	PCB[] p;
	int[] rq;
	int size;
	
	public Scheduler(MemoryManager mgr, char[][][] disk, PCB[] p, int[] rq)
	{
		this.mgr = mgr;
		jobBegin = -1;
		i = -1;
		j = -1;
		this.disk = disk;
		this.p = p;
		this.rq = rq;
	}
	
	public void LoadMulti()
	{
		for (i=0; i<rq.length; i++)
			rq[i] = p[i].jobID;
		mergeSort();
//		for (i=0; i<rq.length; i++)
//			System.out.println(rq[i]);
		j=0;
		for (i=0; i<30; i++)
		{
			p[i].base_Register=j;
			mgr.WriteFrame(j, disk[p[i].beginIndex]);
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].beginIndex+1]);
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].beginIndex+2]);
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].beginIndex+3]);
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].inputPage]);
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
			mgr.WriteFrame(j, disk[p[i].outputPage]);
			p[i].p.pTable[j][0] = 1;
			p[i].p.pTable[j++][1] = 0;
		}
		p[0].p.PrintTable();
//		for (i=0; i<j; i++)
//			PrintMem(mgr.ReadFrame(i));
	}
	
	private void PrintMem(char[][] frame)
	{
		for (int i=0; i<4; i++)
			System.out.println(frame[i]);
		System.out.println();
	}
	
	/**
     * Mergesort algorithm.
     * @param a an array of Comparable items.
     */
    private void mergeSort() {
        int [] tmpArray = new int[ rq.length ];
        mergeSort( tmpArray, 0, rq.length - 1 );
    }
    
    /**
     * Internal method that makes recursive calls.
     * @param a an array of Comparable items.
     * @param tmpArray an array to place the merged result.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    private void mergeSort( int [ ] tmpArray,
            int left, int right ) {
        if( left < right ) {
            int center = ( left + right ) / 2;
            mergeSort( tmpArray, left, center );
            mergeSort( tmpArray, center + 1, right );
            merge( tmpArray, left, center + 1, right );
        }
    }
    
    /**
     * Internal method that merges two sorted halves of a subarray.
     * @param a an array of Comparable items.
     * @param tmpArray an array to place the merged result.
     * @param leftPos the left-most index of the subarray.
     * @param rightPos the index of the start of the second half.
     * @param rightEnd the right-most index of the subarray.
     */
    private void merge( int [ ] tmpArray,
            int leftPos, int rightPos, int rightEnd ) {
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
        
        // Main loop
        while( leftPos <= leftEnd && rightPos <= rightEnd )
            if( p[rq[leftPos]-1].codeSize < p[rq[rightPos]-1].codeSize  )
        	//if( p[rq[leftPos]-1].priority > p[rq[rightPos]-1].priority  )
                tmpArray[ tmpPos++ ] = rq[ leftPos++ ];
            else
                tmpArray[ tmpPos++ ] = rq[ rightPos++ ];
        
        while( leftPos <= leftEnd )    // Copy rest of first half
            tmpArray[ tmpPos++ ] = rq[ leftPos++ ];
        
        while( rightPos <= rightEnd )  // Copy rest of right half
            tmpArray[ tmpPos++ ] = rq[ rightPos++ ];
        
        // Copy tmpArray back
        for( int i = 0; i < numElements; i++, rightEnd-- )
            rq[ rightEnd ] = tmpArray[ rightEnd ];
    }
}
