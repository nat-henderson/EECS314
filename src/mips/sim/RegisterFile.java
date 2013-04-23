package mips.sim;

public class RegisterFile {
	
	private Word[] registers = new Word[32];
	
	public RegisterFile() {
		System.out.println("Constructed");
	}
	
	public Register getRegister(int index) {
		System.out.println("Request for register:  " + index);
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