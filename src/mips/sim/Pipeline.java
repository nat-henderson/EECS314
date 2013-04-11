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
		int initCounter = this.programCounter;
		for (Instruction i : initialInstructions) {
			this.memory.setWord(programCounter, i.toWord());
			programCounter += 4;
		}
	}

}
