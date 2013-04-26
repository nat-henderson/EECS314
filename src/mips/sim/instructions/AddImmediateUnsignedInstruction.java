package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class AddImmediateUnsignedInstruction extends ITypeInstruction {

	public AddImmediateUnsignedInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		long one = Instruction.unsignedLongFromInt(this.inputRegisters.get(0).getWord().asInt());
		return new Word((int)(one + Instruction.unsignedLongFromInt(this.immediate)));
	}

	@Override
	public String getInstructionName() {
		return "ADDIU";
	}

}
