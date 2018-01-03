import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.*;


public class Test 
{

	public static void main(String[] args) throws IOException 
	{

        // no args
        if (args.length < 1)
        {
            System.exit(0);
        }


        String myPath = Paths.get(".").toAbsolutePath().normalize().toString();
        Path path = Paths.get(myPath + "/" + args[0]);
        System.out.println(myPath);
        // file doesn't exist
        if (!Files.exists(path)) 
        {
            System.exit(0);
        }
		Instruction_info[] code_memory ;
	    Queue<Instruction_info> dummy_queue;
	    Queue<Instruction_info> fetch_in ;
	    Queue<Instruction_info> fetch_out;
	    Queue<Instruction_info> decode_out ;
	    Queue<Instruction_info> decode_out_mul ;
	    Queue<Instruction_info> decode_out_div;
	    ArrayList<Instruction_info> forwarded_list;
	    Queue<Instruction_info> div1_out;
	    Queue<Instruction_info> div2_out;
	    Queue<Instruction_info> div3_out;
	    Queue<Instruction_info> div4_out;
	    Queue<Instruction_info> iq_out;
	    Queue<Instruction_info> mul1_out;
	    Queue<Instruction_info> mul2_out;
	    
	    Queue<Instruction_info> mul1_in, div1_in, intfu_in;
	    Queue<Instruction_info> execute_out;
	    Queue<Instruction_info> memory_in;

	    Queue<Instruction_info> memory_out;
	    Queue<Instruction_info> write_out=null;
	    
	    Queue <AddrValPair> forward_bus;
//	    Queue <PSW_Forward_Message> forward_psw_bus;


	    A_Register_File a_reg_file=null;
	    P_Register_File p_reg_file;
	    Data_Memory data_mem=null;
	    
	    Code_Memory_Manager code_mem_mngr;
	    Fetch fetch;
		Decode decode;
		IQ iq;
		Execute execute;
		Mul1 mul1;
		Mul2 mul2;
		Div1 div1;
		Div2 div2;
		Div3 div3;
		Div4 div4;
//		Memory memory;
//		Write write;
		ROB rob = null;
		RAT rat;
		
		ArrayList<Stage> stage_holder=null;

        while (true)
        {
            System.out.println("Input your command, available commands: Initialize, Simulate, Display, Quit ");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("Initialize"))
            {
        		code_memory = new Instruction_info[4000];
        		dummy_queue= new LinkedList<>();
        		fetch_in = new LinkedList<>();
        		fetch_out = new LinkedList<>();
        		decode_out = new LinkedList<>();
        		iq_out= new LinkedList<>();
        		div1_out= new LinkedList<>();
        		div2_out= new LinkedList<>();
        		div3_out= new LinkedList<>();
        		div4_out= new LinkedList<>();
        		mul1_out= new LinkedList<>();
        		mul2_out= new LinkedList<>();
        		execute_out = new LinkedList<>();
        		memory_in = new LinkedList<>();
        		memory_out = new LinkedList<>();
        		write_out = new LinkedList<>();
        	    mul1_in= new LinkedList<>();
        	    div1_in= new LinkedList<>();
        	    intfu_in= new LinkedList<>();
        		a_reg_file= new A_Register_File();
        		p_reg_file= new P_Register_File();
        	    data_mem= new Data_Memory();
        	    
//        	    forward_bus = new LinkedList<>();
//        	    forward_psw_bus = new LinkedList<>();
        	    
        		rat= new RAT(a_reg_file, p_reg_file);
        		rob= new ROB(rat);
        	    
        	    code_mem_mngr= new Code_Memory_Manager(dummy_queue,fetch_in);
        	    fetch = new Fetch(fetch_in,fetch_out);
        		decode = new Decode(fetch_out,decode_out,rat);
        		decode.setROB(rob);
        		// write_out for new IQ is dummy.
        		iq= new IQ(decode_out, write_out);
        		forwarded_list= new ArrayList<Instruction_info>();
        		iq.setForwardedBus(forwarded_list);
        		iq.setMulDivIntFUin(mul1_in, div1_in, intfu_in);
        		decode.setIQholder(iq);
        		
        		execute = new Execute(intfu_in,memory_in);
        		execute.setForwardedBus(forwarded_list);
        		execute.setPRFBus(p_reg_file);
        		execute.setROB(rob);
        		
        		div1 = new Div1(div1_in,div1_out);
        		div2 = new Div2(div1_out,div2_out);
        		div3 = new Div3(div2_out,div3_out);
        		div4 = new Div4(div3_out,memory_in);
        		mul1= new Mul1(mul1_in,mul1_out);
        		mul2= new Mul2(mul1_out,memory_in);
//        		memory = new Memory(memory_in,memory_out,reg_file,data_mem);
//        		write = new Write(memory_out,write_out,reg_file,data_mem);
        		
        		stage_holder = new ArrayList<Stage>();

        		stage_holder.add(code_mem_mngr);
        		stage_holder.add(fetch);
        		stage_holder.add(decode);
        		stage_holder.add(iq);
        		stage_holder.add(execute);
        		stage_holder.add(mul1);
        		stage_holder.add(mul2);
        		stage_holder.add(div1);
        		stage_holder.add(div2);
        		stage_holder.add(div3);
        		stage_holder.add(div4);
//        		stage_holder.add(memory);
//        		stage_holder.add(write);
        		
                FileReader fr = new FileReader(myPath + "/" + args[0]);        		
                BufferedReader br = new BufferedReader(fr);
                ArrayList<String> inputList = new ArrayList<String>();

                String readStr;
                
                ArrayList<String[]> array_holder = new ArrayList<String[]>();

                while((readStr = br.readLine()) != null)
                {
                     inputList.add(readStr);
                     String[] infoTemp = readStr.split(",");
                     array_holder.add(infoTemp);
                 
                }
            	ArrayList<Instruction_info> instr_holder= new ArrayList<Instruction_info>();

        		for (int i = 0; i < array_holder.size(); i++) 
        		{
        			String whole_instr = String.join(",", array_holder.get(i));
                	String instruction_string="";
                	ArrayList<AddrValPair> source_registers = new ArrayList<AddrValPair>();
                	ArrayList<AddrValPair> dest_registers= new ArrayList<AddrValPair>();
                	int target_memory_addr= 0;
                	String target_memory_data= "";
                	int literal=-99;
                	int instr_pc=0;
            		int instr_no=i;
                	
                	if(array_holder.get(i)[0].equals("MOVC"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		//literal
                		String[] literal_with_split = array_holder.get(i)[2].split("#");
                		literal=Integer.parseInt(literal_with_split[1]);
                	}
                	if(array_holder.get(i)[0].equals("ADD"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
                	 }
                	if(array_holder.get(i)[0].equals("SUB"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
                	 }
                	if(array_holder.get(i)[0].equals("OR"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
                	 }
                	
                	if(array_holder.get(i)[0].equals("AND"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
                	 }
                	if(array_holder.get(i)[0].equals("EX-OR"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
                	 }
                	
                	if(array_holder.get(i)[0].equals("LOAD"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		//literal
                		String[] literal_with_split = array_holder.get(i)[3].split("#");
                		literal=Integer.parseInt(literal_with_split[1]);
                	 }
                	
                	if(array_holder.get(i)[0].equals("STORE"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		//literal
                		String[] literal_with_split = array_holder.get(i)[3].split("#");
                		literal=Integer.parseInt(literal_with_split[1]);
                	 }
                	if(array_holder.get(i)[0].equals("JUMP"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		source_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		//literal
                		String[] literal_with_split = array_holder.get(i)[2].split("#");
                		literal=Integer.parseInt(literal_with_split[1]);
                	}
                	if(array_holder.get(i)[0].equals("BZ")||array_holder.get(i)[0].equals("BNZ"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		System.out.println("********************************");
                		//literal
                		String[] literal_with_split = array_holder.get(i)[1].split("#");
                		literal=Integer.parseInt(literal_with_split[1]);
                		instr_pc=(i*4)+4000;
                	}
                	if(array_holder.get(i)[0].equals("MUL"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
                	}
                	if(array_holder.get(i)[0].equals("HALT"))
                	{
                		instruction_string=array_holder.get(i)[0];
                	}
                	if(array_holder.get(i)[0].equals("DIV"))
                	{
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
                	 }
                	if(array_holder.get(i)[0].equals("JAL"))
                	{
                		System.out.println("here");
                		instruction_string=array_holder.get(i)[0];
                		//source register
                		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
                		System.out.println("********************************");
                		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
                		instr_pc=(i*4)+4000;

                		String[] literal_with_split = array_holder.get(i)[3].split("#");
                		literal=Integer.parseInt(literal_with_split[1]);                	 
                	
                	}
                	      
                	            	
        	        Instruction_info Instr_info = new Instruction_info(instruction_string, source_registers, dest_registers,target_memory_addr, target_memory_data, literal,whole_instr,instr_pc,instr_no);
        	        code_memory[i]=Instr_info;
        	     
        	    }

        		code_mem_mngr.setInstrMemory(code_memory);
        	    
        		//for flush set decode and fetch's input for exe.
        		execute.setFetch_Decode_Queue(fetch_in, fetch_out);
        		decode.setFetchQueue(fetch_in);
        		
        		// forwarding queue setting.
//        		mul2.setForwardingBus(forward_bus);
//        		mul2.setForwardingPSWBus(forward_psw_bus);
//        		
//        		decode.setForwardingBus(forward_bus);
//        		decode.setForwardingPSWBus(forward_psw_bus);
//        		
//        		execute.setForwardingBus(forward_bus);
//        		execute.setForwardingPSWBus(forward_psw_bus);
//        		
//        		div4.setForwardingBus(forward_bus);
//        		div4.setForwardingPSWBus(forward_psw_bus);
//        		
//        		decode.setMul2_in(mul1_out);
            }
            else if(command.contains("Simulate"))
            {
                String[] parts = command.split(" ");
                if (parts.length != 2)
                {
                    System.out.println("Put number of cycles as well..");
                    continue;
                }

                int numOfCycles =Integer.parseInt(parts[1]) ;
                
				boolean halt_flag=false;
        	    for (int i=0; i<numOfCycles; i++)
        	    {
        	    	System.out.println(" ");
    				System.out.println(i+1);
    				
        	    	for(int j = stage_holder.size() - 1; j >= 0; j--)
        	    	{
            			stage_holder.get(j).execute();
        		    	stage_holder.get(j).append_q_out();
        		    }
        	    	
    				rob.execute();

	      	    	for (int x=0; x<stage_holder.size(); x++)
	    		    {
	    		    	stage_holder.get(x).printStatus();
	    		    }
	        	}
				
//        	    for (int i=0; i<numOfCycles; i++)
//        	    {
//        	    	if(halt_flag)
//        	    	{
//        	    		break;
//        	    	}
//        	    	System.out.println(" ");
//    				System.out.println(i+1);
//
//        	    	for(int j = stage_holder.size() - 1; j >= 0; j--)
//        	    	{
//            			stage_holder.get(j).execute();
//        		    	write_out.poll();
//        		    	stage_holder.get(j).append_q_out();
//        		    	
//            			if(stage_holder.get(j).name.equals("Write"))
//            			{
//            				Write write_current = (Write) stage_holder.get(j);
//            				halt_flag=write_current.is_halt;
//            				if(halt_flag)
//            				{
//            					break;
//            				}
//            			}
//
//        		    }
//        	    	
//          	    	for (int x=0; x<stage_holder.size(); x++)
//        		    {
//        		    	stage_holder.get(x).printStatus();
//        		    }
//        	    }
                
            }
            else if(command.equals("Display"))
            {
            	System.out.println("*********Data Memory********");
        	    data_mem.printDataMemory(100);
        	    System.out.println("*********Registers********");
//    	    	reg_file.printRegisterFile();

                
            }
            else if(command.equals("Quit"))
            {
                System.exit(0);
            }
            // undefined args
            else
            {
                System.out.println("Undefined command.");
            }
        }
    
   


        

        	
        // array_holder holds your lines 
        // each line has 3 thing :
        // [0] ==> instruction
        // [1] ==> register
        // .... etc

	    

	    

	    	
		
	}

}
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Queue;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//public class Test {
//
//	public static void main(String[] args) throws IOException 
//	{
//		Instruction_info[] code_memory = new Instruction_info[4000];
//	    Queue<Instruction_info> dummy_queue= new LinkedList<>();
//	    Queue<Instruction_info> fetch_in = new LinkedList<>();
//	    Queue<Instruction_info> fetch_out = new LinkedList<>();
//	    Queue<Instruction_info> decode_out = new LinkedList<>();
//	    Queue<Instruction_info> decode_out_mul = new LinkedList<>();
//	    Queue<Instruction_info> mul1_out= new LinkedList<>();
//	    Queue<Instruction_info> mul2_out= new LinkedList<>();
//	    Queue<Instruction_info> execute_out = new LinkedList<>();
//	    Queue<Instruction_info> memory_out = new LinkedList<>();
//	    Queue<Instruction_info> write_out = new LinkedList<>();
//	    
//	    Register_File reg_file= new Register_File();
//	    Data_Memory data_mem= new Data_Memory();
//	    
//	    
//	    Code_Memory_Manager code_mem_mngr= new Code_Memory_Manager(dummy_queue,fetch_in, reg_file,data_mem);
//	    Fetch fetch = new Fetch(fetch_in,fetch_out, reg_file,data_mem);
//		Decode decode = new Decode(fetch_out,decode_out,reg_file,data_mem,decode_out_mul);
//		Execute execute = new Execute(decode_out,execute_out,reg_file,data_mem);
//		Mul1 mul1= new Mul1(decode_out_mul,mul1_out,reg_file,data_mem);
//		Mul2 mul2= new Mul2(mul1_out,mul2_out,reg_file,data_mem);
//		Memory memory = new Memory(execute_out,memory_out,reg_file,data_mem,mul2_out);
//		Write write = new Write(memory_out,write_out,reg_file,data_mem);
//		
//		ArrayList<Stage> stage_holder = new ArrayList<Stage>();
//
//		stage_holder.add(code_mem_mngr);
//		stage_holder.add(fetch);
//		stage_holder.add(decode);
//		stage_holder.add(execute);
//		stage_holder.add(mul1);
//		stage_holder.add(mul2);
//		stage_holder.add(memory);
//		stage_holder.add(write);
//		
//        FileReader fr = new FileReader("/home/dalakada/Downloads/input.txt");
//        BufferedReader br = new BufferedReader(fr);
//        ArrayList<String> inputList = new ArrayList<String>();
//
//        String readStr;
//        
//        ArrayList<String[]> array_holder = new ArrayList<String[]>();
//
//        while((readStr = br.readLine()) != null){
//             inputList.add(readStr);
////		             System.out.println(readStr);
//
//             String[] infoTemp = readStr.split(",");
//             array_holder.add(infoTemp);
//
//        }
//        
////		        String[] stringArr = inputList.toArray(new String[0]);
////		        System.out.println(Arrays.toString(stringArr));
//        	
//        // array_holder holds your lines 
//        // each line has 3 thing :
//        // [0] ==> instruction
//        // [1] ==> register
//        // .... etc
//    	ArrayList<Instruction_info> instr_holder= new ArrayList<Instruction_info>();
//
//		for (int i = 0; i < array_holder.size(); i++) 
//		{
//			String whole_instr = String.join(",", array_holder.get(i));
//        	String instruction_string="";
//        	ArrayList<AddrValPair> source_registers = new ArrayList<AddrValPair>();
//        	ArrayList<AddrValPair> dest_registers= new ArrayList<AddrValPair>();
//        	int target_memory_addr= 0;
//        	String target_memory_data= "";
//        	int literal=-99;
//            	
//        	if(array_holder.get(i)[0].equals("MOVC"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		//literal
//        		String[] literal_with_split = array_holder.get(i)[2].split("#");
//        		literal=Integer.parseInt(literal_with_split[1]);
//        	}
//        	if(array_holder.get(i)[0].equals("ADD"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
//        	 }
//        	if(array_holder.get(i)[0].equals("SUB"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
//        	 }
//        	if(array_holder.get(i)[0].equals("OR"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
//        	 }
//        	
//        	if(array_holder.get(i)[0].equals("AND"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
//        	 }
//        	if(array_holder.get(i)[0].equals("EX-OR"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
//        	 }
//        	
//        	if(array_holder.get(i)[0].equals("LOAD"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		//literal
//        		String[] literal_with_split = array_holder.get(i)[3].split("#");
//        		literal=Integer.parseInt(literal_with_split[1]);
//        	 }
//        	
//        	if(array_holder.get(i)[0].equals("STORE"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		//literal
//        		String[] literal_with_split = array_holder.get(i)[3].split("#");
//        		literal=Integer.parseInt(literal_with_split[1]);
//        	 }
//        	if(array_holder.get(i)[0].equals("JUMP"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		source_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		//literal
//        		String[] literal_with_split = array_holder.get(i)[2].split("#");
//        		literal=Integer.parseInt(literal_with_split[1]);
//        	}
//        	if(array_holder.get(i)[0].equals("BZ")||array_holder.get(i)[0].equals("BNZ"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		System.out.println("********************************");
//        		//literal
//        		String[] literal_with_split = array_holder.get(i)[1].split("#");
//        		literal=Integer.parseInt(literal_with_split[1]);
//        	}
//        	if(array_holder.get(i)[0].equals("MUL"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        		//source register
//        		dest_registers.add(new AddrValPair(array_holder.get(i)[1]));
//        		System.out.println("********************************");
//        		source_registers.add(new AddrValPair(array_holder.get(i)[2]));
//        		source_registers.add(new AddrValPair(array_holder.get(i)[3]));
//        	}
//        	if(array_holder.get(i)[0].equals("HALT"))
//        	{
//        		instruction_string=array_holder.get(i)[0];
//        	}
//        	            	
//	        Instruction_info Instr_info = new Instruction_info(instruction_string, source_registers, dest_registers,target_memory_addr, target_memory_data, literal,whole_instr);
//	        code_memory[i]=Instr_info;
//	     
//	    }
//
//		code_mem_mngr.setInstrMemory(code_memory);
//	    
//		//for flush set decode and fetch's input for exe.
//		execute.setFetch_Decode_Queue(fetch_in, fetch_out);
//	    
//	    for (int i=0; i<100; i++)
//	    {
//
//	    	for(int j = stage_holder.size() - 1; j >= 0; j--){
//    			stage_holder.get(j).execute();
//		    	write_out.poll();
//		    }
//	    	System.out.println(i);
//
//	    	for (int x=0; x<stage_holder.size(); x++)
//		    {
//		    	stage_holder.get(x).append_q_out();
//		    	stage_holder.get(x).printStatus();
//
//		    }
//
//	    	System.out.println("********************************");
//	    	reg_file.printRegisterFile();
//	    	System.out.println("********************************");
//	    }
//	    
//	    data_mem.printDataMemory();	    
//
//	    	
//		
//	}
//
//}

