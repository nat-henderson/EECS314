package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class MoveFromHighInstruction extends RTypeInstruction {

	public MoveFromHighInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		
		this.inputRegisters.clear();
		this.inputRegisters.add(this.regFile.getRegister(33));
	}

	@Override
	protected Word getResult() {
		return this.regFile.getRegister(33).getWord();
	}

	@Override
	public String getInstructionName() {
		return "MFHI";
	}
	
	@Override
	public void instructionDecode() {
		this.inputRegisters.clear();
		this.inputRegisters.add(this.regFile.getRegister(33));
	}

}
