public class AddrValPair{
    public String reg_name;
    public int reg_value;
    public boolean is_forwarded;
	public boolean is_catched_resources;

    public AddrValPair(String reg_name) { 
        this.reg_name= reg_name;
        this.reg_value = 0;
        this.is_catched_resources=false;
        
    }
    
    public String toString()
    {
    	return reg_name+" "+ reg_value ;
    }
}