package mips.sim;

public class MemoryStage extends Stage {

	public MemoryStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		if (this.instructions.size() == numberOfCycles) {
			this.instructions.get(numberOfCycles - 1).doMemory();
		}
	}

}
