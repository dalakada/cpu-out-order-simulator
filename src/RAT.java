import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class RAT {
	private A_Register_File a_reg_file;
	private P_Register_File p_reg_file;
	private HashMap<String,String>latest_table;
	public RAT(A_Register_File a_reg_arg, P_Register_File p_reg_arg)
	{
		// index is String Arch Reg Names
		// Values can be following:
		//		String Physical Reg Names
		//		NULL
		latest_table= new HashMap<String,String>();
		a_reg_file=a_reg_arg;
		p_reg_file=p_reg_arg;
		
		// all entries points to arch files at first
		// if null
		//    then go directly to ARF
		// else
		//	  access via PRF
		latest_table.put("R0", null);
		latest_table.put("R1", null);
		latest_table.put("R2", null);
		latest_table.put("R3", null);
		latest_table.put("R4", null);
		latest_table.put("R5", null);
		latest_table.put("R6", null);
		latest_table.put("R7", null);
		latest_table.put("R8", null);
		latest_table.put("R9", null);
		latest_table.put("R10", null);
		latest_table.put("R11", null);
		latest_table.put("R12", null);
		latest_table.put("R13", null);
		latest_table.put("R14", null);
		latest_table.put("R15", null);
		
	}
	public boolean isPhysicalLatest(String physical_reg_name)
	{
        Set<String> arch_regs = latest_table.keySet();
        for(String arch_reg: arch_regs){
        	if(latest_table.get(arch_reg)==physical_reg_name)
        	{
        		return true;
        	}
        }
        return false;
	}
	public boolean isSourceCommitted(String arch_reg_name)
	{
		String physical_reg_name= latest_table.get(arch_reg_name);

		// there is no associated physical reg
		// fetch directly from ARF
		if(physical_reg_name==null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int getCommittedRegValue(String arch_reg_name)
	{
		int value= a_reg_file.getRegValuebyName(arch_reg_name);
		return value;
	}
	
	public boolean isPhysicalValid(String arch_reg_name)
	{
		String physical_reg_name= latest_table.get(arch_reg_name);
		int p_reg_status=p_reg_file.getRegStatusbyName(physical_reg_name);
		
		if(p_reg_status==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public int getValidPhysicalRegValue(String arch_reg_name)
	{
		String physical_reg_name= latest_table.get(arch_reg_name);

		int value= p_reg_file.getRegValuebyName(physical_reg_name);
		return value;
	}
	
	public String getBusyPhysicalRegName(String arch_reg_name)
	{
		String physical_reg_name= latest_table.get(arch_reg_name);

		return physical_reg_name;
	
	}
	
	public String setDestArchRegPhysicalReg(String arch_reg_name)
	{
		String physical_reg_name=p_reg_file.getFreePhyRegMakeBusy();
		
		// associate reg name with physical reg name.
		latest_table.put(arch_reg_name,physical_reg_name);
		return physical_reg_name;
	}
	
	public void setPhysicalRegFree(String physical_reg_name)
	{
		p_reg_file.setPhysicalRegFree(physical_reg_name);
	}
	public void setCommitedValtoArchReg(Instruction_info instr_to_commit)
	{
        Set<String> arch_regs = latest_table.keySet();
        for(String arch_reg: arch_regs){
        	if(latest_table.get(arch_reg)==instr_to_commit.dest_registers.get(0).reg_name)
        	{
        		int physical_reg_value_to_commit=instr_to_commit.dest_registers.get(0).reg_value;
        		a_reg_file.setRegValuebyName(arch_reg,physical_reg_value_to_commit );
        	}
        }
	}
	public void setRATEntryNull(String physical_reg_name)
	{
        Set<String> arch_regs = latest_table.keySet();
        for(String arch_reg: arch_regs){
        	if(latest_table.get(arch_reg)==physical_reg_name)
        	{
        		latest_table.put(arch_reg,null);
        	}
        }
	}
}
