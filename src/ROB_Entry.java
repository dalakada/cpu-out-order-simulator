
public class ROB_Entry {

	public Instruction_info instr ;
	public boolean is_finished;
	
	public ROB_Entry(Instruction_info instr)
	{
		this.instr=instr;
		is_finished=false;
	}
}
