import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class P_Register_File{
	private ArrayList<Physical_Reg> physical_registers;
	
	
   public P_Register_File()
   {
	   physical_registers= new ArrayList<Physical_Reg>();
	   
	   physical_registers.add(new Physical_Reg(0));
	   physical_registers.add(new Physical_Reg(1));
	   physical_registers.add(new Physical_Reg(2));
	   physical_registers.add(new Physical_Reg(3));
	   physical_registers.add(new Physical_Reg(4));
	   physical_registers.add(new Physical_Reg(5));
	   physical_registers.add(new Physical_Reg(6));
	   physical_registers.add(new Physical_Reg(7));
	   physical_registers.add(new Physical_Reg(8));
	   physical_registers.add(new Physical_Reg(9));
	   physical_registers.add(new Physical_Reg(10));
	   physical_registers.add(new Physical_Reg(11));
	   physical_registers.add(new Physical_Reg(12));
	   physical_registers.add(new Physical_Reg(13));
	   physical_registers.add(new Physical_Reg(14));
	   physical_registers.add(new Physical_Reg(15));
	   physical_registers.add(new Physical_Reg(16));
	   physical_registers.add(new Physical_Reg(17));
	   physical_registers.add(new Physical_Reg(18));
	   physical_registers.add(new Physical_Reg(19));
	   physical_registers.add(new Physical_Reg(20));
	   physical_registers.add(new Physical_Reg(21));
	   physical_registers.add(new Physical_Reg(22));
	   physical_registers.add(new Physical_Reg(23));
	   physical_registers.add(new Physical_Reg(24));
	   physical_registers.add(new Physical_Reg(25));
	   physical_registers.add(new Physical_Reg(26));
	   physical_registers.add(new Physical_Reg(27));
	   physical_registers.add(new Physical_Reg(28));
	   physical_registers.add(new Physical_Reg(29));
	   physical_registers.add(new Physical_Reg(30));
	   physical_registers.add(new Physical_Reg(31));
   }
   
   public int getRegValuebyName(String reg_name)
   {
	   String[] non_filtered=reg_name.split("P");
	   String reg_index=non_filtered[1];
	   int index= Integer.parseInt(reg_index);

	   Physical_Reg register= physical_registers.get(index);
	   return register.val;
   }
   

   
   // return meaning as follows:
   //0 means valid
   //1 means busy.
   public int getRegStatusbyName(String reg_name)
   {
	   String[] non_filtered=reg_name.split("P");
	   String reg_index=non_filtered[1];
	   int index= Integer.parseInt(reg_index);

	   Physical_Reg register= physical_registers.get(index);
	   return register.status;
   }   

   
   public void setRegValuebyName(String reg_name,int val_arg)
   {
	   String[] non_filtered=reg_name.split("P");
	   String reg_index=non_filtered[1];
	   int index= Integer.parseInt(reg_index);

	   Physical_Reg register= physical_registers.get(index);
	   register.val=val_arg;
   }
   
   
   //while renaming should be called for dest regs as follows:
   // reg.setRegStatusByName(physical_reg,+1)
   public void setRegStatusbyName(String reg_name,int val_arg)
   {
	   String[] non_filtered=reg_name.split("P");
	   String reg_index=non_filtered[1];
	   int index= Integer.parseInt(reg_index);

	   Physical_Reg register= physical_registers.get(index);
	   register.status=register.status+val_arg;
   }
   
   public String  getFreePhyRegMakeBusy()
   {
	   for (Physical_Reg phy_reg : physical_registers) {
		   if(phy_reg.is_free==true)
		   {
			   // make physical reg occupied
			   phy_reg.is_free=false;
			   // make physical reg invalid
			   phy_reg.status=phy_reg.status-1;
			   return phy_reg.name;
		   }
		   
	   }
	   
	   // to do check if there is no free phy regs.
	   return null;
	   
   }

	public void setPhysicalRegFree(String physical_reg_name) 
	{
	   for (Physical_Reg phy_reg : physical_registers) {
		   if(phy_reg.name.equals(physical_reg_name))
		   {
			   phy_reg.is_free=true;
		   }
		   
	   }	
		
	}
   

   
   
//   public void printRegisterFile()
//   {
//	   Enumeration items = registers.keys();
//	   while(items.hasMoreElements())
//	   {
//
//		  String strKey = (String)items.nextElement();
//		  if(strKey=="PSW")
//		  {
//			  PSW_Register reg = (PSW_Register) registers.get(strKey);
//			  if(reg.special_status==0)
//			  {
//				  System.out.println(strKey+" " +"VALID");  
//			  }
//			  else
//			  {
//				  System.out.println(strKey+" " +"INVALID");  
//
//			  }
//			  
//		  }
//		  else
//		  {
//			  Register reg= registers.get(strKey);
//			  if(reg.status==0)
//			  {
//				  System.out.println(strKey+" " +registers.get(strKey).val+"  "+"VALID");
//			  }
//			  else
//			  {
//				  System.out.println(strKey+" "+registers.get(strKey).val+"  " +"INVALID");  
//
//			  }
//
//		  }
//		  
//	   }
//
//
//   }
   
}
