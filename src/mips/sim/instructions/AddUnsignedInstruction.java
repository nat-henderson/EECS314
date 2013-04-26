package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class AddUnsignedInstruction extends RTypeInstruction {

	public AddUnsignedInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		long one = (long)this.inputRegisters.get(0).getWord().asInt();
		long two = (long)this.inputRegisters.get(1).getWord().asInt();
		return new Word((int)(one + two));
	}

	@Override
	public String getInstructionName() {
		return "ADDU";
	}

}
