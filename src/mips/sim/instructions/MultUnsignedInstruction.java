package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class MultUnsignedInstruction extends IntegerMathInstruction {

	public MultUnsignedInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected long mathResult(int word1, int word2) {
		long word1Long = 0x00000000FFFFFFFFL & word1;
		long word2Long = 0x00000000FFFFFFFFL & word2;
		return word1Long * word2Long;
	}

	@Override
	public String getInstructionName() {
		return "MULTU";
	}

}
