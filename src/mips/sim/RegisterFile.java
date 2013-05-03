package mips.sim;

import java.io.Serializable;

public class RegisterFile implements Serializable {
	
	private Word[] registers;
	
	// 33 is hi, 34 is lo.
	public RegisterFile() {
		this.registers = new Word[34];
		for (int i = 0; i < 34; i++) {
			this.registers[i] = new Word(0);
		}
	}
	
	public Register getRegister(int index) {
		return new Register(index, registers[index]);
	}
	
	public void putRegister(int index, int value) {
		registers[index] = new Word(value);
	}
	
	public void putRegister(int index, float value) {
		registers[index] = new Word(value);
	}
	
	public void putRegister(int index, byte[] value) {
		registers[index] = new Word(value);
	}
	
	public void putRegister(int index, Word word) {
		registers[index] = word;
	}
}