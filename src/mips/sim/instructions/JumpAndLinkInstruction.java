package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.MIPSSystem;
import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class JumpAndLinkInstruction extends Instruction {

	private int address;
	
	public JumpAndLinkInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
		
		int bitmask = 0x3FFFFFFF;
		this.address = instruction.asInt() & bitmask;
	}

	@Override
	public void instructionDecode() {
		// nothing doing
	}

	@Override
	public void executeInstruction() {
		this.outputRegisters.clear();
		this.regFile.putRegister(31, MIPSSystem.getProgramCounter() + 8);
		MIPSSystem.jumpProgramCounter(this.address); // for great justice
	}

	@Override
	public void doMemory() {
		// nothing doing
	}

	@Override
	public void writeback() {
		// nothing doing
	}

	@Override
	public String getInstructionName() {
		return "JAL"; // http://en.wikipedia.org/wiki/Agent_J
	}
	
	public String toString() {
		return getInstructionName() + " " + this.address;
	}

}
