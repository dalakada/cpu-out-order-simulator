import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class ROB {
	
	public Queue<ROB_Entry> ROB_QUEUE;
	public ArrayList<Instruction_info> finished_instrs;
	RAT rat;

	
	public ROB (RAT rat) {

		ROB_QUEUE=new LinkedList<>();
		this.rat=rat;
		finished_instrs=new ArrayList<Instruction_info>();
	}

	public void createROBEntry(Instruction_info instr_to_add)
	{
		ROB_QUEUE.add(new ROB_Entry(instr_to_add));
	}
	public void execute()
	{
		setFinished();
		commit();
	}
	public void setFinished()
	{
		// set instrcs finished.
		for (Instruction_info instr_in_queue : finished_instrs)
		{
			for (ROB_Entry rob_entry: ROB_QUEUE)
			{
				// find match dest regs
				if(rob_entry.instr.dest_registers.get(0).reg_name.equals(instr_in_queue.dest_registers.get(0).reg_name))
				{
					rob_entry.is_finished=true;
				}
			}
		}

		finished_instrs.clear();
	} 
	
	public void commit()
	{
		if(ROB_QUEUE.size()>0)
		{
			try {
			while(ROB_QUEUE.peek().is_finished==true)
			{
				String to_commit_physical_name=ROB_QUEUE.peek().instr.dest_registers.get(0).reg_name;
				// is physical in the rat?
				if(rat.isPhysicalLatest(to_commit_physical_name))
				{
					rat.setPhysicalRegFree(to_commit_physical_name);
					rat.setCommitedValtoArchReg(ROB_QUEUE.peek().instr);
					rat.setRATEntryNull(to_commit_physical_name);
				}

				System.out.println("Comit :" +ROB_QUEUE.peek().instr.raw_instr);
				ROB_QUEUE.poll();
			}
			}
			catch (Exception e) 
			{
				return;
			}
		}

	}

}
