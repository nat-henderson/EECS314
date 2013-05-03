package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.Word;
import mips.sim.MIPSSystem.StageType;

public class StoreByteInstruction extends ITypeInstruction {

	public StoreByteInstruction(Memory memory, RegisterFile regFile,
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
		byte b = (byte)this.regFile.getRegister(this.registerRt).getWord().asByteArray()[0];
		this.memory.setByte(getResult().asInt(), b);
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
