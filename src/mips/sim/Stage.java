package mips.sim;

import java.util.ArrayList;
import java.util.List;

public abstract class Stage {
	private List<Stage> forwardTo;
	private List<Instruction> instructions;
	private int numberOfCycles;
	private boolean hasHadInsertThisCycle;
	
	public Stage(int nCycles) {
		this.numberOfCycles = nCycles;
		this.forwardTo = new ArrayList<Stage>();
		this.instructions = new ArrayList<Instruction>(nCycles);
		this.hasHadInsertThisCycle = false;
	}
	
	protected abstract void execute();
	
	public Instruction doExecute() {
		this.execute();
		if (!this.hasHadInsertThisCycle) {
			try {
				this.load(null);
			} catch (DoubleLoadException e) {
				// this can't happen for obvious reasons
			}
		}
		this.hasHadInsertThisCycle = false;
		if (this.instructions.size() < this.numberOfCycles) {
			return null;
		}
		Instruction inst = this.instructions.remove(this.numberOfCycles - 1);
		if (inst == null) {
			return inst;
		}
		for (Stage forwardTo : this.forwardTo) {
			for (Register forward: inst.getOutputRegisters()) {
				// we can only forward to the beginning of a stage
				forwardTo.getCurrentInstruction(0).acceptForwardedRegister(forward);
			}
		}
		return inst;
	}
	
	public Instruction getCurrentInstruction(int idx) {
		return this.instructions.get(idx);
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
