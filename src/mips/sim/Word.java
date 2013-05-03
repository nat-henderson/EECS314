package mips.sim;

import java.io.Serializable;

public class Word implements Serializable {
	
	private byte[] bytes = new byte[4];
	
	public Word(int initialValue) {
		this.bytes = new byte[] {
	            (byte)(initialValue >> 24),
	            (byte)(initialValue >> 16),
	            (byte)(initialValue >> 8),
	            (byte)initialValue};
	}
	
	
	//TODO: Add constructors that take registerids or registerids and literals and make words
	public Word(float initialValue) {
		this(Float.floatToRawIntBits(initialValue));		
	}
	
	public Word(byte[] initialValue) {
		System.arraycopy(initialValue, 0, this.bytes, 0, 4);
	}
	
	public int asInt() {
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	public float asFloat() {
		return Float.intBitsToFloat(this.asInt());
	}
	
	public byte[] asByteArray() {
		byte [] bytearr = new byte[4];
		System.arraycopy(bytes, 0, bytearr, 0, 4);
		return bytearr;
	}
	
	public String toString() {
		return "int: " + this.asInt() + " float: " + this.asFloat();
	}

}
