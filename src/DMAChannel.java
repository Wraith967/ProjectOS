/**
 * 
 */

/**
 * @author Ben
 * Created: 3/8/2011
 * Last Edit: 3/8/2011
 */
public class DMAChannel {

	MemoryManager mgr;
	
	public DMAChannel(MemoryManager mgr)
	{
		this.mgr = mgr;
	}
	
	public char[] Read(int n, int m)
	{
		return mgr.ReadInstruction(EffectiveAddress.DirectAddress(n,m)).clone();
	}
	
	public void Write(int n, int m, char[] c)
	{
		mgr.WriteInstruction(EffectiveAddress.DirectAddress(n, m), c);
	}
	
}
