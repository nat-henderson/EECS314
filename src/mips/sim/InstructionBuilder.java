package mips.sim;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import mips.sim.instructions.*;

public class InstructionBuilder {
	
	private static Memory memory;
	private static RegisterFile regFile;
	
	private static void initSystems() {
		memory = MIPSSystem.getInstance().getMemory();
		regFile = MIPSSystem.getInstance().getRegFile();
	}
	
	public static Map<String, Integer> registerMap;
	
	// I love java
	static {
		registerMap = new HashMap<String, Integer>();
		registerMap.put("$zero", 0);
		registerMap.put("$at", 1);
		registerMap.put("$v0", 2);
		registerMap.put("$v1", 3);
		registerMap.put("$a0", 4);
		registerMap.put("$a1", 5);
		registerMap.put("$a2", 6);
		registerMap.put("$a3", 7);
		registerMap.put("$t0", 8);
		registerMap.put("$t1", 9);
		registerMap.put("$t2", 10);
		registerMap.put("$t3", 11);
		registerMap.put("$t4", 12);
		registerMap.put("$t5", 13);
		registerMap.put("$t6", 14);
		registerMap.put("$t7", 15);
		registerMap.put("$s0", 16);
		registerMap.put("$s1", 17);
		registerMap.put("$s2", 18);
		registerMap.put("$s3", 19);
		registerMap.put("$s4", 20);
		registerMap.put("$s5", 21);
		registerMap.put("$s6", 22);
		registerMap.put("$s7", 23);
		registerMap.put("$t8", 24);
		registerMap.put("$t9", 25);
		registerMap.put("$k0", 26);
		registerMap.put("$k1", 27);
		registerMap.put("$gp", 28);
		registerMap.put("$sp", 29);
		registerMap.put("$fp", 30);
		registerMap.put("$ra", 31);
		registerMap.put("HI", 33);
		registerMap.put("LO", 34);
		
		initSystems();
	}
	
	/**
	 * parse one line of assembly, returning an array of the corresponding instructions
	 */
	public static Instruction[] buildInstruction(String line) 
			throws UnsupportedInstructionException {
		Scanner s = new Scanner(line);
		String instruction = s.next();
		String lowerCaseInstruction = instruction.toLowerCase();
		if(lowerCaseInstruction.equals("add") || lowerCaseInstruction.equals("addu")
				|| lowerCaseInstruction.equals("and") || lowerCaseInstruction.equals("jr")
				|| lowerCaseInstruction.equals("or") || lowerCaseInstruction.equals("sll")
				|| lowerCaseInstruction.equals("slt") || lowerCaseInstruction.equals("sltu")
				|| lowerCaseInstruction.equals("sra") || lowerCaseInstruction.equals("srl")
				|| lowerCaseInstruction.equals("sub") || lowerCaseInstruction.equals("subu")
				|| lowerCaseInstruction.equals("xor") || lowerCaseInstruction.equals("nor")
				|| lowerCaseInstruction.equals("mult") || lowerCaseInstruction.equals("multu")
				|| lowerCaseInstruction.equals("div") || lowerCaseInstruction.equals("divu")
				|| lowerCaseInstruction.equals("mfhi") || lowerCaseInstruction.equals("mflo")) {
			return buildRtypeInstruction(line, instruction, s);
		}
		else if(lowerCaseInstruction.equals("addi") || lowerCaseInstruction.equals("addiu")
				|| lowerCaseInstruction.equals("andi") || lowerCaseInstruction.equals("beq")
				|| lowerCaseInstruction.equals("bne") || lowerCaseInstruction.equals("lui")
				|| lowerCaseInstruction.equals("lw") || lowerCaseInstruction.equals("ori")
				|| lowerCaseInstruction.equals("sw") || lowerCaseInstruction.equals("slti") 
				|| lowerCaseInstruction.equals("sltiu") || lowerCaseInstruction.equals("xori")
				|| lowerCaseInstruction.equals("lb") || lowerCaseInstruction.equals("sb")) {
			return buildItypeInstruction(line, instruction, s);
		}
		else if(lowerCaseInstruction.equals("j") || lowerCaseInstruction.equals("jal")) {
			return buildJTypeInstruction(line, lowerCaseInstruction, s);
		}
		else{
			return buildPseudoInstruction(line, lowerCaseInstruction, s);
		}
	}
		
	
	private static Instruction[] buildRtypeInstruction(String line, String instruction, Scanner s) 
			throws UnsupportedInstructionException {
		int opcode = 0;
		int registerRd = 0;
		int registerRs = 0;
		int registerRt = 0;
		int shamt = 0;
		
		String lowerCaseInstruction = instruction.toLowerCase();
		if(lowerCaseInstruction.equals("add") || lowerCaseInstruction.equals("addu")
				|| lowerCaseInstruction.equals("and") || lowerCaseInstruction.equals("or")
				|| lowerCaseInstruction.equals("slt") || lowerCaseInstruction.equals("sltu")
				|| lowerCaseInstruction.equals("sub") || lowerCaseInstruction.equals("subu")
				|| lowerCaseInstruction.equals("xor") || lowerCaseInstruction.equals("nor")) {
			registerRd = registerMap.get(s.next().replaceAll(",", ""));
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
		}
		else if(lowerCaseInstruction.equals("sll") || lowerCaseInstruction.equals("sra")
				|| lowerCaseInstruction.equals("srl")) {
			registerRd = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			shamt = s.nextInt();
		}
		else if(lowerCaseInstruction.equals("jr")) {
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
		}
		else if (lowerCaseInstruction.equals("mult") || lowerCaseInstruction.equals("multu")
				|| lowerCaseInstruction.equals("div") || lowerCaseInstruction.equals("divu")) {
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
		}
		else if (lowerCaseInstruction.equals("mfhi") || lowerCaseInstruction.equals("mflo")) {
			registerRd = registerMap.get(s.next().replaceAll(",", ""));
		}
		
		Instruction[] instr = new Instruction[1];
		int funct = 0;
		Word w;
		if(lowerCaseInstruction.equals("add")) {
			funct = 0x20;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AddInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("addu")) {
			funct = 0x21;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AddUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("and")) {
			funct = 0x24;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AndInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("jr")) {
			funct = 0x08;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new JumpRegisterInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("nor")) {
			funct = 0x27;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new NorInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("xor")) {
			funct = 0x26;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new XorInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("or")) {
			funct = 0x25;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new OrInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("slt")) {
			funct = 0x2A;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SetLessThanInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("sltu")) {
			funct = 0x2B;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SetLessThanUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("sll")) {
			funct = 0x00;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftLeftLogicalInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("srl")) {
			funct = 0x02;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftRightLogicalInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("sra")) {
			funct = 0x03;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftRightArithmeticInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("sub")) {
			funct = 0x22;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("subu")) {
			funct = 0x23;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("mult")) {
			funct = 0x18;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("multu")) {
			funct = 0x19;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("div")) {
			funct = 0x1A;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("divu")) {
			funct = 0x1B;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("mfhi")) {
			funct = 0x10;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("mflo")) {
			funct = 0x12;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		return instr;
	}
	
	private static Instruction[] buildItypeInstruction(String line, String instruction, Scanner s) 
			throws UnsupportedInstructionException {
		int immediate;
		int registerRt;
		int registerRs;
		
		String lowerCaseInstruction = instruction.toLowerCase();
		if(lowerCaseInstruction.equals("addi") || lowerCaseInstruction.equals("addiu")
				|| lowerCaseInstruction.equals("andi") || lowerCaseInstruction.equals("ori")
				|| lowerCaseInstruction.equals("slti") || lowerCaseInstruction.equals("sltiu")
				|| lowerCaseInstruction.equals("xori")) {
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			immediate = s.nextInt();
		}
		else if(lowerCaseInstruction.equals("sw") || lowerCaseInstruction.equals("lw")
				|| lowerCaseInstruction.equals("lb") || lowerCaseInstruction.equals("sb")) {
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			String addr = s.next();
			int scan;
			for (scan = 0; addr.charAt(scan) != '(' && scan < addr.length(); scan++);
			if (scan == 0) {
				immediate = 0;
				registerRs = registerMap.get(addr.substring(1, addr.length() - 1));
			} else {
				immediate = Integer.parseInt(addr.substring(0, scan));
				registerRs = registerMap.get(addr.substring(scan + 1, addr.length() - 1));
			}
		}
		else if(lowerCaseInstruction.equals("bne") || lowerCaseInstruction.equals("beq")) {
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			immediate = s.nextInt(); // assumes direct address, doesn't yet handle labels
		}
		else{
			throw new UnsupportedInstructionException();
		}
		
		int opcode = 0x00;
		if(lowerCaseInstruction.equals("addi")) {
			opcode = 0x08;
		}
		else if(lowerCaseInstruction.equals("addiu")) {
			opcode = 0x09;
		}
		else if(lowerCaseInstruction.equals("andi")) {
			opcode = 0x0C;
		}
		else if(lowerCaseInstruction.equals("beq")) {
			opcode = 0x04;
		}
		else if(lowerCaseInstruction.equals("bne")) {
			opcode = 0x05;
		}
		else if(lowerCaseInstruction.equals("lw")) {
			opcode = 0x23;
		}
		else if(lowerCaseInstruction.equals("ori")) {
			opcode = 0x0D;
		}
		else if(lowerCaseInstruction.equals("slti")) {
			opcode = 0x0A;
		}
		else if(lowerCaseInstruction.equals("sltiu")) {
			opcode = 0x0B;
		}
		else if(lowerCaseInstruction.equals("xori")) {
			opcode = 0x0E;
		} else if (lowerCaseInstruction.equals("lb")) {
			opcode = 0x20;
		} else if (lowerCaseInstruction.equals("sb")) {
			opcode = 0x28;
		}

		
		Word w = new Word(immediate | (registerRt << 16) | (registerRs << 21) | (opcode << 26));
		Instruction[] instr = new Instruction[1];
		
		if(lowerCaseInstruction.equals("addi")) {
			instr[0] = new AddImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("addiu")) {
			instr[0] = new AddImmediateUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("andi")) {
			instr[0] = new AndImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("beq")) {
			instr[0] = new BranchOnEqualInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("bne")) {
			instr[0] = new BranchOnNotEqualInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("lw")) {
			instr[0] = new LoadWordInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("ori")) {
			instr[0] = new OrImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("slti")) {
			instr[0] = new SetLessThanImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("sltiu")) {
			instr[0] = new SetLessThanImmediateUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction.equals("xori")) {
			instr[0] = new XorImmediateInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("lb")) {
			instr[0] = new LoadByteInstruction(memory, regFile, w);
		}
		else if (lowerCaseInstruction.equals("sb")) {
			instr[0] = new StoreByteInstruction(memory, regFile, w);
		}
		
		return instr;
	}
	
	private static Instruction[] buildJTypeInstruction(String line, String instruction, Scanner s) {
		int location = s.nextInt();
		int mask = 0x3ffffff;
		location = mask & location;
		
		Instruction[] instr = new Instruction[1];
		
		if (instruction.equals("j")) {
			instr[0] = new JumpInstruction(memory, regFile, new Word((0x2 << 26) | location));
		} else if (instruction.equals("jr")) {
			instr[0] = new JumpInstruction(memory, regFile, new Word((0x3 << 26) | location));
		}
		
		return instr;
	}
	
	private static Instruction[] buildPseudoInstruction(String line, String instruction, Scanner s) 
			throws UnsupportedInstructionException {
		int registerLeft = registerMap.get(s.next().replaceAll(",", ""));
		int registerRight = registerMap.get(s.next().replaceAll(",", ""));
		int registerAt = 0x01;
		int immediate = s.nextInt(); // assumes direct address, doesn't yet handle labels
		
		final int slt = 0x2A; // funct
		final int beq = 0x04; // opcode
		final int bne = 0x05; // opcode
		
		Instruction[] instr = new Instruction[2];
		
		if (instruction.equals("blt")) {
			Word set = new Word( slt | (registerAt << 11) | (registerLeft << 16) | (registerRight << 21));
			instr[0] = new SetLessThanInstruction(memory, regFile, set);
			Word branch = new Word(immediate | (registerAt << 21) | (beq << 26));
			instr[1] = new BranchOnEqualInstruction(memory, regFile, branch);
		} else if (instruction.equals("bgt")) {
			Word set = new Word( slt | (registerAt << 11) | (registerRight << 16) | (registerLeft << 21));
			instr[0] = new SetLessThanInstruction(memory, regFile, set);
			Word branch = new Word(immediate | (registerAt << 21) | (beq << 26));
			instr[1] = new BranchOnNotEqualInstruction(memory, regFile, branch);
		} else if (instruction.equals("ble")) {
			Word set = new Word( slt | (registerAt << 11) | (registerRight << 16) | (registerLeft << 21));
			instr[0] = new SetLessThanInstruction(memory, regFile, set);
			Word branch = new Word(immediate | (registerAt << 21) | (beq << 26));
			instr[1] = new BranchOnEqualInstruction(memory, regFile, branch);
		} else if (instruction.equals("bge")) {
			Word set = new Word( slt | (registerAt << 11) | (registerLeft << 16) | (registerRight << 21));
			instr[0] = new SetLessThanInstruction(memory, regFile, set);
			Word branch = new Word(immediate | (registerAt << 21) | (beq << 26));
			instr[1] = new BranchOnEqualInstruction(memory, regFile, branch);
		} else {
			throw new UnsupportedInstructionException();
		}
		
		return instr;
	}
}
