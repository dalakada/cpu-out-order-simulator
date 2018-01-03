import java.util.Queue;

public class Div3 extends Stage{
	
	public Div3(Queue<Instruction_info> q_in_arg, Queue<Instruction_info> q_out_arg) {
		super(q_in_arg, q_out_arg);
		name="Div3";
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
