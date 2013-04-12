package mips.sim;

public abstract class Instruction {
	
	private Memory memory;
	private RegisterFile regFile;
	
	public Instruction(Memory memory, RegisterFile regFile, Word instruction) {
		this.memory = memory;
		this.regFile = regFile;
	}
	
	public abstract Word toWord();
	
	public abstract void instructionDecode();

	public abstract void executeInstruction() throws RegisterNeedsForwardingException;
		
	public abstract void doMemory() throws RegisterNeedsForwardingException;
	
	public abstract void writeback();

}
