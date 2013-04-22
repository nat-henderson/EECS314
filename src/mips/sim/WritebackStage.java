package mips.sim;

public class WritebackStage extends Stage{
	
	public WritebackStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		for (Instruction i : this.instructions) {
			i.writeback();
		}
	}

}
