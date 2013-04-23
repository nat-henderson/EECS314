package mips.sim;

public class InstructionDecodeStage extends Stage {
	
	public InstructionDecodeStage(int nSteps) {
		super(nSteps);
	}

	@Override
	protected void execute() {
		if (this.instructions.size() == numberOfCycles) {
			Instruction inst = this.instructions.get(numberOfCycles - 1);
			if (inst != null) {
				inst.instructionDecode();
			}
		}
	}

}
