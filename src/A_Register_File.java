import java.util.Enumeration;
import java.util.Hashtable;

public class A_Register_File{
	private Hashtable<String, Register> arch_registers;
              
   public A_Register_File()
   {
	   arch_registers= new Hashtable<String, Register>();
	   arch_registers.put("R0",new Register());
	   arch_registers.put("R1",new Register());
	   arch_registers.put("R2",new Register());
	   arch_registers.put("R3",new Register());
	   arch_registers.put("R4",new Register());
	   arch_registers.put("R5",new Register());
	   arch_registers.put("R6",new Register());
	   arch_registers.put("R7",new Register());
	   arch_registers.put("R8",new Register());
	   arch_registers.put("R9",new Register());
	   arch_registers.put("R10",new Register());
	   arch_registers.put("R11",new Register());
	   arch_registers.put("R12",new Register());
	   arch_registers.put("R13",new Register());
	   arch_registers.put("R14",new Register());
	   arch_registers.put("R15",new Register());
	   arch_registers.put("PSW",new PSW_Register());
   }
   
   public int getRegValuebyName(String reg_name)
   {
	   Register register= arch_registers.get(reg_name);
	   return register.val;
   }
   

   
   public void setRegValuebyName(String reg_name,int val_arg)
   {
	   Register register= arch_registers.get(reg_name);
	   register.val=val_arg;
   }
   
   
   public void setPSWStatus(int val)
   {
	   PSW_Register register= (PSW_Register) arch_registers.get("PSW");
	   register.special_status=register.special_status+val;

   }
   public int getPSWStatus()
   {
	   PSW_Register register= (PSW_Register) arch_registers.get("PSW");
	   return register.special_status;

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
