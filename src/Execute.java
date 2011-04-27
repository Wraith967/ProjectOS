/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 3/8/2011
 */
public class Execute {
	
	CPU pc;
	MemoryManager mgr;
	char[] inst, hexArr;
	String hex; 
	int sum, power, i, j;
	int address, offset;
	BlockingQueue read, write;
	boolean jumped;
	
	public Execute(CPU c, MemoryManager m, BlockingQueue r, BlockingQueue w)
	{
		pc = c;
		mgr = m;
		inst = new char[8];
		read = r;
		write = w;
	}
	
	/**
	 * 
	 * @param c Int[] holding decoded instruction
	 */
	public boolean ExecInst(int[] c)
	{
		jumped = false;
		
		switch (c[1])
		{
		case 0:
			pc.p.count++;
			//Dispatcher.threadMessage("Current value of register " + c[3] + " = " + pc.registerBank[c[3]]);
			if (c[4]==0)
				address = EffectiveAddress.DirectAddress(0,pc.registerBank[c[3]]);
			else
				address = EffectiveAddress.DirectAddress(0, c[4]);
			address -= pc.p.codeSize;
			offset = address % 4;
			address = address / 4;
			address += pc.p.numPages;
			pc.p.ioFrame = address;
			pc.p.ioOffset = offset;
			pc.p.readInst = c.clone();
			pc.p.running = false;
			pc.p.reading = true;
			pc.t.interrupt();
			break;
		case 1:
			pc.p.count++;
			hex = Integer.toHexString(pc.registerBank[c[2]]);
			hex = hex.toUpperCase();
			hexArr = hex.toCharArray();
			for (i=0; i<8-hexArr.length; i++)
				inst[i] = '0';
			for (j=0; j<hexArr.length; j++)
				inst[i+j] = hexArr[j];
			if (c[4]==0)
				address = EffectiveAddress.DirectAddress(0,pc.registerBank[c[3]]);
			else
				address = EffectiveAddress.DirectAddress(0, c[4]);
			address -= pc.p.codeSize;
			offset = address % 4;
			address = address / 4;
			address += pc.p.numPages;
			pc.p.ioFrame = address;
			pc.p.ioOffset = offset;
			pc.p.writeInst = inst.clone();
			pc.p.writing = true;
			pc.p.running = false;
			pc.t.interrupt();
			break;
		case 2:
			hex = Integer.toHexString(pc.registerBank[c[2]]);
			hex = hex.toUpperCase();
			hexArr = hex.toCharArray();
			for (i=0; i<8-hexArr.length; i++)
				inst[i] = '0';
			for (j=0; j<hexArr.length; j++)
				inst[i+j] = hexArr[j];
			address = EffectiveAddress.DirectAddress(0,pc.registerBank[c[3]]);
			address -= (pc.p.codeSize + 32);
			offset = address % 4;
			address = address / 4;
			pc.tempCache[address][offset] = inst.clone();
			break;
		case 3:
			address = EffectiveAddress.DirectAddress(0,pc.registerBank[c[3]]);
			address -= (pc.p.codeSize + 32);
			offset = address % 4;
			address = address / 4;
			inst = pc.tempCache[address][offset].clone();
			sum = 0;
			for (i=0; i<8; i++)
			{
				power = 1;
				for (j=0; j<7-i; j++)
				{
					power *=16;
				}
				sum += (HexToInt.convertHextoInt(inst[i]))*power;
			}
			pc.registerBank[c[3]] = sum;
			break;
		case 4:
			pc.registerBank[c[2]]= pc.registerBank[c[3]];
			break;
		case 5:
			pc.registerBank[c[4]]=pc.registerBank[c[2]] + pc.registerBank[c[3]];
			break;
		case 6:
			pc.registerBank[c[4]]=pc.registerBank[c[2]] - pc.registerBank[c[3]];
			break;
		case 7:
			pc.registerBank[c[4]]=pc.registerBank[c[2]] * pc.registerBank[c[3]];
			break;
		case 8:
			pc.registerBank[c[4]]=pc.registerBank[c[2]] / pc.registerBank[c[3]];
			break;
		case 9:
			pc.registerBank[c[4]]=pc.registerBank[c[2]] & pc.registerBank[c[3]];
			break;
		case 10:
			pc.registerBank[c[4]]=pc.registerBank[c[2]] | pc.registerBank[c[3]];
			break;
		case 11:
			pc.registerBank[c[3]] = c[4];
			break;
		case 12:
			pc.registerBank[c[3]] += c[4];
			break;
		case 13:
			pc.registerBank[c[3]] *= c[4];
			break;
		case 14:
			pc.registerBank[c[3]] /= c[4];
			break;
		case 15:
			pc.registerBank[c[3]] = c[4];
			break;
		case 16:
			if (pc.registerBank[c[2]]<pc.registerBank[c[3]])
				pc.registerBank[c[4]]=1;
			else
				pc.registerBank[c[4]]=0;
			break;
		case 17:
			if (pc.registerBank[c[2]]<c[4])
				pc.registerBank[c[3]]=1;
			else
				pc.registerBank[c[3]]=0;
			break;
		case 18:
			address = pc.p.codeSize;
			pc.p.PC = address % 4;
			pc.p.FC = address / 4; // Force PC to end of job
			pc.p.finished = true;
			jumped = true;
			pc.t.interrupt();
			break;
		case 20:
			address = EffectiveAddress.DirectAddress(0, c[2]);
			pc.p.PC = address % 4;
			pc.p.FC = address / 4;
			jumped = true;
			break;
		case 21:
			if (pc.registerBank[c[2]] == pc.registerBank[c[3]])
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 22:
			if (pc.registerBank[c[2]] != pc.registerBank[c[3]])
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 23:
			if (pc.registerBank[c[3]] == 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 24:
			if (pc.registerBank[c[2]] != 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 25:
			if (pc.registerBank[c[2]] > 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 26:
			if (pc.registerBank[c[2]] < 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		}
		return true;
	}	
}
