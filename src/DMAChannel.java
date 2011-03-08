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
	CPU pc;
	
	public DMAChannel(MemoryManager mgr, CPU pc)
	{
		this.mgr = mgr;
		this.pc = pc;
	}
	
	public char[] Read(int n, int m)
	{
		return pc.cache[EffectiveAddress.DirectAddress(n,m)].clone();
	}
	
	public void Write(int n, int m, char[] c)
	{
		pc.cache[EffectiveAddress.DirectAddress(n, m)] = c;
	}
	
}
