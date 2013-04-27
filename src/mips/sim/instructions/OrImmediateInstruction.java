package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class OrImmediateInstruction extends ITypeInstruction {

	public OrImmediateInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		int one = this.inputRegisters.get(0).getWord().asInt();
		return new Word(one | this.immediate);
	}

	@Override
	public String getInstructionName() {
		return "ORI";
	}

}
