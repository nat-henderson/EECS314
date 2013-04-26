package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class AddImmediateUnsigned extends ITypeInstruction {

	public AddImmediateUnsigned(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		long one = (long)this.inputRegisters.get(0).getWord().asInt();
		return new Word((int)(one + (long)(this.immediate)));
	}

	@Override
	public String getInstructionName() {
		return "ADDIU";
	}

}
