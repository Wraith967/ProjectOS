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
//		String msg = "";
//		for (int i=0; i<5; i++)
//			msg += c[i]+ " ";
//		Dispatcher.threadMessage(msg);
		
		switch (c[1])
		{
		case 0:
			Dispatcher.threadMessage("RD" + " " + c[2] + " " + c[3] + " " + c[4]);
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
			pc.p.readInst = c.clone();
			read.push(pc.p);
			pc.p.running = false;
			pc.t.interrupt();
			break;
		case 1:
			Dispatcher.threadMessage("WR" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.count++;
			hex = Integer.toHexString(pc.p.registerBank[c[2]]);
			//Dispatcher.threadMessage("hex = " + hex + " for register " + c[2]);
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
			address -= pc.p.codeSize ;
			offset = address % 4;
			address = address / 4;
			pc.p.ioFrame = address;
			pc.p.ioOffset = offset;
			pc.p.writeInst = inst.clone();
			//System.out.println(inst);
			write.push(pc.p);
			pc.p.running = false;
			pc.t.interrupt();
			break;
		case 2:
			Dispatcher.threadMessage("ST" + " " + c[2] + " " + c[3] + " " + c[4]);
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
			Dispatcher.threadMessage("LW" + " " + c[2] + " " + c[3] + " " + c[4]);
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
			Dispatcher.threadMessage("MOV" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[2]]= pc.p.registerBank[c[3]];
			break;
		case 5:
			Dispatcher.threadMessage("ADD" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] + pc.p.registerBank[c[3]];
			break;
		case 6:
			Dispatcher.threadMessage("SUB" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] - pc.p.registerBank[c[3]];
			break;
		case 7:
			Dispatcher.threadMessage("MUL" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] * pc.p.registerBank[c[3]];
			break;
		case 8:
			Dispatcher.threadMessage("DIV" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] / pc.p.registerBank[c[3]];
			break;
		case 9:
			Dispatcher.threadMessage("AND" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] & pc.p.registerBank[c[3]];
			break;
		case 10:
			Dispatcher.threadMessage("OR" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[4]]=pc.p.registerBank[c[2]] | pc.p.registerBank[c[3]];
			break;
		case 11:
			Dispatcher.threadMessage("MOVI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] = c[4];
			break;
		case 12:
			Dispatcher.threadMessage("ADDI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] += c[4];
			break;
		case 13:
			Dispatcher.threadMessage("MULI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] *= c[4];
			break;
		case 14:
			Dispatcher.threadMessage("DIVI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] /= c[4];
			break;
		case 15:
			Dispatcher.threadMessage("LDI" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.registerBank[c[3]] = c[4];
			break;
		case 16:
			Dispatcher.threadMessage("SLT" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]]<pc.p.registerBank[c[3]])
				pc.p.registerBank[c[4]]=1;
			else
				pc.p.registerBank[c[4]]=0;
			break;
		case 17:
			Dispatcher.threadMessage("SLTI" + " " + c[2] + " " + c[3] + " " + c[4]);
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
			jumped = true;
			pc.t.interrupt();
			break;
		case 20:
			Dispatcher.threadMessage("JMP" + " " + c[2] + " " + c[3] + " " + c[4]);
			address = EffectiveAddress.DirectAddress(0, c[2]);
			pc.p.PC = address % 4;
			pc.p.FC = address / 4;
			jumped = true;
			break;
		case 21:
			Dispatcher.threadMessage("BEQ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] == pc.p.registerBank[c[3]])
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 22:
			Dispatcher.threadMessage("BNE" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] != pc.p.registerBank[c[3]])
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 23:
			Dispatcher.threadMessage("BEZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[3]] == 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 24:
			Dispatcher.threadMessage("BNZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] != 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 25:
			Dispatcher.threadMessage("BGZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] > 0)
			{
				address = EffectiveAddress.DirectAddress(0, c[4]);
				pc.p.PC = address % 4;
				pc.p.FC = address / 4;
				jumped = true;
			}
			break;
		case 26:
			Dispatcher.threadMessage("BLZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] < 0)
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
