package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class MultInstruction extends IntegerMathInstruction {

	public MultInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected long mathResult(int word1, int word2) {
		return (long)word1 * (long)word2;
	}

	@Override
	public String getInstructionName() {
		return "MULT";
	}

}
