package mips.sim.instructions;

import mips.sim.Instruction;
import mips.sim.Memory;
import mips.sim.Register;
import mips.sim.RegisterFile;
import mips.sim.Word;
import mips.sim.MIPSSystem.StageType;

public abstract class IntegerMathInstruction extends Instruction {

	protected int function;
	protected int shiftAmount;
	protected int registerRd; // destination
	protected int registerRt; // source 1
	protected int registerRs; // source 2
	protected int opcode;
	
	public IntegerMathInstruction(Memory memory, RegisterFile regFile,
			Word instruction) {
		super(memory, regFile, instruction);

		int instructionWord = instruction.asInt();
		int bitmask = 0x0000003F; // mask bits 0-5
		this.function = instructionWord & bitmask;
		bitmask = 0x000007C0; // mask bits 6-10
		this.shiftAmount = (instructionWord & bitmask) >> 6;
		bitmask = 0x0000F800; // mask bits 11-15
		this.registerRd = (instructionWord & bitmask) >> 11;
		bitmask = 0x001F0000; // mask bits 16-20
		this.registerRt = (instructionWord & bitmask) >> 16;
		bitmask = 0x03E00000; // mask bits 21-25
		this.registerRs = (instructionWord & bitmask) >> 21;
		bitmask = 0xFC000000; // mask bits 26-31
		this.opcode = (instructionWord & bitmask) >> 26;
		
		this.inputRegisters.add(new Register(this.registerRt, null));
		this.inputRegisters.add(new Register(this.registerRs, null));
		this.outputRegisters.add(new Register(33, null));
		this.outputRegisters.add(new Register(34, null));
	}
	
	protected abstract long mathResult(int word1, int word2);

	@Override
	public void instructionDecode() {
		this.inputRegisters.clear();
		this.inputRegisters.add(this.regFile.getRegister(this.registerRt));
		this.inputRegisters.add(this.regFile.getRegister(this.registerRs));
	}

	@Override
	public void executeInstruction() {
		int one = this.inputRegisters.get(0).getWord().asInt();
		int two = this.inputRegisters.get(1).getWord().asInt();
		long result = mathResult(one, two);
		int low = (int)result;
		int high = (int)(result >>> 32);
		this.outputRegisters.clear();
		this.outputRegisters.add(new Register(33, new Word(high)));
		this.outputRegisters.add(new Register(34, new Word(low)));
	}

	@Override
	public void doMemory() {
		// OH GOD ALL THE ABSTRACTION
	}

	@Override
	public void writeback() {
		this.regFile.putRegister(33, this.outputRegisters.get(0).getWord());
		this.regFile.putRegister(34, this.outputRegisters.get(1).getWord());
	}
	
	public String toString() {
		return getInstructionName() + " " + lookup(this.registerRs) + " "
					+ lookup(this.registerRt);
	}
	
	public StageType getOutputReadyAfter() {
		return StageType.EX;
	}
	public StageType getInputNeededBefore() {
		return StageType.EX;
	}

}
