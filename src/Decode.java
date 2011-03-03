/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 2/28/2011
 */
public class Decode {

	int[] decInst;
	int[] converted;
	public Decode()
	{
		decInst = new int[5];
		converted = new int[8];
	}
	
	public int[] DecodeInst(char[] a)
	{
		for (int i=0; i<8; i++)
			converted[i]=convertHextoInt(a[i]);
		if ((converted[0]==0)||(converted[0]==1))
		{
			decInst[0]=0;
			decInst[1]=converted[0]*16+converted[1];
			decInst[2]=converted[2];
			decInst[3]=converted[3];
			decInst[4]=converted[4];
		}
		else if ((converted[0]==4)||(converted[0]==5))
		{
			decInst[0]=1;
			decInst[1]=(converted[0]%4)*16+converted[1];
			decInst[2]=converted[2];
			decInst[3]=converted[3];
			decInst[4]=converted[4]*4096+converted[5]*256+converted[6]*16+converted[7];
		}
		else if (converted[0]==9)
		{
			decInst[0]=2;
			decInst[1]=(converted[0]%8)*16+converted[1];
			decInst[2]=converted[2]*1048576+converted[3]*65536+converted[4]*4096+converted[5]*256+converted[6]*16+converted[7];
		}
		else
		{
			decInst[0]=3;
			decInst[1]=(converted[0]%12)+converted[1];
			decInst[2]=converted[2];
			decInst[3]=converted[3];
			decInst[4]=converted[4]*4096+converted[5]*256+converted[6]*16+converted[7];
		}
		return decInst;
	}
	
	private int convertHextoInt(char c)
	{
		switch (c)
		{
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'A':
			return 10;
		case 'B':
			return 11;
		case 'C':
			return 12;
		case 'D':
			return 13;
		case 'E':
			return 14;
		case 'F':
			return 15;
		}
		return -1;
	}
			
}