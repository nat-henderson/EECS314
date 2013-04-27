package mips.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class Instruction extends Word {
	
	protected Memory memory;
	protected RegisterFile regFile;
	
	// oh no protected access
	protected List<Register> inputRegisters;
	protected List<Register> outputRegisters;
	
	public Instruction(Memory memory, RegisterFile regFile, Word instruction) {
		super(instruction.asByteArray());
		this.memory = memory;
		this.regFile = regFile;
		this.inputRegisters = new ArrayList<Register>();
		this.outputRegisters = new ArrayList<Register>();
	}
	
	public List<Register> getInputRegisters() {
		return new ArrayList<Register>(this.inputRegisters);
	}
	
	public List<Register> getOutputRegisters() {
		return new ArrayList<Register>(this.outputRegisters);
	}

	public void acceptForwardedRegister(Register r) {
		Register register = null;
		ListIterator<Register> iterator = this.inputRegisters.listIterator();
		while (iterator.hasNext()){
			register = iterator.next();
			if (register != null && register.getId() == r.getId()) {
				iterator.set(r);
			}
		}
	}
	
	// accesses that register file and loads the inputRegisters and outputRegisters array.
	public abstract void instructionDecode();
	
	public abstract void executeInstruction();
		
	public abstract void doMemory();
	
	public abstract void writeback();
	
	/**
	 * 
	 * @return the name of the instruction (ADD, ADDI, ect)
	 */
	public abstract String getInstructionName();

	public static Instruction fromWord(Memory memory2, RegisterFile regFile2,
			Word nextInst) {
		// this needs to return the right KIND of instruction... woooo
		// this will be a MASSIVE pain in the ass to write.
		return null;
	}	
	
	public String toString() {
		return "Instruction:  " + this.inputRegisters.toString() + "   " + this.outputRegisters.toString();
	}
	
	protected static long unsignedLongFromInt(int i) {
		long bitmask = 0x00000000FFFFFFFF;
		long temp = (long)i;
		return bitmask & temp; // undo sign extension
	}
}