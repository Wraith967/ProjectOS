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
	/**
	 * jobBegin = beginning index of a job in disk
	 * i,j = indices
	 * dest = used to find actual address for memory
	 */
	int jobBegin, i, j, k, dest;
	char[][] disk;
	PCB[] p;
	int[] rq;
	
	public Scheduler(MemoryManager mgr, char[][] disk, PCB[] p, int[] rq)
	{
		this.mgr = mgr;
		jobBegin = -1;
		i = -1;
		j = -1;
		k = -1;
		dest = -1;
		this.disk = disk;
		this.p = p;
		this.rq = rq;
	}
	
	/**
	 * 
	 * @param rq
	 * @param p
	 * @param disk
	 */
	public void LoadMulti(int run)
	{
		dest = 0;
		if (run == 0) k = 0;
		else k = 15;
		for (i=0; i<rq.length; i++)
		{
			jobBegin = p[k].beginIndex;
			if (jobBegin == -1)
			{
				System.out.println("Invalid PCB");
			}
			else
			{
				rq[i] = p[k].jobID;
				p[k].base_Register = dest; // modify for m-scheduler later
				for (int j=0; j<p[k].totalSize; j++)
				{
					mgr.WriteInstruction(dest+j,disk[jobBegin+j]);
				}
				dest += p[k].totalSize;
				p[k++].readyStart = System.nanoTime();
			}
		}
		mergeSort(rq);
//		for (i=0; i<rq.length; i++)
//			System.out.println(rq[i]);
	}
	
	/**
     * Mergesort algorithm.
     * @param a an array of Comparable items.
     */
    private void mergeSort( int [] a ) {
        int [] tmpArray = new int[ a.length ];
        mergeSort( a, tmpArray, 0, a.length - 1 );
    }
    
    /**
     * Internal method that makes recursive calls.
     * @param a an array of Comparable items.
     * @param tmpArray an array to place the merged result.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    private void mergeSort( int [ ] a, int [ ] tmpArray,
            int left, int right ) {
        if( left < right ) {
            int center = ( left + right ) / 2;
            mergeSort( a, tmpArray, left, center );
            mergeSort( a, tmpArray, center + 1, right );
            merge( a, tmpArray, left, center + 1, right );
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
    private void merge( int [ ] a, int [ ] tmpArray,
            int leftPos, int rightPos, int rightEnd ) {
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
        
        // Main loop
        while( leftPos <= leftEnd && rightPos <= rightEnd )
            if( p[a[leftPos]-1].codeSize < p[a[rightPos]-1].codeSize  )
           // if( p[a[leftPos]-1].priority < p[a[rightPos]-1].priority  )
                tmpArray[ tmpPos++ ] = a[ leftPos++ ];
            else
                tmpArray[ tmpPos++ ] = a[ rightPos++ ];
        
        while( leftPos <= leftEnd )    // Copy rest of first half
            tmpArray[ tmpPos++ ] = a[ leftPos++ ];
        
        while( rightPos <= rightEnd )  // Copy rest of right half
            tmpArray[ tmpPos++ ] = a[ rightPos++ ];
        
        // Copy tmpArray back
        for( int i = 0; i < numElements; i++, rightEnd-- )
            a[ rightEnd ] = tmpArray[ rightEnd ];
    }
}
