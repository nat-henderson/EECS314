package mips.sim;

import java.io.Serializable;

public class Register implements Serializable {
	
	private int registerId;
	private Word data;
	
	public Register(int id, Word data) {
		this.registerId = id;
		this.data = data;
	}
	
	public Word getWord() {
		return this.data;
	}
	
	public int getId() {
		return this.registerId;
	}

}
