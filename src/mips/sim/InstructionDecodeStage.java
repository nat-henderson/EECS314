package mips.sim;

public class InstructionDecodeStage extends Stage {
	
	public InstructionDecodeStage(int nSteps) {
		super(nSteps);
	}

	@Override
	protected void execute() {
		for (Instruction i : this.instructions) {
			i.instructionDecode();
		}
	}

}
