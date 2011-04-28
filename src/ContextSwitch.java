/**
 * @author bbernhar
 *
 */
public class ContextSwitch {
	
	public static void SwitchIn(CPU pc, PCB p)
	{
		//Dispatcher.threadMessage("Swapping job " + p.jobID + " into pc");
//		pc.registerBank = null;
//		pc.tempCache = null;
		pc.registerBank = p.registerBank.clone();
		pc.tempCache = p.tempCache.clone();
	}
	
	public static void SwitchOut(CPU pc, PCB p)
	{
		//Dispatcher.threadMessage("Swapping job " + p.jobID + " out of pc");
//		p.registerBank = null;
//		p.tempCache = null;
		p.registerBank = pc.registerBank.clone();
		p.tempCache = pc.tempCache.clone();
	}
}