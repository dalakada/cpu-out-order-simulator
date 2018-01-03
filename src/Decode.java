import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Decode extends Stage{

	boolean isstalled=false;
	Queue<Instruction_info> fetch_in;
	RAT rat;
	
	// to check if there is free space in IQ
	IQ iq;
	ROB rob;

		
	public Decode(Queue<Instruction_info> q_in_arg, Queue<Instruction_info> q_out_arg,RAT rat) {
		super(q_in_arg, q_out_arg);
		name="Decode";
		this.rat=rat;
	}

	public void setROB(ROB rob)
	{
		this.rob=rob;
	}
	

	// for halt
	public void setFetchQueue(Queue<Instruction_info> fetch_in_arg)
	{
		fetch_in=fetch_in_arg;
	}
	// for halt
	public void Halt_flush()
	{
		fetch_in.poll();
	}
	
	public boolean isSourceCommitted(String arch_reg_name)
	{
		boolean result =rat.isSourceCommitted( arch_reg_name);
		return result;
	}
	
	// to check if there is free space in IQ
	public void setIQholder(IQ iq)
	{
		this.iq=iq;
	}
	
	
	public int getCommittedRegValue(String arch_reg_name)
	{
		int result =rat.getCommittedRegValue( arch_reg_name);
		return result;
	}
	
	public boolean isPhysicalValid(String arch_reg_name)
	{
		boolean result = rat.isPhysicalValid(arch_reg_name);
		return result;
	}
	public int getValidPhysicalRegValue(String arch_reg_name)
	{
		int value= rat.getValidPhysicalRegValue(arch_reg_name);
		return value;
	}

	public void setSourceVals()
	{
		for (AddrValPair pair: instr.source_registers)
		{
			// there is no associated physical reg
			// fetch directly from ARF
			if(isSourceCommitted(pair.reg_name))
			{
				int source_reg_val = rat.getCommittedRegValue(pair.reg_name);
				pair.reg_value=source_reg_val;			
				pair.is_catched_resources=true;

			}
			// check if physical valid
			else if(isPhysicalValid(pair.reg_name))
			{
				int source_reg_val = rat.getValidPhysicalRegValue(pair.reg_name);
				pair.reg_value=source_reg_val;
				pair.is_catched_resources=true;
				
				//get the physical name
				String physical_reg_name= rat.getBusyPhysicalRegName(pair.reg_name);
				pair.reg_name=physical_reg_name;
			}
			
			// not valid only get the physical name
			else
			{
				String physical_reg_name= rat.getBusyPhysicalRegName(pair.reg_name);
				pair.reg_name=physical_reg_name;
			}
		}
	}
	
	public void setDestArchRegPhysicalReg()
	{
		if(!instr.dest_registers.isEmpty())
		{
			String dest_arch_reg_name=instr.dest_registers.get(0).reg_name;
			// set new physical name dest register.
			// rat also makes physical reg busy.
			instr.dest_registers.get(0).reg_name=rat.setDestArchRegPhysicalReg(dest_arch_reg_name);
		}
	}
	
	
//	public void setPswRegVal(int result)
//	{
//		String psw_reg_name="PSW";
//
//		if(result==0)
//		{
//			reg_file.setRegValuebyName(psw_reg_name, 0);
//		}
//		else
//		{
//			reg_file.setRegValuebyName(psw_reg_name, 1);
//		}
//		
//	}
	


	public boolean isStall() 
	{
		//can be instr or null.
		Instruction_info instr_to_check = 	q_in.peek();
		
		int IQ_queue_size=16;
		int rob_size=16;
		
		if(instr_to_check !=null)
		{
			// is there any free space in IQ
			if(iq.instr_queue.size()<16 && rob.ROB_QUEUE.size()<16)
			{
				stalled=false;
				return false;
			}
			else
			{
				// stalled variable for fetch.
				stalled=true;
				return true;
			}
		}

		else
		{
			stalled=false;
			return false;
		}
	}
	


	
//	public void setPSWRegInvalid()
//	{
//	
//		reg_file.setPSWStatus(1);
//		
//	}
//	

	
	public void doRename()
	{
		setSourceVals();
		setDestArchRegPhysicalReg();
	}
	
	public void createROBEntry(Instruction_info instr_to_create_entry)
	{
		rob.createROBEntry(instr_to_create_entry);
	}
	public void doInstr()
	{
		if(instr==null)
		{
			return;
		}
		else 
		{
			doRename();
			createROBEntry(instr);
			changeRawInstr(instr);
			
//			setPSWRegInvalid();
		}
	}
//	{
//		if(instr==null)
//		{
//			return;
//		}
//		else
//		{
//			if(instr.instruction_string.equals("BZ") || (instr.instruction_string.equals("BNZ")))
//			{
//				if(isPswInvalid_w_forward())
//				{
//					q_in.add(instr);
//					instr=null;
//					return;
//				}
//				//psw valid..
//				else
//				{
//					if(instr.instruction_string.equals("BZ") )
//					{
//						//condition met
//						// do branch..
//						if(reg_file.getRegValuebyName("PSW")==0)
//						{
//							//placeholder for saying do branching.
//							// 0 means do not branch
//							// 1 means do branch
//							instr.target_memory_addr=1;
//						}
//						// do not branch
//						else
//						{
//							instr.target_memory_addr=0;
//						}
//					}
//					if(instr.instruction_string.equals("BNZ") )
//					{
//						//condition met
//						// do branch..
//						if(reg_file.getRegValuebyName("PSW")!=0)
//						{
//							//placeholder for saying do branching.
//							// 0 means do not branch
//							// 1 means do branch
//							instr.target_memory_addr=1;
//						}
//						// do not branch
//						else
//						{
//							instr.target_memory_addr=0;
//						}
//					}
//
//				}
//			}
//			else if(instr.instruction_string.equals("LOAD") ||
//					instr.instruction_string.equals("STORE")||
//					instr.instruction_string.equals("JUMP") ||
//					instr.instruction_string.equals("MOVC") ||
//					instr.instruction_string.equals("JAL") ||
//					instr.instruction_string.equals("AND")||
//					instr.instruction_string.equals("OR")||
//					instr.instruction_string.equals("EX-OR"))
//			{
//				//flow dep
//				setDestRegInvalid();
//				// output dep
//				setDestRegOutputStatusInvalid();
//				setInstrResRegVals();				
//			}
//			else
//			{
//				if(instr.instruction_string.equals("HALT"))
//				{
//					//halt remove before
//					Halt_flush();
//					// set pc higher so that no instruction will be fetched.
//					pc=-1;
//				}
//				else
//				{
//					// flow dep
//					setDestRegInvalid();
//					// output dep
//					setDestRegOutputStatusInvalid();
//					
//					setInstrResRegVals();	
//					setPSWRegInvalid();
//
//
//				}
//
//
//			}
//
//		}
//	}
	

	
	public void changeRawInstr(Instruction_info instr)
	{
		instr.raw_instr=instr.instruction_string;
		if(instr.dest_registers.size()>0)
		{
			instr.raw_instr=instr.raw_instr+","+instr.dest_registers.get(0).reg_name;
		}
		if(instr.source_registers.size()>0)
		{
			for (AddrValPair source_reg :instr.source_registers)
			{
				instr.raw_instr=instr.raw_instr+","+source_reg.reg_name ;	
			}
			
		}
		if(instr.literal!=-99)
		{
			instr.raw_instr=instr.raw_instr+",#"+Integer.toString(instr.literal);
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
			isstalled=true;
			instr=null;
			return;
		}
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
	



}
