package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class NorInstruction extends RTypeInstruction {

	public NorInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		int one = this.inputRegisters.get(0).getWord().asInt();
		int two = this.inputRegisters.get(1).getWord().asInt();
		return new Word(~(one | two));
	}

	@Override
	public String getInstructionName() {
		return "NOR";
	}

}
