package mips.sim;

public class WritebackStage extends Stage{
	
	public WritebackStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		if (this.instructions.size() == numberOfCycles + 1) {
			Instruction inst = this.instructions.get(numberOfCycles);
			if (inst != null) {
				inst.writeback();
			}
		}
	}
	public int getStageTimeInNs() {
		return 250;
	}
}
