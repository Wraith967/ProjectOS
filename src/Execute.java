/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 2/28/2011
 */
public class Execute {
	
	CPU pc;
	MemoryManager mgr;
	EffectiveAddress ea;
	
	public Execute(CPU c, MemoryManager m)
	{
		pc = c;
		mgr = m;
		ea = new EffectiveAddress();
	}
	
	public void ExecInst(int[] c)
	{
		//TODO Finish this code
		for (int i=0; i<5; i++)
			System.out.print(c[i]+ " ");
		System.out.println();
		
		switch (c[0])
		{
		case 0:
			switch (c[1])
			{
			case 4:
				pc.registerBank[c[4]]= pc.registerBank[c[3]];
				break;
			case 5:
				pc.registerBank[c[4]]=pc.registerBank[c[3]] + pc.registerBank[c[2]];
				break;
			case 6:
				pc.registerBank[c[4]]=pc.registerBank[c[3]] - pc.registerBank[c[2]];
				break;
			case 7:
				pc.registerBank[c[4]]=pc.registerBank[c[3]] * pc.registerBank[c[2]];
				break;
			case 8:
				pc.registerBank[c[4]]=pc.registerBank[c[3]] / pc.registerBank[c[2]];
				break;
			case 9:
				pc.registerBank[c[4]]=pc.registerBank[c[3]] & pc.registerBank[c[2]];
				break;
			case 10:
				pc.registerBank[c[4]]=pc.registerBank[c[3]] | pc.registerBank[c[2]];
				break;
			case 16:
				if (pc.registerBank[c[2]]<pc.registerBank[c[3]])
					pc.registerBank[c[4]]=1;
				else
					pc.registerBank[c[4]]=0;
				break;
			}
			break;
		case 1:
			switch (c[1])
			{
			case 2:
				
			case 3:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 17:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			}
			break;
		case 2:
			switch (c[1])
			{
			case 14:
			case 16:
			}
			break;
		case 3:
			switch (c[1])
			{
			case 0:
			case 1:
			}
		}
	}
	
}
