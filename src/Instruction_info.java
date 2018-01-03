import java.util.ArrayList;

public class Instruction_info {
	public String raw_instr;
	public String instruction_string;
	public ArrayList<AddrValPair> source_registers;
	public ArrayList<AddrValPair> dest_registers;
	public int target_memory_addr;
	public String target_memory_data;
	public int literal;
	public int instr_pc;
	public int instr_no;
	public boolean isflushed;
	public int req_sources;
	public int catched_sources;
	
	public Instruction_info(String instr_str, 
			ArrayList<AddrValPair> source_registers
			,ArrayList<AddrValPair> dest_registers
			,int target_memory_addr
			,String target_memory_data
			,int literal,String raw_instr_arg,int instr_pc, int instr_no)
	{
		instruction_string=instr_str;
		this.source_registers= source_registers;
		this.dest_registers=dest_registers;
		this.target_memory_addr=target_memory_addr;
		this.target_memory_data = target_memory_data;
		this.literal=literal;
		this.instr_pc=instr_pc;
		raw_instr=raw_instr_arg;
		this.instr_no=instr_no;
		this.isflushed=false;
		this.req_sources=this.source_registers.size();
		this.catched_sources=0;
	}
}
