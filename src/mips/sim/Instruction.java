package mips.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class Instruction {
	
	private Memory memory;
	private RegisterFile regFile;
	private Word instruction;
	
	// oh no protected access
	protected List<Register> inputRegisters;
	protected List<Register> outputRegisters;
	
	public Instruction(Memory memory, RegisterFile regFile, Word instruction) {
		this.memory = memory;
		this.regFile = regFile;
		this.instruction = instruction;
		this.inputRegisters = new ArrayList<Register>();
		this.outputRegisters = new ArrayList<Register>();
	}
	
	// no reason this would ever change
	public Word toWord() {
		return instruction;
	}

	public List<Register> getInputRegisters() {
		return new ArrayList<Register>(this.inputRegisters);
	}
	
	public List<Register> getOutputRegisters() {
		return new ArrayList<Register>(this.outputRegisters);
	}

	public void acceptForwardedRegister(Register r) {
		Register register = null;
		for (ListIterator<Register> iterator = this.inputRegisters.listIterator();
				iterator.hasNext(); register = iterator.next()) {
			if (register.getId() == r.getId()) {
				iterator.add(r);
				iterator.remove();
			}
		}
	}
	
	// accesses that register file and loads the inputRegisters and outputRegisters array.
	public abstract void instructionDecode();
	
	public abstract void executeInstruction();
		
	public abstract void doMemory();
	
	public abstract void writeback();
	
}