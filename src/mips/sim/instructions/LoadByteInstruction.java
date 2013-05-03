package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.MemoryLocationNotInitializedException;
import mips.sim.RegisterFile;
import mips.sim.Word;
import mips.sim.MIPSSystem.StageType;

public class LoadByteInstruction extends ITypeInstruction {

	private byte loadedByte;
	
	public LoadByteInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Word getResult() {
		int one = this.inputRegisters.get(0).getWord().asInt();
		return new Word(one + this.immediate);
	}

	@Override
	public String getInstructionName() {
		return "LBU";
	}
	
	@Override
	public void executeInstruction() {
		// nothing doing
	}
	
	@Override
	public void doMemory() {
		try {
			loadedByte = this.memory.getByte(getResult().asInt());
		} catch (MemoryLocationNotInitializedException e) {
			this.memory.setByte(getResult().asInt(), (byte) 0);
			loadedByte = (byte) 0;
		}
	}
	
	@Override
	public void writeback() {
		int bitmask = 0x000000FF;
		int result = bitmask & (int)( loadedByte );
		this.regFile.putRegister(this.registerRt, result);
	}
	
	public StageType getOutputReadyAfter() {
		return StageType.MEM;
	}
	public StageType getInputNeededBefore() {
		return StageType.EX;
	}

}
