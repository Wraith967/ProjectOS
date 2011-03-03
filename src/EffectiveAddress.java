/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 2/17/2011
 */
public class EffectiveAddress {

	public EffectiveAddress()
	{
		
	}
	
	public int DirectAddress(int offset, int base)
	{
		return base+offset;
	}
	
	public int IndirectAddress(int offset, int base, int index)
	{
			return base+index+offset;
	}
	
}
