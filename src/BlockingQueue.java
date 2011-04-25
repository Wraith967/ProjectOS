import java.util.ArrayList;

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
  private final ArrayList<PCB> arr = new ArrayList<PCB>();
  /**
    This method pushes an object onto the end of the queue, and
    then notifies one of the waiting threads.
   */
  public void push(PCB p) {
    synchronized(arr) {
    	//Dispatcher.threadMessage("push called");
      arr.add(p);
     // Dispatcher.threadMessage("Queue has " + last + " items");
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
    	PCB temp = arr.remove(0);
    	arr.notify();
    	return temp;
    }
  }
  /** Return the number of elements currently in the queue. */
  public int size() {
    return arr.size();
  }
  
  public boolean isEmpty() {
	  return arr.isEmpty();
  }
  /**
   * Calls the sorting method on the inner array
   */
  public void sort() {
	  synchronized(arr) {
	  mergeSort();
	  arr.notify();
	  }
  }
  
  /**
   * Mergesort algorithm.
   */
  private void mergeSort() {
      ArrayList<PCB> tmpArray = new ArrayList<PCB>();
      mergeSort( tmpArray, 0, arr.size() - 1 );
  }
  
  /**
   * Internal method that makes recursive calls.
   * @param tmpArray an array to place the merged result.
   * @param left the left-most index of the subarray.
   * @param right the right-most index of the subarray.
   */
  private void mergeSort( ArrayList<PCB> tmpArray,
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
  private void merge( ArrayList<PCB> tmpArray,
          int leftPos, int rightPos, int rightEnd ) {
      int leftEnd = rightPos - 1;
      int tmpPos = leftPos;
      int numElements = rightEnd - leftPos + 1;
      
      // Main loop
      while( leftPos <= leftEnd && rightPos <= rightEnd )
          if( arr.get(leftPos).codeSize < arr.get(rightPos).codeSize  )
      	//if( arr[leftPos].priority > arr[rightPos].priority  )
              tmpArray.add(tmpPos++,arr.get(leftPos++));
          else
              tmpArray.add(tmpPos++,arr.get(rightPos++));
      
      while( leftPos <= leftEnd )    // Copy rest of first half
          tmpArray.add(tmpPos++, arr.get(leftPos++));
      
      while( rightPos <= rightEnd )  // Copy rest of right half
          tmpArray.add(tmpPos++, arr.get(rightPos++));
      
      // Copy tmpArray back
      for( int i = 0; i < numElements; i++, rightEnd-- )
          arr.set(rightEnd, tmpArray.get(rightEnd));
  }
}
