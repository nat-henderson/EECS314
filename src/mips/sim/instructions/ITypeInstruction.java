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
		
		// sign extend the immediate
		bitmask = 0x8000; // extract bit 15
		int sign = this.immediate & bitmask;
		if (sign == 0x800) { // if negative number, extend sign
			this.immediate |= 0xffff0000;
		}
	}
	
	protected abstract Word getResult();
	
	public int getImmediate(){
	    return immediate;
	}
	
	@Override
	public void instructionDecode() {
		this.inputRegisters.clear();
		this.inputRegisters.add(this.regFile.getRegister(this.registerRs));
	}
	
	@Override
	public void doMemory() {
		// load/store will override
	}
	
	@Override
	public void writeback() {
		this.regFile.putRegister(this.registerRt, this.outputRegisters.get(0).getWord());
	}
	
	@Override
	public void executeInstruction() {
		this.outputRegisters.clear();
		Word result = this.getResult();
		this.outputRegisters.add(new Register(this.registerRt, result));
	}
}
