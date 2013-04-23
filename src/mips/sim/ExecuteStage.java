package mips.sim;

public class ExecuteStage extends Stage {
	
	public ExecuteStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		if (this.instructions.size() == numberOfCycles) {
			this.instructions.get(numberOfCycles - 1).executeInstruction();
		}
	}

}
