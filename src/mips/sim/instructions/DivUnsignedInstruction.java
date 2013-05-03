package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class DivUnsignedInstruction extends IntegerMathInstruction {

	public DivUnsignedInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected long mathResult(int word1, int word2) {
		long word1Long = 0x00000000FFFFFFFFL & word1;
		long word2Long = 0x00000000FFFFFFFFL & word2;
		int divres = (int)(word2Long / word1Long);
		int modres = (int)(word2Long % word1Long);
		long lowLong = 0x00000000FFFFFFFFL & divres;
		long highLong = (0x00000000FFFFFFFFL & modres) << 32;
		return lowLong & highLong;
	}

	@Override
	public String getInstructionName() {
		return "DIVU";
	}

}
