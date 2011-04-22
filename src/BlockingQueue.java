/**
 * 
 */

/**
 * @author Wraith967
 * Created: 4/21/2011
 * Last Edit: 4/21/2011
 */


public class BlockingQueue {
  /**
    It makes logical sense to use a linked list for a FIFO queue,
    although an ArrayList is usually more efficient for a short
    queue (on most VMs).
   */
  private final PCB[] arr = new PCB[30];
  private int last = 0;
  /**
    This method pushes an object onto the end of the queue, and
    then notifies one of the waiting threads.
   */
  public void push(PCB p) {
    synchronized(arr) {
    	//Dispatcher.threadMessage("push called");
      arr[last++] = p;
      arr.notify();
    }
  }
  /**
    The pop operation blocks until either an object is returned
    or the thread is interrupted, in which case it throws an
    InterruptedException.
   */
  public PCB pop() throws InterruptedException {
    synchronized(arr) {
    	//Dispatcher.threadMessage("pop called");
     // Dispatcher.threadMessage("done waiting");
      PCB temp = arr[0];
      last--;
      for (int i=0; i<last; i++)
    	  arr[i] = arr[i+1];
      return temp;
    }
  }
  /** Return the number of elements currently in the queue. */
  public int size() {
    return last;
  }
  
  public boolean isEmpty() {
	  if (last == 0)
		  return true;
	  else
		  return false;
  }
  /**
   * Calls the sorting method on the inner array
   */
  public void sort() {
	  mergeSort();
  }
  
  /**
   * Mergesort algorithm.
   */
  private void mergeSort() {
      PCB [] tmpArray = new PCB[ last ];
      mergeSort( tmpArray, 0, last - 1 );
  }
  
  /**
   * Internal method that makes recursive calls.
   * @param tmpArray an array to place the merged result.
   * @param left the left-most index of the subarray.
   * @param right the right-most index of the subarray.
   */
  private void mergeSort( PCB [ ] tmpArray,
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
   * @param tmpArray an array to place the merged result.
   * @param leftPos the left-most index of the subarray.
   * @param rightPos the index of the start of the second half.
   * @param rightEnd the right-most index of the subarray.
   */
  private void merge( PCB [ ] tmpArray,
          int leftPos, int rightPos, int rightEnd ) {
      int leftEnd = rightPos - 1;
      int tmpPos = leftPos;
      int numElements = rightEnd - leftPos + 1;
      
      // Main loop
      while( leftPos <= leftEnd && rightPos <= rightEnd )
          if( arr[leftPos].codeSize < arr[rightPos].codeSize  )
      	//if( arr[leftPos].priority > arr[rightPos].priority  )
              tmpArray[ tmpPos++ ] = arr[ leftPos++ ];
          else
              tmpArray[ tmpPos++ ] = arr[ rightPos++ ];
      
      while( leftPos <= leftEnd )    // Copy rest of first half
          tmpArray[ tmpPos++ ] = arr[ leftPos++ ];
      
      while( rightPos <= rightEnd )  // Copy rest of right half
          tmpArray[ tmpPos++ ] = arr[ rightPos++ ];
      
      // Copy tmpArray back
      for( int i = 0; i < numElements; i++, rightEnd-- )
          arr[ rightEnd ] = tmpArray[ rightEnd ];
  }
}
