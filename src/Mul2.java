import java.util.ArrayList;
import java.util.Queue;
public class Mul2 extends Stage{
	
	boolean isstalled=false;
	AddrValPair jal_to_forward_dest_reg;
	ArrayList<Instruction_info> forwarded_list;
	P_Register_File p_reg_file;
	ROB rob;
	
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
	
	public Mul2(Queue<Instruction_info> q_in_arg, Queue<Instruction_info> q_out_arg) {
		super(q_in_arg, q_out_arg);
		name="Mul2";
	}
	//cannot be stalled
	public boolean isStall() {
		return false;
	}
	public void doInstr()
	{
		if(instr==null)
		{
			return;
		}
		
		else
		{
			if(instr.instruction_string.equals("MUL"))
			{
				//calculate AND
				
				
				int val1=instr.source_registers.get(0).reg_value;
				int val2=instr.source_registers.get(1).reg_value;
				int multiplied_val= val1*val2;
				//store it in dest_reg_data
				instr.dest_registers.get(0).reg_value=multiplied_val;
				
				forwarded_list.add(instr);
				// make physical register valid
				p_reg_file.setRegStatusbyName(instr.dest_registers.get(0).reg_name,1);
				p_reg_file.setRegValuebyName(instr.dest_registers.get(0).reg_name, instr.dest_registers.get(0).reg_value);
				
				// set instruction finished in rob
				rob.finished_instrs.add(instr);

				
			}
		}
	}
	public void append_q_out()
	{

		// do nothing

		
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
