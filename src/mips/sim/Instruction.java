package mips.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public abstract class Instruction extends Word {
	
	protected Memory memory;
	protected RegisterFile regFile;
	
	// oh no protected access
	public List<Register> inputRegisters;
	public List<Register> outputRegisters;
	
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
	
	public abstract String toString();
	
	public String lookup(int registerID) {
		for (Map.Entry<String, Integer> entry : InstructionBuilder.registerMap.entrySet()) {
			if (entry.getValue() == registerID) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	protected static long unsignedLongFromInt(int i) {
		long bitmask = 0x00000000FFFFFFFF;
		long temp = (long)i;
		return bitmask & temp; // undo sign extension
	}
}