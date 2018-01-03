import java.util.Queue;


public abstract class Stage {
	protected Queue<Instruction_info>  q_in;
	protected Instruction_info  instr;
	protected Queue<Instruction_info> q_out;
	protected static boolean stalled;
	protected static int pc;
	protected  String name;

	
	// set queues for q_in and q_out
	public Stage (Queue<Instruction_info> q_in_arg, 
			Queue<Instruction_info> q_out_arg )
	{
		q_in= q_in_arg;
		q_out= q_out_arg;
		instr=null;
		stalled=false;
		pc=4000;
		name="";

	}
	public boolean isStall() 
	{
		return stalled;
	}
//	public abstract void doInstr();

	public void doInstr()
	{
		if(instr==null)
		{
			return;
		}
	}
	
	public void printStatus()
	{
		// if queue is not empty.
		if(q_in.peek()!=null)
		{
			System.out.println(name+"	"+"I"+ q_in.peek().instr_no+"  "+ q_in.peek().raw_instr );

		}
		// if queue is empty
		else
		{
			System.out.println(name+"	Empty");
		}
	}
	
	
	public void pop_q_in()
	{
		// could be, data or null.
		instr=q_in.poll();
	}
	
	public void append_q_out()
	{

//		// if it processed something.
//		if(instr!=null)
//		{
//			if(q_out.size()==0)
//			{
//				q_out.add(instr);
//			}
//			else
//			{
//				q_in.add(instr);
//			}
//			
//		}			
		if(instr!=null)
		{
			q_out.add(instr);
		}	

		
	}
	
	
	public void execute()
	{
		// if not stall
		if(!(isStall()))
		{
			pop_q_in();
			doInstr();
		}
		
		else
		{
			instr=null;
			return;
		}

		
//		// previous inst. still in there?
//		if(data == null)
//		{
//			// if not fetch from q_in
//			data = q_in.poll();
//			
//			//if q_in is empty
//			if(data==null)
//			{
//				//do nothing
//				return;
//			}
//		}
//		
//		if( isStall())
//		{
//			//do nothing
//			return;
//		}
//		else
//		{
//			// do stage specific instruction.
//			doInstr();
//			// add instruction to the next stage input queue.
//			q_out.add(data);
//			// set data null again for next cycle.
//			data=null;
//			return
			
	}

}
