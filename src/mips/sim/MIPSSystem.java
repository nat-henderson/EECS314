package mips.sim;

import java.util.ArrayList;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mips.sim.instructions.AddInstruction;

public class MIPSSystem {
	private int programCounter;
	private Map<Integer, Integer> registersInUse = new HashMap<Integer, Integer>();
	private static MIPSSystem system;
	private InstructionDecodeStage idStage;
	private ExecuteStage eStage;
	private MemoryStage mStage;
	private WritebackStage wStage;
	private Map<Integer, Instruction> instrMemory;
	private Map<StageType, List<StageType>> forwardingMap;
	private List<Instruction> stallsQueued = new LinkedList<Instruction>();
	private Memory memory;
	private RegisterFile registers;
	
	public static MIPSSystem getInstance() {
		if (system == null)
			system = new MIPSSystem();
		return system;
	}
	
	public Memory getMemory() {
		return memory;
	}
	
	public RegisterFile getRegFile() {
		return registers;
	}
	
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
		for (int i = 0; i < 32; i++) {
			this.registersInUse.put(i, 0);
		}
		system = this;
	}
	
	public int length() {
		return this.idStage.size() + this.eStage.size() + 
				this.mStage.size() + this.wStage.size();
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
		this.stallsQueued.clear();
	}
	
	public void run() {
		Instruction nextInst = null;
		if (this.stallsQueued.isEmpty()) {
			nextInst = this.instrMemory.get(programCounter);
			programCounter += 4;
		}
		Instruction output = this.wStage.doExecute();
		Instruction memOut = this.mStage.doExecute();
		Instruction eOut = this.eStage.doExecute();
		Instruction idOut = this.idStage.doExecute();

		if (output != null) {
			for (Register out : output.outputRegisters) {
				this.registersInUse.put(out.getId(), this.registersInUse.get(out.getId()) - 1);
			}
		}
		
		if (nextInst != null) {
			List<Integer> positionsAhead = new ArrayList<Integer>();
			List<Integer> viablePositionsAhead = getViablePositionsAhead();
			for (Register r : nextInst.inputRegisters) {
				int earliest = this.getEarliestInstructionPosition(r.getId());
				if (earliest > 0) {
					positionsAhead.add(earliest);
				}
			}
			int stalls;
			for (stalls = 0; stalls < this.length() + 1; stalls++) {
				if (checkIsViable(viablePositionsAhead, positionsAhead, stalls)) {
					break;
				}
			}
			System.out.println("Inserting " + stalls + " stalls");
			for (int i = 0; i < stalls; i++) {
				this.stallsQueued.add(null);
			}
			
			this.stallsQueued.add(nextInst);
			
			for (Register out : nextInst.outputRegisters) {
				this.registersInUse.put(out.getId(), this.registersInUse.get(out.getId()) + 1);
			}
		}
		else if (this.stallsQueued.isEmpty()) {
			this.stallsQueued.add(null);
		}
		try{
			this.wStage.load(memOut);
			this.mStage.load(eOut);
			this.eStage.load(idOut);
			this.idStage.load(stallsQueued.remove(0));
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
	
	private boolean checkIsViable(List<Integer> viablePositionsAhead,
			List<Integer> positionsAhead, int stalls) {
		System.out.println("Ahead:  " + positionsAhead.toString());
		for (int pos : positionsAhead) {
			if (!viablePositionsAhead.contains(pos + stalls)) {
				return false;
			}
			System.out.println(pos + stalls);
		}
		System.out.println(viablePositionsAhead);
		System.out.println(positionsAhead);
		return true;
	}

	private List<Integer> getViablePositionsAhead() {
		List<Integer> viablePos = new LinkedList<Integer>();
		if (forwardingMap.get(StageType.EX).contains(StageType.EX)) {
			viablePos.add(eStage.size());
		}
		if (forwardingMap.get(StageType.MEM).contains(StageType.EX)) {
			viablePos.add(mStage.size() + eStage.size());
		}
		if (forwardingMap.get(StageType.MEM).contains(StageType.MEM)) {
			viablePos.add(mStage.size());
		}
		// 2 is magical: it's the number 1 + 1.  1 for all instructions are 1 ahead
		// of the one in front of them, and one for instruction fetch which we don't
		// model as a stage.
		for (int i = this.length() + 2; i < this.length() * 3; i++) {
			viablePos.add(i);
		}
		System.out.println("Viable:  " + viablePos.toString());
		return viablePos;
	}

	private int getEarliestInstructionPosition(int id) {
		int counter = 0;
		for (Instruction i : this.idStage.instructions) {
			counter++;
			if (i == null) {
				continue;
			}
			for (Register out : i.outputRegisters) {
				if (out.getId() == id) {
					return counter;
				}
			}
		}
		for (Instruction i : this.eStage.instructions) {
			counter++;
			if (i == null) {
				continue;
			}
			for (Register out : i.outputRegisters) {
				if (out.getId() == id) {
					return counter;
				}
			}
		}
		for (Instruction i : this.mStage.instructions) {
			counter++;
			if (i == null) {
				continue;
			}
			for (Register out : i.outputRegisters) {
				if (out.getId() == id) {
					return counter;
				}
			}
		}
		for (Instruction i : this.wStage.instructions) {
			counter++;
			if (i == null) {
				continue;
			}
			for (Register out : i.outputRegisters) {
				if (out.getId() == id) {
					return counter;
				}
			}
		}
		return -1;
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
		MIPSSystem ms = new MIPSSystem(instList, 1,1,1,1);
		ms.setupForwarding(StageType.EX, StageType.EX);
		ms.run(30);
	}
}