package mips.sim;

public class WritebackStage extends Stage{
	
	public WritebackStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		if (this.instructions.size() == numberOfCycles) {
			this.instructions.get(numberOfCycles - 1).writeback();
		}
	}

}
