package mips.sim;

public class Register {
	
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
