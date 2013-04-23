package mips.sim;

public class MemoryStage extends Stage {

	public MemoryStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		if (this.instructions.size() == numberOfCycles) {
			Instruction inst = this.instructions.get(numberOfCycles - 1);
			if (inst != null) {
				inst.doMemory();
			}
		}
	}

}
