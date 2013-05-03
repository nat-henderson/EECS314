package mips.sim;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Memory implements Serializable {
	/*Most of memory is empty; I'm going to go for a map of integer (address) to Word*/
	private Map<Integer, Byte> memory;
	
	public Memory() {
		this.memory = new HashMap<Integer, Byte>();
	}
	
	public Word getWord(int address) throws MemoryLocationNotInitializedException {
		byte[] bytearr = new byte[4];
		for (int i = 0; i < 4; i++) {
			if (this.memory.containsKey(address + i)) {
				bytearr[i] = this.memory.get(address + i);
			} else {
				throw new MemoryLocationNotInitializedException();
			}
		}
		return new Word(bytearr);
	}
	
	public void setWord(int address, Word word) {
		for (int i = 0; i < 4; i++) {
			this.memory.put(address + i, word.asByteArray()[i]);
		}
	}
	
	public void setByte(int address, byte b) {
		this.memory.put(address, b);
	}
	
	public byte getByte(int address) throws MemoryLocationNotInitializedException {
		if (this.memory.containsKey(address)) {
			return this.memory.get(address);
		}
		throw new MemoryLocationNotInitializedException();
	}

}
