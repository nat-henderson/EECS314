package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.Memory;
import mips.sim.Register;
import mips.sim.RegisterFile;
import mips.sim.Word;

public abstract class ITypeInstruction extends Instruction {
	
	// protected access strikes again
	protected int immediate;
	protected int registerRt; // destination
	protected int registerRs; // source
	protected int opcode;

	public ITypeInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
		
		int bitmask = 0x0000FFFF; // mask bits 0-15
		int instructionWord = instruction.asInt();
		this.immediate = (instructionWord & bitmask);
		bitmask = 0x001F0000; // mask bits 16-20
		this.registerRt = (instructionWord & bitmask) >> 16;
		bitmask = 0x03E00000; // mask bits 21-25
		this.registerRs = (instructionWord & bitmask) >> 21;
		bitmask = 0xFC000000; // mask bits 26-31
		this.opcode = (instructionWord & bitmask) >> 26;
		
		this.inputRegisters.add(new Register(this.registerRs, null));
		this.outputRegisters.add(new Register(this.registerRt, null));
	}
}
