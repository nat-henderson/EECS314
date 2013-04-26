package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class BranchOnEqualInstruction extends ITypeInstruction {

	public BranchOnEqualInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
	}

	@Override
	protected Word getResult() {
		int one = this.inputRegisters.get(0).getWord().asInt();
		int two = this.inputRegisters.get(1).getWord().asInt();
		return new Word((one == two ? 1 : 0));
	}

	@Override
	public String getInstructionName() {
		return "BEQ";
	}
	
	@Override
	public void writeback() {
		// nothing doing
	}
	
	@Override
	public void executeInstruction() {
		if (this.getResult().asInt() == 0) { // if not equal
			mips.sim.MIPSSystem.moveProgramCounter(this.immediate);
		}
	}

}
