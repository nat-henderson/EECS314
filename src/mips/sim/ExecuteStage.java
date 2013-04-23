package mips.sim;

public class ExecuteStage extends Stage {
	
	public ExecuteStage(int nSteps) {
		super(nSteps);
	}
	
	protected void execute() {
		if (this.instructions.size() == numberOfCycles + 1) {
			Instruction inst = this.instructions.get(numberOfCycles);
			if (inst != null) {
				inst.executeInstruction();
			}
			System.out.println(this.instructions.toString());
		}
	}

}
