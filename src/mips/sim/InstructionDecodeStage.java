package mips.sim;

public class InstructionDecodeStage extends Stage {
	
	public InstructionDecodeStage(int nSteps) {
		super(nSteps);
	}

	@Override
	protected void execute() {
		if (this.instructions.size() == numberOfCycles) {
			this.instructions.get(numberOfCycles - 1).instructionDecode();
		}
	}

}
