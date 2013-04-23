package mips.sim;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
	private int programCounter;
	private static Pipeline pipeline;
	private InstructionDecodeStage idStage;
	private ExecuteStage eStage;
	private MemoryStage mStage;
	private WritebackStage wStage;
	private RegisterFile regFile;
	private Memory memory;
	
	public Pipeline() {
		this(new ArrayList<Instruction>(), 1,1,1,1);
	}
	
	public Pipeline(List<Instruction> initialInstructions, int idSteps, int eSteps,
			int mSteps, int wbSteps) {
		this.programCounter = 0x40000000;
		this.idStage = new InstructionDecodeStage(idSteps);
		this.eStage = new ExecuteStage(eSteps);
		this.mStage = new MemoryStage(mSteps);
		this.wStage = new WritebackStage(wbSteps);
		this.regFile = new RegisterFile();
		this.memory = new Memory();
		int initCounter = programCounter;
		for (Instruction i : initialInstructions) {
			this.memory.setWord(initCounter, i.toWord());
			initCounter += 4;
		}
		pipeline = this;
	}
	
	public void run(int steps) throws MemoryLocationNotInitializedException {
		for (int i = 0; i < steps; i++) {
			this.run();
		}
	}
	
	public static void changeProgramCounter(int newPC) {
		pipeline.programCounter = newPC;
		pipeline.flushAll();
	}
	
	public void flushAll() {
		this.idStage.flush();
		this.eStage.flush();
		this.mStage.flush();
		this.wStage.flush();
	}
	
	public void run() throws MemoryLocationNotInitializedException {
		Word nextInst = this.memory.getWord(programCounter);
		programCounter += 4;
		Instruction next = Instruction.fromWord(this.memory, this.regFile, nextInst);
		Instruction output = this.wStage.doExecute();
		Instruction memOut = this.mStage.doExecute();
		Instruction eOut = this.eStage.doExecute();
		Instruction idOut = this.idStage.doExecute();
		try{
			this.wStage.load(memOut);
			this.mStage.load(eOut);
			this.eStage.load(idOut);
			this.idStage.load(next);
		}
		catch (DoubleLoadException e) {
			throw new RuntimeException(e);
		}
		if (output != null) {
			System.out.println("Output:  " + output.toString());
		}
	}
}