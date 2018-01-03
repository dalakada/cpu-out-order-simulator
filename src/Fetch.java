import java.util.Queue;

public class Fetch extends Stage{
	
	public Fetch(Queue<Instruction_info> q_in_arg, Queue<Instruction_info> q_out_arg) {
		super(q_in_arg, q_out_arg);
		name="Fetch";
	}
	
	public void doInstr()
	{
		if(instr==null)
		{
			return;
		}
	}
	




}
