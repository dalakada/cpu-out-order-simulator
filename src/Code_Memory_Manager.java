import java.util.Queue;

public class Code_Memory_Manager extends Stage{
	private Instruction_info[] memory; 
	

	public Code_Memory_Manager(Queue<Instruction_info> q_in_arg, 
			Queue<Instruction_info> q_out_arg) 
	{
		super(q_in_arg, q_out_arg);
		// TODO Auto-generated constructor stub
	}
	public void doInstr()
	{
		if(instr==null)
		{
			return;
		}

	}
	
	public int map_mem_to_index(int mem_addr)
	{
		int a = mem_addr-4000;
		int index = a/4;
		return index;
	}
	
	public void append_q_out()
	{

		// if it processed something.
		if(instr!=null)
		{	
			q_out.add(instr);
		}			

		
	}
		
	public void pop_q_in()
	{
		// could be, data or null.
		if(pc==-1)
		{
			//halt
			instr=null;
			
		}
		else 
		{
			if(q_out.size()==0)
			{
				try {
					int index=map_mem_to_index(pc);
					instr=memory[index];
					pc=pc+4;
				}
				catch(ArrayIndexOutOfBoundsException exception) {
				    System.out.println("End of instruction memory...");
				}
			}
			else
			{
				instr=null;
			}

		}
	}
	
	public void setInstrMemory(Instruction_info[] memory_arg)
	{
		memory=memory_arg;
	}
	
	public void printStatus()
	{
		System.out.println("");

	}
}
