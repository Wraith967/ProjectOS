/**
 * 
 */

/**
 * @author Ben
 * Created: 3/8/2011
 * Last Edit: 3/8/2011
 */
public class DMAChannel {

	
	public DMAChannel()
	{
	}
	
	public char[] Read(int n, int m, CPU pc)
	{
		//System.out.println("Read from CPU " + pc.cpuID);
		return pc.cache[0][EffectiveAddress.DirectAddress(n,m)].clone();
	}
	
	public void Write(int n, int m, char[] c, CPU pc)
	{
		//System.out.println("Write from CPU " + pc.cpuID);
		pc.cache[0][EffectiveAddress.DirectAddress(n, m)] = c;
	}
	
}
