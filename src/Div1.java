import java.util.Queue;

public class Div1 extends Stage{
	
	public Div1(Queue<Instruction_info> q_in_arg, Queue<Instruction_info> q_out_arg) {
		super(q_in_arg, q_out_arg);
		name="Div1";
	}
	
	public void doInstr()
	{
		if(instr==null)
		{
			return;
		}
	}
	
	//cannot be stalled
	public boolean isStall() {
		return false;
	}



}
