package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.MIPSSystem;
import mips.sim.Memory;
import mips.sim.Register;
import mips.sim.RegisterFile;
import mips.sim.Word;
import mips.sim.MIPSSystem.StageType;

public class JumpAndLinkInstruction extends Instruction {

	private int address;
	
	public JumpAndLinkInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
		
		int bitmask = 0x3FFFFFFF;
		this.address = instruction.asInt() & bitmask;
		this.outputRegisters.add(new Register(31, null));
	}

	@Override
	public void instructionDecode() {
		// nothing doing
	}

	@Override
	public void executeInstruction() {
		this.outputRegisters.clear();
		this.outputRegisters.add(new Register(31, new Word(MIPSSystem.getProgramCounter() + 8)));
		MIPSSystem.jumpProgramCounter(this.address); // for great justice
	}
	
	public StageType getOutputReadyAfter() {
		return StageType.EX;
	}
	public StageType getInputNeededBefore() {
		return StageType.EX;
	}

	@Override
	public void doMemory() {
		// nothing doing
	}

	@Override
	public void writeback() {
		this.regFile.putRegister(31, this.outputRegisters.get(0).getWord());
	}

	@Override
	public String getInstructionName() {
		return "JAL"; // http://en.wikipedia.org/wiki/Agent_J
	}
	
	public String toString() {
		return getInstructionName() + " " + this.address;
	}

}
