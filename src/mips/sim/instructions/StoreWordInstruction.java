package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;
import mips.sim.MIPSSystem.StageType;

public class StoreWordInstruction extends ITypeInstruction {
	
	public StoreWordInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		return new Word(this.registerRs + this.immediate);
	}
	
	@Override
	public void doMemory() {
		this.memory.setWord(getResult().asInt(), 
				this.regFile.getRegister(this.registerRt).getWord());
	}

	@Override
	public String getInstructionName() {
		return "SW";
	}
	
	@Override
	public void executeInstruction() {
		// nothing doing
	}
	
	@Override
	public void writeback() {
		// nothing doing
	}
	public StageType getOutputReadyAfter() {
		return StageType.MEM;
	}
	public StageType getInputNeededBefore() {
		return StageType.EX;
	}

}
