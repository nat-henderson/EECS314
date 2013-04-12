package mips.sim;

public class RegisterFile {
	
	private Word[] registers = new Word[32];
	
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
}