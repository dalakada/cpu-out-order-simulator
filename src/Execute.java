import java.util.ArrayList;
import java.util.Queue;

public class Execute extends Stage{
	Queue<Instruction_info> fetch_in;
	Queue<Instruction_info> decode_in;
	boolean isstalled=false;
	AddrValPair jal_to_forward_dest_reg;
	ArrayList<Instruction_info> forwarded_list;
	P_Register_File p_reg_file;
	
	ROB rob;
	
	public Execute(Queue<Instruction_info> q_in_arg, Queue<Instruction_info> q_out_arg) {
		super(q_in_arg, q_out_arg);
		name="Execute";
	}
	
	public void setForwardedBus(ArrayList<Instruction_info> forwarded_list)
	{
		this.forwarded_list= forwarded_list;
	}
	
	
	public void setROB(ROB rob)
	{
		this.rob=rob;
	}
	public void setPRFBus(P_Register_File p_reg_file)
	{
		this.p_reg_file=p_reg_file;
	}


	public void setFetch_Decode_Queue(Queue<Instruction_info> fetch_in_arg,Queue<Instruction_info> decode_in_arg)
	{
		fetch_in=fetch_in_arg;
		decode_in=decode_in_arg;
	}
	
	public void flush()
	{
		fetch_in.poll();
		decode_in.poll();
	}

	//cannot be stalled
	public boolean isStall() {
		return false;
	}
	

	
	public void append_q_out()
	{
		//Do nothing
	}
	
	public void printStatus()
	{
		// if queue is not empty.
		if(q_in.peek()!=null)
		{
			if(isstalled==true)
			{
				System.out.println(name+"	"+"I"+ q_in.peek().instr_no+"  "+ q_in.peek().raw_instr +"	(Stalled)");

			}
			else
			{
				System.out.println(name+"	"+"I"+ q_in.peek().instr_no+"  "+ q_in.peek().raw_instr );
			}
		}
		// if queue is empty
		else
		{
			System.out.println(name+"	Empty");
		}
	}
	public void doInstr()
	{
		if(instr==null)
		{
			return;
		}
		else
		{
			if(instr.instruction_string.equals("ADD"))
			{
				//calculate addition
				int counter=0;
				for (AddrValPair pair: instr.source_registers)
				{
					counter=counter+pair.reg_value;
				}
				
				//store it in dest_reg_data
				
				instr.dest_registers.get(0).reg_value=counter;
				
				//forward
				forwarded_list.add(instr);
				
				// make physical register valid
				p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
				p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
				
				// set instruction finished in rob
				rob.finished_instrs.add(instr);

			}

			if(instr.instruction_string.equals("SUB"))
			{
				//calculate subtract
				
				
				int val1=instr.source_registers.get(0).reg_value;
				int val2=instr.source_registers.get(1).reg_value;
				int subtract= val1-val2;
				//store it in dest_reg_data
				instr.dest_registers.get(0).reg_value=subtract;
				//forward
				AddrValPair to_forward_dest_reg=instr.dest_registers.get(0);
				
				forwarded_list.add(instr);				
				// make physical register valid
				// write to physical register
				p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
				p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
				
				// set instruction finished in rob
				rob.finished_instrs.add(instr);


			}
			if(instr.instruction_string.equals("OR"))
			{
				//calculate OR
				
				
				int val1=instr.source_registers.get(0).reg_value;
				int val2=instr.source_registers.get(1).reg_value;
				int or_ed_value= val1|val2;
				//store it in dest_reg_data
				instr.dest_registers.get(0).reg_value=or_ed_value;
				
				//forward bus
				forwarded_list.add(instr);
				// make physical register valid
				p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
				p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
				
				// set instruction finished in rob
				rob.finished_instrs.add(instr);
			}
			if(instr.instruction_string.equals("AND"))
			{
				//calculate AND
				
				
				int val1=instr.source_registers.get(0).reg_value;
				int val2=instr.source_registers.get(1).reg_value;
				int and_ed_value= val1&val2;
				//store it in dest_reg_data
				instr.dest_registers.get(0).reg_value=and_ed_value;
				
				//forward bus
				forwarded_list.add(instr);
				// make physical register valid
				p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
				p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
				
				// set instruction finished in rob
				rob.finished_instrs.add(instr);
			}
			if(instr.instruction_string.equals("MOVC"))
			{
				//calculate MOVC
				
				int literal=instr.literal;
				int computed_val= literal+0;
				//store it in dest_reg_data
				instr.dest_registers.get(0).reg_value=computed_val;
				
				//forward bus
				forwarded_list.add(instr);
				
				// make physical register valid
				p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
				p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
				
				// set instruction finished in rob
				rob.finished_instrs.add(instr);
			}
			if(instr.instruction_string.equals("EX-OR"))
			{
				//calculate XOR
				
				
				int val1=instr.source_registers.get(0).reg_value;
				int val2=instr.source_registers.get(1).reg_value;
				int xored_val= val1^val2;
				//store it in dest_reg_data
				instr.dest_registers.get(0).reg_value=xored_val;
				
				//forward bus
				forwarded_list.add(instr);
				
				// make physical register valid
				p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
				p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
				
				// set instruction finished in rob
				rob.finished_instrs.add(instr);
			}
			if(instr.instruction_string.equals("LOAD"))
			{
				//calculate address to load
				
				int val1=instr.source_registers.get(0).reg_value;
				int literal=instr.literal;
				int address_to_fetch= val1+literal;
				//store it in dest_reg_data
				instr.target_memory_addr=address_to_fetch;
				

			}
			if(instr.instruction_string.equals("STORE"))
			{
				//calculate address to load
				
				int val1=instr.source_registers.get(1).reg_value;
				int literal=instr.literal;
				int address_to_write= val1+literal;
				//store it in dest_reg_data
				instr.target_memory_addr=address_to_write;
			}
			if(instr.instruction_string.equals("JUMP"))
			{
				//calculate address to load
				if(instr.isflushed==false)
				{
					int val1=instr.source_registers.get(0).reg_value;
					int literal=instr.literal;
					int address_to_jump= val1+literal;
					pc=address_to_jump;
					flush();
					instr.isflushed=true;
				}


			}
			
			if(instr.instruction_string.equals("BZ") || instr.instruction_string.equals("BNZ"))
			{
				if(instr.target_memory_addr==1)
				{
					if(instr.isflushed==false)
					{
					int literal=instr.literal;
					int address_to_jump=literal;
					pc=instr.instr_pc+address_to_jump;
					flush();
					instr.isflushed=true;
					}
					
				}
				else
				{
					return;
				}
				//calculate address to branch
			}
			
			if(instr.instruction_string.equals("JAL"))
			{
			

				if(instr.isflushed==false)
				{
					int val1=instr.source_registers.get(0).reg_value;
					int literal=instr.literal;
					int address_to_jump= val1+literal;
					pc=address_to_jump;
					instr.dest_registers.get(0).reg_value=instr.instr_pc+4;
					//forward
					jal_to_forward_dest_reg=instr.dest_registers.get(0);
					//forward bus
					forwarded_list.add(instr);
					// make physical register valid
					p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
					p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
					flush();
					// set instruction finished in rob
					rob.finished_instrs.add(instr);
					
					instr.isflushed=true;
					//store it in dest_reg_data
				}
				else
				{
					//forward bus
					forwarded_list.add(instr);
					// make physical register valid
					p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
					p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
					
					// set instruction finished in rob
					rob.finished_instrs.add(instr);
				}
				
			}


		}
	}

}
