package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.MIPSSystem;
import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;
import mips.sim.MIPSSystem.StageType;

public class JumpInstruction extends Instruction {

	private int address;
	
	public JumpInstruction(Memory memory, RegisterFile regFile, Word instruction) {
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
		return "J"; // http://en.wikipedia.org/wiki/Agent_J
	}
	
	public StageType getOutputReadyAfter() {
		return StageType.EX;
	}
	public StageType getInputNeededBefore() {
		return StageType.EX;
	}
	
	public String toString() {
		return getInstructionName() + " " + this.address;
	}

}
