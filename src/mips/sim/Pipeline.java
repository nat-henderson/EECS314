package mips.sim;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
	private int programCounter;
	private List<Stage> pipelineStages;
	private RegisterFile regFile;
	private Memory memory;
	
	public Pipeline(List<Instruction> initialInstructions) {
		this.programCounter = 0x40000000;
		this.pipelineStages = new ArrayList<Stage>();
		this.regFile = new RegisterFile();
		this.memory = new Memory();
		int initCounter = programCounter;
		for (Instruction i : initialInstructions) {
			this.memory.setWord(initCounter, i.toWord());
			initCounter += 4;
		}
	}
	
	public void run(int steps) {
		for (int i = 0; i < steps; i++) {
			for (int pipelineStage = this.pipelineStages.size() - 1;
					pipelineStage <= 0; pipelineStage--) {
				
			}
		}
	}

}
