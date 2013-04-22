package mips.sim;

public class MemoryStage extends Stage {

	public MemoryStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		for (Instruction i : this.instructions) {
			i.doMemory();
		}
	}

}
