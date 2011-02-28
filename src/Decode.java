/**
 * 
 */

/**
 * @author Ben
 * Created: 2/17/2011
 * Last Edit: 2/28/2011
 */
public class Decode {

	String inst;
	public Decode()
	{
		inst = null;
	}
	
	public String DecodeInst(char a, char b)
	{
		switch (a)
		{
		case '0':
			switch(b)
			{
			case '4':
				inst = "MOV";
				break;
			case '5':
				inst = "ADD";
				break;
			case '6':
				inst = "SUB";
				break;
			case '7':
				inst = "MUL";
				break;
			case '8':
				inst = "DIV";
				break;
			case '9':
				inst = "AND";
				break;
			case 'A':
				inst = "OR";
				break;
			}
			break;
		case '1':
			inst = "SLT";
			break;
		case '4':
			switch (b)
			{
			case '2':
				inst = "ST";
				break;
			case '3':
				inst = "LW";
				break;
			case 'B':
				inst = "MOVI";
				break;
			case 'C':
				inst = "ADDI";
				break;
			case 'D':
				inst = "MULI";
				break;
			case 'E':
				inst = "DIVI";
				break;
			case 'F':
				inst = "LDI";
				break;
			}
			break;
		case '5':
			switch (b)
			{
			case '1':
				inst = "SLTI";
				break;
			case '5':
				inst = "BEQ";
				break;
			case '6':
				inst = "BNE";
				break;
			case '7':
				inst = "BEZ";
				break;
			case '8':
				inst = "BNZ";
				break;
			case '9':
				inst = "BGZ";
				break;
			case 'A':
				inst = "BLZ";
				break;
			}
			break;
		case '9':
			switch (b)
			{
			case '2':
				inst = "HLT";
				break;
			case '4':
				inst = "JMP";
				break;
			}
			break;
		case 'C':
			switch (b)
			{
			case '0':
				inst = "RD";
				break;
			case '1':
				inst = "WR";
				break;
			}
			break;
			}
		return inst;
	}
}