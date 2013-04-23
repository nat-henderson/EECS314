package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class AddInstruction extends RTypeInstruction {

	public AddInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
	}

	@Override
	protected Word getResult() {
		int one = this.inputRegisters.get(0).getWord().asInt();
		int two = this.inputRegisters.get(1).getWord().asInt();
		return new Word(one + two);
	}

}
