package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.Memory;
import mips.sim.Register;
import mips.sim.RegisterFile;
import mips.sim.Word;

public abstract class RTypeInstruction extends Instruction {
	
	// protected access all over again
	protected int function;
	protected int shiftAmount;
	protected int registerRd; // destination
	protected int registerRt; // source 1
	protected int registerRs; // source 2
	protected int opcode;

	public RTypeInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
		
		int instructionWord = instruction.asInt();
		int bitmask = 0x0000003F; // mask bits 0-5
		this.function = instructionWord & bitmask;
		bitmask = 0x000007C0; // mask bits 6-10
		this.shiftAmount = (instructionWord & bitmask) >> 6;
		bitmask = 0x0000F800; // mask bits 11-15
		this.registerRd = (instructionWord & bitmask) >> 11;
		bitmask = 0x001F0000; // mask bits 16-20
		this.registerRt = (instructionWord & bitmask) >> 16;
		bitmask = 0x03E00000; // mask bits 21-25
		this.registerRs = (instructionWord & bitmask) >> 21;
		bitmask = 0xFC000000; // mask bits 26-31
		this.opcode = (instructionWord & bitmask) >> 26;
		
		this.outputRegisters.add(this.regFile.getRegister(this.registerRd));
	}
	
	@Override
	public void instructionDecode() {
		this.inputRegisters.clear();
		this.inputRegisters.add(this.regFile.getRegister(this.registerRt));
		this.inputRegisters.add(this.regFile.getRegister(this.registerRs));
	}
	
	@Override
	public void doMemory() {
		// nothing by default
	}
	
	@Override
	public void writeback() {
		this.regFile.putRegister(this.registerRd, this.outputRegisters.get(0).getWord());
	}
	
	protected abstract Word getResult();
	
	@Override
	public void executeInstruction() {
		this.outputRegisters.clear();
		Word result = this.getResult();
		this.outputRegisters.add(new Register(this.registerRd, result));
	}

}
