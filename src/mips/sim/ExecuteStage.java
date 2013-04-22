package mips.sim;

public class ExecuteStage extends Stage {
	
	public ExecuteStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		for (Instruction i : this.instructions) {
			i.executeInstruction();
		}
	}

}
