package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class DivInstruction extends IntegerMathInstruction {

	public DivInstruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected long mathResult(int word1, int word2) {
		int low = word2 / word1;
		int high = word2 % word1;
		long lowLong = 0x00000000FFFFFFFFL & low;
		long highLong = (0x00000000FFFFFFFFL & high) << 32;
		return lowLong & highLong;
	}

	@Override
	public String getInstructionName() {
		return "DIV";
	}

}
