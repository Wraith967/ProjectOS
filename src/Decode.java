/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 3/3/2011
 */
public class Decode {

	int[] decInst;
	int[] converted;
	public Decode()
	{
		decInst = new int[5];
		converted = new int[8];
	}
	
	/**
	 * 
	 * @param a Instruction from memory to decode
	 * @return
	 */
	public int[] DecodeInst(char[] a)
	{
		String msg = "";
		for (int i=0; i<8; i++)
		{
			msg += a[i];
			converted[i] = HexToInt.convertHextoInt(a[i]);
		}
		switch (converted[0])
		{
			case 0:
			case 1:
				decInst[0]=0;
				decInst[1]=converted[0]*16+converted[1];
				decInst[2]=converted[2];
				decInst[3]=converted[3];
				decInst[4]=converted[4];
				break;
			case 4:
			case 5:
				decInst[0]=1;
				decInst[1]=(converted[0]%4)*16+converted[1];
				decInst[2]=converted[2];
				decInst[3]=converted[3];
				decInst[4]=converted[4]*4096+converted[5]*256+converted[6]*16+converted[7];
				break;
			case 9:
				decInst[0]=2;
				decInst[1]=(converted[0]%8)*16+converted[1];
				decInst[2]=converted[2]*1048576+converted[3]*65536+converted[4]*4096+converted[5]*256+converted[6]*16+converted[7];
				decInst[3]=0;
				decInst[4]=0;
				break;
			default:
				decInst[0]=3;
				decInst[1]=(converted[0]%12)+converted[1];
				decInst[2]=converted[2];
				decInst[3]=converted[3];
				decInst[4]=converted[4]*4096+converted[5]*256+converted[6]*16+converted[7];
				break;
		}
		return decInst;
	}			
}