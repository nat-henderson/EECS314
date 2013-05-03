package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class MoveFromLowInstruction extends RTypeInstruction {

	public MoveFromLowInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		
		this.inputRegisters.clear();
		this.inputRegisters.add(this.regFile.getRegister(34));
	}

	@Override
	protected Word getResult() {
		return this.regFile.getRegister(34).getWord();
	}

	@Override
	public String getInstructionName() {
		return "MFHI";
	}
	
	@Override
	public void instructionDecode() {
		this.inputRegisters.clear();
		this.inputRegisters.add(this.regFile.getRegister(34));
	}

}
