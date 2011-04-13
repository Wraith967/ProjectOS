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
	DMAChannel dm;
	char[] inst, hexArr;
	String hex; 
	int sum, power, i, j;
	int count; // number of I/O requests per job
	
	public Execute(CPU c, MemoryManager m, DMAChannel dm)
	{
		pc = c;
		mgr = m;
		inst = new char[8];
		this.dm = dm;
		count = 0;
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
			count++;
			sum = 0;
			if (c[4] == 0)
				inst = dm.Read(0, pc.p.registerBank[c[3]], pc);
			else
				inst = dm.Read(0, c[4], pc);
			for (i=0; i<8; i++)
			{
				power = 1;
				for (j=0; j<7-i; j++)
				{
					power *=16;
				}
				sum += (HexToInt.convertHextoInt(inst[i]))*power;
			}
			pc.p.registerBank[c[2]] = sum;
			//pc.p.PC--;
			break;
		case 1:
			//System.out.println("WR" + " " + c[2] + " " + c[3] + " " + c[4]);
			count++;
			hex = Integer.toHexString(pc.p.registerBank[c[2]]);
			hex = hex.toUpperCase();
			hexArr = hex.toCharArray();
			for (i=0; i<8-hexArr.length; i++)
				inst[i] = '0';
			for (j=0; j<hexArr.length; j++)
				inst[i+j] = hexArr[j];
			if (c[4]==0)
			{
				dm.Write(0, pc.p.registerBank[c[3]], inst.clone(), pc);
				pc.p.changeIndex[pc.p.numChange++] = EffectiveAddress.DirectAddress(0,pc.p.registerBank[c[3]]);
			}
			else
			{
				dm.Write(0, c[4], inst.clone(), pc);
				pc.p.changeIndex[pc.p.numChange++] = EffectiveAddress.DirectAddress(0,c[4]);
			}
			//pc.p.PC--;
			break;
		case 2:
			//System.out.println("ST" + " " + c[2] + " " + c[3] + " " + c[4]);
			count++;
			hex = Integer.toHexString(pc.p.registerBank[c[2]]);
			hex = hex.toUpperCase();
			hexArr = hex.toCharArray();
			for (i=0; i<8-hexArr.length; i++)
				inst[i] = '0';
			for (j=0; j<hexArr.length; j++)
				inst[i+j] = hexArr[j];
			//pc.cache[EffectiveAddress.DirectAddress(0, pc.p.registerBank[c[3]])] = inst.clone();
			break;
		case 3:
			//System.out.println("LW" + " " + c[2] + " " + c[3] + " " + c[4]);
			count++;
			sum = 0;
			//inst = pc.cache[EffectiveAddress.DirectAddress(c[4],pc.p.registerBank[c[2]])].clone();
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
			//System.out.println("HLT" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.PC = pc.p.codeSize; // Force PC to end of job
			pc.p.runEnd = System.nanoTime();
			pc.t.interrupt();
			break;
		case 20:
			//System.out.println("JMP" + " " + c[2] + " " + c[3] + " " + c[4]);
			pc.p.PC = EffectiveAddress.DirectAddress(0, c[2]);
			break;
		case 21:
			//System.out.println("BEQ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] == pc.p.registerBank[c[3]])
				pc.p.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 22:
			//System.out.println("BNE" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] != pc.p.registerBank[c[3]])
				pc.p.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 23:
			//System.out.println("BEZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[3]] == 0)
				pc.p.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 24:
			//System.out.println("BNZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] != 0)
				pc.p.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 25:
			//System.out.println("BGZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] > 0)
				pc.p.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 26:
			//System.out.println("BLZ" + " " + c[2] + " " + c[3] + " " + c[4]);
			if (pc.p.registerBank[c[2]] < 0)
				pc.p.PC = EffectiveAddress.DirectAddress(0, c[4]);
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
