
public class Data_Memory {
	
	private int[] memory;
	
	public Data_Memory()
	{
		memory= new int[4000];
	}

	public int getMemoryValuebyAddress(int addrs_arg)
	{
		int value= memory[addrs_arg];
		return value;
	}
	
	public void setMemoryValuebyAddress(int addrs_arg, int value_to_set)
	{
		memory[addrs_arg]=value_to_set;
	}
	
	public void printDataMemory(int var)
	{
		for(int i = 0; i < var; i++)
		{
		    System.out.println(i+" "+memory[i]);
		}
	}
}
