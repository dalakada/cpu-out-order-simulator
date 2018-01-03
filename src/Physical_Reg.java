
public class Physical_Reg {
	public int val;
	public String name;
	public int status;
	public boolean is_free;
	
	public Physical_Reg(int name)
	{
		val = 0;
		String name_num_portion=Integer.toString(name);
		this.name="P"+name_num_portion;
		//0 means valid.. (flow dep)
		status=0;
		is_free=true;
	}
}
