package mips.sim.instructions;

import mips.sim.MIPSSystem;
import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class JumpRegisterInstruction extends RTypeInstruction {

	public JumpRegisterInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		return this.getInputRegisters().get(1).getWord();
	}

	@Override
	public String getInstructionName() {
		return "JR";
	}
	
	@Override
	public void executeInstruction() {
		this.outputRegisters.clear();
		MIPSSystem.changeProgramCounter(this.getResult().asInt());
	}

}
