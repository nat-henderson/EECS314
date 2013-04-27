package mips.sim;

import java.util.ArrayList;
import java.util.List;

public abstract class Stage {
	private List<Stage> forwardTo;
	protected List<Instruction> instructions;
	protected int numberOfCycles;
	protected boolean hasHadInsertThisCycle;
	
	public Stage() {
		this(1);
	}
	
	public Stage(int nCycles) {
		this.numberOfCycles = nCycles;
		this.forwardTo = new ArrayList<Stage>();
		this.instructions = new ArrayList<Instruction>(nCycles);
		this.hasHadInsertThisCycle = false;
		for (int i = 0; i < nCycles; i++) {
			this.instructions.add(null);
		}
	}
	
	protected abstract void execute();
	
	public Instruction doExecute() {
		this.execute();
		if (!this.hasHadInsertThisCycle) {
			try {
				this.load(null);
			} catch (DoubleLoadException e) {
				// this can't happen for obvious reasons
				throw new RuntimeException();
				// but we should see it if it ever does, I guess.
			}
		}
		this.hasHadInsertThisCycle = false;
		if (this.instructions.size() < this.numberOfCycles) {
			return null;
		}
		Instruction inst = this.instructions.remove(this.numberOfCycles);
		if (inst == null) {
			return inst;
		}
		for (Stage forwardTo : this.forwardTo) {
			for (Register forward: inst.getOutputRegisters()) {
				// we can only forward to the beginning of a stage
				forwardTo.acceptForwardedRegister(forward);
			}
		}
		return inst;
	}
	
	protected void acceptForwardedRegister(Register forward) {
		// before a value is computed, the word is null.
		if (forward.getWord() == null) {
			return;
		}
		
		if (this.instructions.size() > 0 && this.instructions.get(0) != null) {
			this.instructions.get(0).acceptForwardedRegister(forward);
		}
		// loop runs backwards because we alter the size but still only need to keep moving forward
		for (int i = this.instructions.size(); i > 1 ; i--) {
			int stalls = 0;
			boolean needsForwarding = false;
			for (Register r : this.instructions.get(i).inputRegisters) {
				if (r.getId() == forward.getId()) {
					needsForwarding = true;
				}
			}
			if (needsForwarding) {
				// we need to add stalls equal to the difference between the current
				// location and the start of the stage.
				while (stalls < i) {
					this.instructions.add(i + 1, null);
					stalls ++;
				}
			}
		}
	}

	public Instruction getCurrentInstruction(int idx) {
		if (idx < this.numberOfCycles) {
			return this.instructions.get(idx);	
		}
		else {
			throw new RuntimeException("Invalid index");
		}
	}
	
	public void load(Instruction i) throws DoubleLoadException{
		if (this.hasHadInsertThisCycle) {
			throw new DoubleLoadException();
		}
		this.instructions.add(0, i);
		this.hasHadInsertThisCycle = true;
	}
	
	public void stall(int numStalls, int whereToStall) {
		for (int i = 0; i < numStalls; i++) {
			this.instructions.add(whereToStall, null);
		}
	}
	
	public void flush() {
		this.instructions.clear();
		this.hasHadInsertThisCycle = false;
	}
	
	public void acceptForwardingConfiguration(Stage stage) {
		this.forwardTo.add(stage);
	}
}
