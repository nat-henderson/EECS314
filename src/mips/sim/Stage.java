package mips.sim;

public interface Stage {
	
	public Instruction execute();
	
	public void load(Instruction i);
	
	public void flush();
	
	public void insertNop();

}
