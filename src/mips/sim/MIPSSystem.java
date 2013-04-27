package mips.sim;

import java.util.ArrayList;
import java.util.FormatterClosedException;
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
	private Map<StageType, List<StageType>> forwardingMap;
	
	public enum StageType {
		ID, EX, MEM, WB;
	}
	
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
		this.forwardingMap = new HashMap<StageType, List<StageType>>();
		this.forwardingMap.put(StageType.ID, new ArrayList<StageType>());
		this.forwardingMap.put(StageType.EX, new ArrayList<StageType>());
		this.forwardingMap.put(StageType.MEM, new ArrayList<StageType>());
		this.forwardingMap.put(StageType.WB, new ArrayList<StageType>());
		system = this;
	}
	
	public void setupForwarding(StageType forwardFrom, StageType forwardTo) {
		Stage from;
		Stage to;
		if (forwardFrom == StageType.ID) {
			from = this.idStage;
		} else if (forwardFrom == StageType.EX) {
			from = this.eStage;
		} else if (forwardFrom == StageType.MEM) {
			from = this.mStage;
		} else {
			from = this.wStage;
		}
		if (forwardTo == StageType.ID) {
			to = this.idStage;
		} else if (forwardTo == StageType.EX) {
			to = this.eStage;
		} else if (forwardTo == StageType.MEM) {
			to = this.mStage;
		} else {
			to = this.wStage;
		}
		from.acceptForwardingConfiguration(to);
		this.forwardingMap.get(forwardFrom).add(forwardTo);
	}
	
	public void run(int steps) {
		for (int i = 0; i < steps; i++) {
			this.run();
		}
	}
	
	public static void changeProgramCounter(int newPC) {
		system.programCounter = newPC;
		system.flushAll();
	}
	
	public static void moveProgramCounter(int numInstructions) {
		system.programCounter += 4 * numInstructions;
		system.flushAll();
	}
	
	public static void jumpProgramCounter(int newPcLowerBits) {
		int bitmask = 0xFC000000;
		int newPcUpperBits = system.programCounter & bitmask;
		system.programCounter = newPcUpperBits | newPcLowerBits;
		system.flushAll();
	}
	
	public static int getProgramCounter() {
		return system.programCounter;
	}
	
	public void flushAll() {
		this.idStage.flush();
		this.eStage.flush();
		this.mStage.flush();
		this.wStage.flush();
	}
	
	public void run() {
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
			System.out.println("RegValue:" + output.outputRegisters.get(0).getWord().asInt());
		}
		else {
			System.out.println("Null output");
		}
	}
	
	public static void main(String[] args) {
		Memory mem = new Memory();
		RegisterFile regFile = new RegisterFile();
		regFile.putRegister(0, 1);
		List<Instruction> instList = new ArrayList<Instruction>();
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		instList.add(new AddInstruction(mem, regFile, new Word(0)));
		System.out.println(instList);
		MIPSSystem ms = new MIPSSystem(instList, 1,1,1,1);
		ms.setupForwarding(StageType.EX, StageType.EX);
		ms.run(15);
	}
}