import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IQ extends Stage
{
	Queue<Instruction_info> mul1_in;
	Queue<Instruction_info> div1_in;
	Queue<Instruction_info> intfu_in;
	// issue queue
	ArrayList<Instruction_info> instr_queue;
	// forwarded values holder.
	ArrayList<Instruction_info> forwarded_list;

	public IQ(Queue<Instruction_info> q_in_arg, Queue<Instruction_info> q_out_arg) 
	{
		super(q_in_arg, q_out_arg);
		name="IQ";
		instr_queue= new ArrayList<>();
		
	}
	
	public void setMulDivIntFUin(Queue<Instruction_info> mul1_in,Queue<Instruction_info> div1_in,
			Queue<Instruction_info> intfu_in)
	{
		this.mul1_in=mul1_in;
		this.div1_in=div1_in;
		this.intfu_in=intfu_in;
	}
	
	public void setForwardedBus(ArrayList<Instruction_info> forwarded_list)
	{
		this.forwarded_list= forwarded_list;
	}
	
	public void catchForwardedValues()
	{
		for (Instruction_info instr_in_queue : instr_queue)
		{
			for (AddrValPair source_reg_in_queue : instr_in_queue.source_registers)
			{
				for (Instruction_info forwarded_instr: forwarded_list)
				{
					if(source_reg_in_queue.reg_name.equals(forwarded_instr.dest_registers.get(0).reg_name))
					{
						source_reg_in_queue.reg_value=forwarded_instr.dest_registers.get(0).reg_value;
						instr_in_queue.catched_sources=instr_in_queue.catched_sources+1;
						source_reg_in_queue.is_catched_resources=true;
					}
				}
			}

		}
		
		//it's not cumulative so flush it.
		forwarded_list.clear();
	}
	public void Issue()
	{
		boolean mul_waiting=true;
		boolean div_waiting=true;
		boolean intfu_waiting=true;

		for (int i = 0; i < instr_queue.size(); i++)
		{
			boolean all_catched=true;
			// see if all resources are catched.
			for (AddrValPair source_reg_in : instr_queue.get(i).source_registers)
			{
				if(!source_reg_in.is_catched_resources)
				{
					all_catched=false;
				}
			}
			
			// it catched all the sources
			if(all_catched)
			{
				if(instr_queue.get(i).instruction_string.equals("MUL"))
				{
					if(mul_waiting)
					{
						mul1_in.add(instr_queue.get(i));
						mul_waiting=false;
						// remove from IQ
						instr_queue.remove(i);
					}
					else 
					{
						continue;
					}
					
				}
				else if(instr_queue.get(i).instruction_string.equals("DIV") ||instr_queue.get(i).instruction_string.equals("HALT"))
				{
					if(div_waiting)
					{
						div1_in.add(instr_queue.get(i));	
						div_waiting=false;
						// remove from IQ
						instr_queue.remove(i);
					}
					else 
					{
						continue;
					}
				}
				else
				{
					//only send one checker
					if(intfu_waiting)
					{
						intfu_in.add(instr_queue.get(i));
						intfu_waiting=false;
						// remove from IQ
						instr_queue.remove(i);
					}
					else
					{
						continue;
					}
					
					
				}	
			}
		}
	}
	
	public void append_q_out()
	{			
		Issue();
//		storeIncomingInstructions();
	}
	public void storeIncomingInstructions()
	{
		if(instr!=null)
		{
			instr_queue.add(instr);	
		}
	}
	public void doInstr()
	{
		catchForwardedValues();
		storeIncomingInstructions();
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
			
	}

	
}
