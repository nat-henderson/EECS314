package mips.sim.instructions;

import mips.sim.Memory;
import mips.sim.MemoryLocationNotInitializedException;
import mips.sim.RegisterFile;
import mips.sim.Word;

public class LoadWordInstruction extends ITypeInstruction {
	
	private Word loadedWord;

	public LoadWordInstruction(Memory memory, RegisterFile regFile,
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
		return "LW";
	}
	
	@Override
	public void executeInstruction() {
		// nothing doing
	}
	
	@Override
	public void doMemory() {
		try {
			loadedWord = this.memory.getWord(getResult().asInt());
		} catch (MemoryLocationNotInitializedException e) {
			Word zero = new Word(0);
			this.memory.setWord(getResult().asInt(), zero);
			loadedWord = zero;
		}
	}
	
	@Override
	public void writeback() {
		this.regFile.putRegister(this.registerRt, loadedWord.asByteArray());
	}

}
