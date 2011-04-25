/**
 * 
 */

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
	public void ExecInst(int[] c)
	{
		/*for (int i=0; i<5; i++)
			System.out.print(c[i]+ " ");
		System.out.println();*/
		
		switch (c[1])
		{
		case 0:
			//System.out.println("RD" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.count++;
			if (c[4]==0)
				address = EffectiveAddress.DirectAddress(0,pc.p.registerBank[c[3]]);
			else
				address = EffectiveAddress.DirectAddress(0, c[4]);
			address -= pc.p.codeSize;
			offset = address % 4;
			address = address / 4;
			pc.p.ioFrame = address;
			pc.p.ioOffset = offset;
			pc.p.readInst = c;
			read.push(pc.p);
			pc.p.running = false;
			pc.t.interrupt();
			break;
		case 1:
			//System.out.println("WR" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.count++;
			hex = Integer.toHexString(pc.p.registerBank[c[2]]);
			hex = hex.toUpperCase();
			hexArr = hex.toCharArray();
			for (i=0; i<8-hexArr.length; i++)
				inst[i] = '0';
			for (j=0; j<hexArr.length; j++)
				inst[i+j] = hexArr[j];
			if (c[4]==0)
				address = EffectiveAddress.DirectAddress(0,pc.p.registerBank[c[3]]);
			else
				address = EffectiveAddress.DirectAddress(0, c[4]);
			address -= (pc.p.codeSize + 20) ;
			offset = address % 4;
			address = address / 4;
			pc.p.ioFrame = address;
			pc.p.ioOffset = offset;
			pc.p.writeInst = inst.clone();
			write.push(pc.p);
			pc.p.running = false;
			pc.t.interrupt();
			break;
		case 2:
			//System.out.println("ST" + " " + c[2] + " " + c[3] + " " + c[4]);
			hex = Integer.toHexString(pc.p.registerBank[c[2]]);
			hex = hex.toUpperCase();
			hexArr = hex.toCharArray();
			for (i=0; i<8-hexArr.length; i++)
				inst[i] = '0';
			for (j=0; j<hexArr.length; j++)
				inst[i+j] = hexArr[j];
			address = EffectiveAddress.DirectAddress(0,pc.p.registerBank[c[3]]);
			//Dispatcher.threadMessage("Storing at " + address);
			address -= (pc.p.codeSize + 32);
			offset = address % 4;
			address = address / 4;
			pc.tempCache[address][offset] = inst.clone();
			break;
		case 3:
			//System.out.println("LW" + " " + c[2] + " " + c[3] + " " + c[4]);
			address = EffectiveAddress.DirectAddress(0,pc.p.registerBank[c[2]]);
			//Dispatcher.threadMessage("Loading from " + address);
			address -= (pc.p.codeSize + 32);
			offset = address % 4;
			address = address / 4;
			inst = pc.tempCache[address][offset].clone();
			for (i=0; i<8; i++)
			{
				power = 1;
				for (j=0; j<7-i; j++)
				{
					power *=16;
				}
				sum += (HexToInt.convertHextoInt(inst[i]))*power;
			}
			pc.p.registerBank[c[3]] = sum;
			break;
		case 4:
			//System.out.println("MOV" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[2]]= pc.p.registerBank[c[3]];
			break;
		case 5:
			//System.out.println("ADD" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] + pc.p.registerBank[c[3]];
			break;
		case 6:
			//System.out.println("SUB" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] - pc.p.registerBank[c[3]];
			break;
		case 7:
			//System.out.println("MUL" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] * pc.p.registerBank[c[3]];
			break;
		case 8:
			//System.out.println("DIV" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] / pc.p.registerBank[c[3]];
			break;
		case 9:
			//System.out.println("AND" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] & pc.p.registerBank[c[3]];
			break;
		case 10:
			//System.out.println("OR" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] | pc.p.registerBank[c[3]];
			break;
		case 11:
			//System.out.println("MOVI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] = c[4];
			break;
		case 12:
			//System.out.println("ADDI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] += c[4];
			break;
		case 13:
			//System.out.println("MULI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] *= c[4];
			break;
		case 14:
			//System.out.println("DIVI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] /= c[4];
			break;
		case 15:
			//System.out.println("LDI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] = c[4];
			break;
		case 16:
			//System.out.println("SLT" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]]<pc.p.registerBank[c[3]])
				pc.p.registerBank[c[4]]=1;
			else
				pc.p.registerBank[c[4]]=0;
			break;
		case 17:
			//System.out.println("SLTI" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]]<c[4])
				pc.p.registerBank[c[3]]=1;
			else
				pc.p.registerBank[c[3]]=0;
			break;
		case 18:
			System.out.println("HLT" + " " + c[2] + " " + c[3] + " " + c[4]);
			address = pc.p.codeSize;
			pc.p.PC = address % 4;
			pc.p.FC = address / 4; // Force PC to end of job
			//pc.p.runEnd = System.nanoTime();
			pc.p.finished = true;
			pc.t.interrupt();
			break;
		case 20:
			//System.out.println("JMP" + " " + c[2] + " " + c[3] + " " + c[4]);
			address = EffectiveAddress.DirectAddress(0, c[2]);
			pc.p.PC = address % 4;
			pc.p.FC = address / 4;
			break;
		case 21:
			//System.out.println("BEQ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] == pc.p.registerBank[c[3]])
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
			}
			break;
		case 22:
			//System.out.println("BNE" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] != pc.p.registerBank[c[3]])
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
			}
			break;
		case 23:
			//System.out.println("BEZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[3]] == 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
			}
			break;
		case 24:
			//System.out.println("BNZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] != 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
			}
			break;
		case 25:
			//System.out.println("BGZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] > 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
			}
			break;
		case 26:
			//System.out.println("BLZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] < 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
			}
			break;
		}
		pc.p.PC++;
		if (pc.p.PC == 4)
		{
			pc.p.PC = 0;
			pc.p.FC++;
		}
	}
	
}
