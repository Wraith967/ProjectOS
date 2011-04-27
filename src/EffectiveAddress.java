/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 3/3/2011
 */
public class EffectiveAddress {
	
	/**
	 * 
	 * @param offset 16-bit offset
	 * @param base Content of base register
	 * @return
	 */
	public static int DirectAddress(int offset, int base)
	{
		//Dispatcher.threadMessage("offset = " + offset + " base = " + base);
		return (base+offset)/4;
	}
	/**
	 * 
	 * @param offset 16-bit offset
	 * @param base Content of base register
	 * @param index Content of index register
	 * @return
	 */
	public static int IndirectAddress(int offset, int base, int index)
	{
			return (base+index+offset)/4;
	}
	
}