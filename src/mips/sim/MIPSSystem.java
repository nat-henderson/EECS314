package mips.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mips.sim.instructions.AddInstruction;

public class MIPSSystem {
	private int programCounter;
	private static MIPSSystem system;
	private InstructionDecodeStage idStage;
	private ExecuteStage eStage;
	private MemoryStage mStage;
	private WritebackStage wStage;
	private Map<Integer, Instruction> instrMemory;
	
	public MIPSSystem() {
		this(new ArrayList<Instruction>(), 1,1,1,1);
	}
	
	public MIPSSystem(List<Instruction> initialInstructions, int idSteps, int eSteps,
			int mSteps, int wbSteps) {
		this.programCounter = 0x40000000;
		this.idStage = new InstructionDecodeStage(idSteps);
		this.eStage = new ExecuteStage(eSteps);
		this.mStage = new MemoryStage(mSteps);
		this.wStage = new WritebackStage(wbSteps);
		this.instrMemory = new HashMap<Integer, Instruction>();
		int initCounter = programCounter;
		for (Instruction i : initialInstructions) {
			instrMemory.put(initCounter, i);
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
		Instruction nextInst = this.instrMemory.get(programCounter);
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
		else {
			System.out.println("Null output");
		}
	}
	
	public static void main(String[] args) throws MemoryLocationNotInitializedException {
		Memory mem = new Memory();
		RegisterFile regFile = new RegisterFile();
		List<Instruction> instList = new ArrayList<Instruction>();
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		MIPSSystem ms = new MIPSSystem(instList, 1,1,1,1);
		ms.run(15);
	}
}