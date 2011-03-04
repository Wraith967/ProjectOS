/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 3/3/2011
 */
public class Execute {
	
	CPU pc;
	MemoryManager mgr;
	char[] inst, hexArr;
	String hex; 
	int sum, i;
	
	public Execute(CPU c, MemoryManager m)
	{
		pc = c;
		mgr = m;
		inst = new char[8];
		sum = 0;
		i = 0;
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
			//System.out.println("RD");
			sum = 0;
			inst = mgr.ReadInstruction(EffectiveAddress.DirectAddress(c[4],pc.registerBank[c[3]]));
			for (int i=0; i<8; i++)
				sum += HexToInt.convertHextoInt(inst[i]);
			pc.registerBank[c[2]] = sum;
			break;
		case 1:
			//System.out.println("WR");
			hex = Integer.toHexString(pc.registerBank[c[2]]);
			hexArr = hex.toCharArray();
			for (int i=hexArr.length-1; i>=0; i--)
				inst[7-i]=hexArr[i];
			mgr.WriteInstruction(EffectiveAddress.DirectAddress(0,pc.registerBank[c[3]]), inst);
			break;
		case 2:
			//System.out.println("ST");
			hex = Integer.toHexString(pc.registerBank[c[2]]);
			hexArr = hex.toCharArray();
			for (int i=hexArr.length-1; i>=0; i--)
				inst[7-i]=hexArr[i];
			mgr.WriteInstruction(EffectiveAddress.DirectAddress(0,pc.registerBank[c[3]]), inst);
			break;
		case 3:
			//System.out.println("LW");
			sum = 0;
			inst = mgr.ReadInstruction(EffectiveAddress.DirectAddress(c[4],pc.registerBank[c[2]]));
			for (int i=0; i<8; i++)
				sum += HexToInt.convertHextoInt(inst[i]);
			pc.registerBank[c[3]] = sum;
			break;
		case 4:
			//System.out.println("MOV");
			pc.registerBank[c[4]]= pc.registerBank[c[3]];
			break;
		case 5:
			//System.out.println("ADD");
			pc.registerBank[c[4]]=pc.registerBank[c[3]] + pc.registerBank[c[2]];
			break;
		case 6:
			//System.out.println("SUB");
			pc.registerBank[c[4]]=pc.registerBank[c[3]] - pc.registerBank[c[2]];
			break;
		case 7:
			//System.out.println("MUL");
			pc.registerBank[c[4]]=pc.registerBank[c[3]] * pc.registerBank[c[2]];
			break;
		case 8:
			//System.out.println("DIV");
			pc.registerBank[c[4]]=pc.registerBank[c[3]] / pc.registerBank[c[2]];
			break;
		case 9:
			//System.out.println("AND");
			pc.registerBank[c[4]]=pc.registerBank[c[3]] & pc.registerBank[c[2]];
			break;
		case 10:
			//System.out.println("OR");
			pc.registerBank[c[4]]=pc.registerBank[c[3]] | pc.registerBank[c[2]];
			break;
		case 11:
			//System.out.println("MOVI");
			pc.registerBank[c[3]] = c[4];
			break;
		case 12:
			//System.out.println("ADDI");
			pc.registerBank[c[3]] += c[4];
			break;
		case 13:
			//System.out.println("MULI");
			pc.registerBank[c[3]] *= c[4];
			break;
		case 14:
			//System.out.println("DIVI");
			pc.registerBank[c[3]] /= c[4];
			break;
		case 15:
			//System.out.println("LDI");
			pc.registerBank[c[3]] = c[4];
			break;
		case 16:
			//System.out.println("SLT");
			if (pc.registerBank[c[2]]<pc.registerBank[c[3]])
				pc.registerBank[c[4]]=1;
			else
				pc.registerBank[c[4]]=0;
			break;
		case 17:
			//System.out.println("SLTI");
			if (pc.registerBank[c[2]]<c[4])
				pc.registerBank[c[3]]=1;
			else
				pc.registerBank[c[3]]=0;
			break;
		case 18:
			//System.out.println("HLT");
			pc.PC = pc.jobSize; // Force PC to end of job
			break;
		case 20:
			//System.out.println("JMP");
			pc.PC = EffectiveAddress.DirectAddress(0, c[2]);
			break;
		case 21:
			//System.out.println("BEQ");
			if (pc.registerBank[c[2]] == pc.registerBank[c[3]])
				pc.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 22:
			//System.out.println("BNE");
			if (pc.registerBank[c[2]] != pc.registerBank[c[3]])
				pc.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 23:
			//System.out.println("BEZ");
			if (pc.registerBank[c[3]] == 0)
				pc.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 24:
			//System.out.println("BNZ");
			if (pc.registerBank[c[2]] != 0)
				pc.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 25:
			//System.out.println("BGZ");
			if (pc.registerBank[c[2]] > 0)
				pc.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		case 26:
			//System.out.println("BLZ");
			if (pc.registerBank[c[2]] < 0)
				pc.PC = EffectiveAddress.DirectAddress(0, c[4]);
			break;
		}
	}
	
}
