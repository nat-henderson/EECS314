package mips.sim;

import java.util.ArrayList;
import java.util.List;

public class MIPSSystem {
	private int programCounter;
	private static MIPSSystem system;
	private InstructionDecodeStage idStage;
	private ExecuteStage eStage;
	private MemoryStage mStage;
	private WritebackStage wStage;
	private Memory memory;
	
	public MIPSSystem() {
		this(new ArrayList<Instruction>(), 1,1,1,1, new Memory());
	}
	
	public MIPSSystem(List<Instruction> initialInstructions, int idSteps, int eSteps,
			int mSteps, int wbSteps, Memory memory) {
		this.programCounter = 0x40000000;
		this.memory = memory;
		this.idStage = new InstructionDecodeStage(idSteps);
		this.eStage = new ExecuteStage(eSteps);
		this.mStage = new MemoryStage(mSteps);
		this.wStage = new WritebackStage(wbSteps);
		int initCounter = programCounter;
		for (Instruction i : initialInstructions) {
			memory.setWord(initCounter, i);
			initCounter += 4;
		}
		system = this;
	}
	
	public void run(int steps) throws MemoryLocationNotInitializedException {
		for (int i = 0; i < steps; i++) {
			this.run();
		}
	}
	
	public static void changeProgramCounter(int newPC) {
		system.programCounter = newPC;
		system.flushAll();
	}
	
	public void flushAll() {
		this.idStage.flush();
		this.eStage.flush();
		this.mStage.flush();
		this.wStage.flush();
	}
	
	public void run() throws MemoryLocationNotInitializedException {
		Instruction nextInst = (Instruction)this.memory.getWord(programCounter);
		programCounter += 4;
		Instruction output = this.wStage.doExecute();
		Instruction memOut = this.mStage.doExecute();
		Instruction eOut = this.eStage.doExecute();
		Instruction idOut = this.idStage.doExecute();
		try{
			this.wStage.load(memOut);
			this.mStage.load(eOut);
			this.eStage.load(idOut);
			this.idStage.load(nextInst);
		}
		catch (DoubleLoadException e) {
			throw new RuntimeException(e);
		}
		if (output != null) {
			System.out.println("Output:  " + output.toString());
		}
	}
}