# cpu-out-order-simulator
Data Structures:

1.)Register File Register File is a class which acts as a holder for register types. It has a hashtable for registers to mimic register_file in cpu. User can access register values and their availability by register names. Register file also has methods to print it’s register values and their availability.

2.) Register Register is class which mimics registers in CPU. (R0-R15 plus PSW.) Has two fields, its’ value and status (valid,invalid)

3.)Instruction_info Instruction_info is class to mimics instructions. Instructions flow through stages in cpu. Fields are: raw_instr (representing raw instruction) instricton_string(string for instruction names) acts like opcode. source_registers (source register holder which is arraylist for AddrValPair which is basically tuple) dest_registers (destination register holder which is arraylist for AddrValPair which is basically tuple) target_memory_addr (int which holds mem address to access) target_memory_data(string which holds data to write on memory) literal (int who holds literal) instr_pic (address of instruction in instruction memory)

4.)Code_Memory_Manager Acts as code memory manager to calculate what’s going to supply for fetch. For each cycle it supplies instruction for fetch based on PC value. Field: code_memory(array of Instruction_info which is basically bunch of instructions)

5.)Code Memory It’s a field in Code_Memory_Manager which described above.

6.) Data Memory It’s class for acting as memory. It’s accessed by Memory stage in CPU. Field: memory (array of integers)

7.) CPU Stages

7.a) Stage (Abstract )

It’s abstract class for other stage types, (Fetch, Decode, Execute(ADD),Mul1,Mul2,Mem,Write) Each stage has connection between consecutive stages by queues. By holding queue I decoupled the responsibility of passing instruction to queue logic. Even though it’s queue there is no concept of buffer (any time queue size is 1) Basic Flow for Stages (Generic)

public void execute()
{
	// if not stall
	if(!(isStall()))
	{
		pop_q_in();
		doInstr();
	}
	
	else
	{
		instr=null;
		return;
	}
}
First each stage checks if there is need for stall which means do not process anything, do not fetch from previous stage and do not produce output for next stage.

Receive from input queue which is output queue of the previous stage. Do required processing. Put instruction into output queue which is input queue for the next stage.

Fetch: Fetch instruction from instruction memory based on PC. Can be stalled. Decode: Sets Destination Registers Invalid by accessing Register_file. Sets PSW Register Invalid if instruction requires. Sets Instruction Resource regs values by accessing Register_file. Sets halt procedure by flushing fetch. Decides which stage to send instruction based on looking at opcode (either mul1 or execute (which is add)) Execute(ADD) Does required calculation related instruction it process. Process branches flushes. Mul1: Passes mul instruction to mul2 Mul2: Does multiplcation. Memory: Does required mem operation for STORE and LOAD Write: Sets destination registers valid. Sets PSW register valid if instructions requrires by accessing Register_file. Sets PswRegVal whether it’s 0 or not. Sets destination registers value.

Stall Mechanism for Flow Dependence: It’s done in decode. Decode checks source registers to see if they are valid. If valid not stall. If not valid stall.

Flush Mechanism for JUMP and Branches: It’s done in execute by checking opcode. If it’s branch call whether to do branch or not decided in decode by checking PSW register’s value.

Flush Mechanism for HALT: It’s done in decode. Flush previous instructions. Decode sets pc of fetch to -1 to not fetch further instructions.
