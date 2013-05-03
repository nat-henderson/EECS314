package mips.sim;

public class InstructionDecodeStage extends Stage {
	
	public InstructionDecodeStage(int nSteps) {
		super(nSteps);
	}

	@Override
	protected void execute() {
		if (this.instructions.size() == numberOfCycles + 1) {
			Instruction inst = this.instructions.get(numberOfCycles);
			if (inst != null) {
				inst.instructionDecode();
			}
		}
	}
	public int getStageTimeInNs() {
		return 120;
	}
}
