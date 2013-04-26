package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class SetLessThanUnsignedInstruction extends RTypeInstruction {

	public SetLessThanUnsignedInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		long one = Instruction.unsignedLongFromInt(this.inputRegisters.get(0).getWord().asInt());
		long two = Instruction.unsignedLongFromInt(this.inputRegisters.get(1).getWord().asInt());
		return new Word((two < one ? 1 : 0));
	}

	@Override
	public String getInstructionName() {
		return "SLTU";
	}

}
