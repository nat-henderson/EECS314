package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class BranchOnNotEqualInstruction extends ITypeInstruction {

	public BranchOnNotEqualInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		int one = this.inputRegisters.get(0).getWord().asInt();
		int two = this.inputRegisters.get(1).getWord().asInt();
		return new Word((one == two ? 1 : 0));
	}

	@Override
	public String getInstructionName() {
		return "BNE";
	}
	
	@Override
	public void writeback() {
		// nothing doing
	}
	
	@Override
	public void executeInstruction() {
		if (this.getResult().asInt() == 1) { // if not equal
			mips.sim.MIPSSystem.moveProgramCounter(this.immediate);
		}
	}

}
