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
	
	public abstract int getStageTimeInNs();
	
	public int getMaxFrequencyInHz() {
		return (int)(1.0e9 / (getStageTimeInNs())) * this.numberOfCycles;
	}
	
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
	}
	
	public int size() {
		return this.numberOfCycles;
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
	
	public void flush() {
		this.instructions.clear();
		this.hasHadInsertThisCycle = false;
	}
	
	public void acceptForwardingConfiguration(Stage stage) {
		this.forwardTo.add(stage);
	}
}
