package mips.sim;

import java.util.HashMap;
import java.util.Map;

public class Memory {
	/*Most of memory is empty; I'm going to go for a map of integer (address) to Word*/
	private Map<Integer, Word> memory;
	
	public Memory() {
		this.memory = new HashMap<Integer, Word>();
	}
	
	public Word getWord(int address) throws MemoryLocationNotInitializedException {
		if (this.memory.containsKey(address)) {
			return this.memory.get(address);
		}
		else if (this.memory.containsKey(address - 1)) {
			byte[] bytes = this.memory.get(address - 1).asByteArray();
			// here lies boredom
		}
		else {
			throw new MemoryLocationNotInitializedException();
		}
	}
	
	public void setWord(int address, Word word) {
		this.memory.put(address, word);
	}

}
