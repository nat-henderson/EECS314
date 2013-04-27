package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class LoadUpperImmediateInstruction extends ITypeInstruction {

	public LoadUpperImmediateInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		int result = this.immediate << 16;
		return new Word(result);
	}

	@Override
	public String getInstructionName() {
		return "LUI";
	}

}
