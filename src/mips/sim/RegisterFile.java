package mips.sim;

import java.io.Serializable;

public class RegisterFile implements Serializable {
	
	private Word[] registers;
	
	public RegisterFile() {
		this.registers = new Word[32];
		for (int i = 0; i < 32; i++) {
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