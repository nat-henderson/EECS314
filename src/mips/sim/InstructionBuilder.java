package mips.sim;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import mips.sim.instructions.*;

public class InstructionBuilder {
	
	private static boolean built = false;
	private static Map<String, Integer> registerMap;
	private static Memory memory;
	private static RegisterFile regFile;
	
	private static void initSystems() {
		memory = MIPSSystem.getInstance().getMemory();
		regFile = MIPSSystem.getInstance().getRegFile();
	}
	
	// I love java
	private static void buildRegisterMap() {
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
	}
	
	/**
	 * parse one line of assembly, returning an array of the corresponding instructions
	 */
	public static Instruction[] buildInstruction(String line) 
			throws UnsupportedInstructionException {
		if (!built) {
			buildRegisterMap();
			initSystems();
			built = true;
		}
		Scanner s = new Scanner(line);
		String instruction = s.next();
		String lowerCaseInstruction = instruction.toLowerCase();
		if(lowerCaseInstruction == "add" || lowerCaseInstruction == "addu"
				|| lowerCaseInstruction == "and" || lowerCaseInstruction == "jr"
				|| lowerCaseInstruction == "or" || lowerCaseInstruction == "sll"
				|| lowerCaseInstruction == "slt" || lowerCaseInstruction == "sltu"
				|| lowerCaseInstruction == "sra" || lowerCaseInstruction == "srl"
				|| lowerCaseInstruction == "sub" || lowerCaseInstruction == "subu"
				|| lowerCaseInstruction == "xor" || lowerCaseInstruction == "nor") {
			return buildRtypeInstruction(line, instruction, s);
		}
		else if(lowerCaseInstruction == "addi" || lowerCaseInstruction == "addiu"
				|| lowerCaseInstruction == "andi" || lowerCaseInstruction == "beq"
				|| lowerCaseInstruction == "bne" || lowerCaseInstruction == "lui"
				|| lowerCaseInstruction == "lw" || lowerCaseInstruction == "ori"
				|| lowerCaseInstruction == "sw"|| lowerCaseInstruction == "slti" 
				|| lowerCaseInstruction == "sltiu" || lowerCaseInstruction == "xori") {
			return buildItypeInstruction(line, instruction, s);
		}
		else if(lowerCaseInstruction == "j" || lowerCaseInstruction == "jal") {
			throw new UnsupportedInstructionException();
		}
		else{
			throw new UnsupportedInstructionException();
		}
	}
		
	
	private static Instruction[] buildRtypeInstruction(String line, String instruction, Scanner s) 
			throws UnsupportedInstructionException {
		int opcode = 0x00000000;
		int registerRd = 0;
		int registerRs = 0;
		int registerRt = 0;
		int shamt = 0;
		
		String lowerCaseInstruction = instruction.toLowerCase();
		if(lowerCaseInstruction == "add" || lowerCaseInstruction == "addu"
				|| lowerCaseInstruction == "and" || lowerCaseInstruction == "or"
				|| lowerCaseInstruction == "slt" || lowerCaseInstruction == "sltu"
				|| lowerCaseInstruction == "sub" || lowerCaseInstruction == "subu"
				|| lowerCaseInstruction == "xor" || lowerCaseInstruction == "nor") {
			registerRd = registerMap.get(s.next().replaceAll(",", ""));
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
		}
		else if(lowerCaseInstruction == "sll" || lowerCaseInstruction == "sra"
				|| lowerCaseInstruction == "srl") {
			registerRd = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			shamt = s.nextInt();
		}
		else if(lowerCaseInstruction == "jr") {
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
		}
		else{
			throw new UnsupportedInstructionException();
		}
		
		Instruction[] instr = new Instruction[1];
		int funct = 0;
		Word w;
		if(lowerCaseInstruction == "add") {
			funct = 0x20;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AddInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "addu") {
			funct = 0x21;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AddUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "and") {
			funct = 0x24;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AndInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "jr") {
			funct = 0x08;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new JumpRegisterInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "nor") {
			funct = 0x27;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new NorInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "xor") {
			funct = 0x26;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new XorInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "or") {
			funct = 0x25;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new OrInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "slt") {
			funct = 0x2A;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SetLessThanInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "sltu") {
			funct = 0x2B;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SetLessThanUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "sll") {
			funct = 0x00;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftLeftLogicalInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "srl") {
			funct = 0x02;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftRightLogicalInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "sra") {
			funct = 0x03;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftRightArithmeticInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "sub") {
			funct = 0x22;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "subu") {
			funct = 0x23;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
		}
		else{
			throw new UnsupportedInstructionException();
		}
		return instr;
	}
	
	private static Instruction[] buildItypeInstruction(String line, String instruction, Scanner s) 
			throws UnsupportedInstructionException {
		int immediate;
		int registerRt;
		int registerRs;
		
		String lowerCaseInstruction = instruction.toLowerCase();
		if(lowerCaseInstruction == "addi" || lowerCaseInstruction == "addiu"
				|| lowerCaseInstruction == "andi" || lowerCaseInstruction == "ori"
				|| lowerCaseInstruction == "slti" || lowerCaseInstruction == "sltiu"
				|| lowerCaseInstruction == "xori") {
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			immediate = s.nextInt();
		}
		else if(lowerCaseInstruction == "sw" || lowerCaseInstruction == "lw") {
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
		else if(lowerCaseInstruction == "bne" || lowerCaseInstruction == "beq") {
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			immediate = s.nextInt(); // assumes direct address, doesn't yet handle labels
		}
		else{
			throw new UnsupportedInstructionException();
		}
		
		int opcode = 0x00;
		if(lowerCaseInstruction == "addi") {
			opcode = 0x08;
		}
		else if(lowerCaseInstruction == "addiu") {
			opcode = 0x09;
		}
		else if(lowerCaseInstruction == "andi") {
			opcode = 0x0C;
		}
		else if(lowerCaseInstruction == "beq") {
			opcode = 0x04;
		}
		else if(lowerCaseInstruction == "bne") {
			opcode = 0x05;
		}
		else if(lowerCaseInstruction == "lw") {
			opcode = 0x23;
		}
		else if(lowerCaseInstruction == "ori") {
			opcode = 0x0D;
		}
		else if(lowerCaseInstruction == "slti") {
			opcode = 0x0A;
		}
		else if(lowerCaseInstruction == "sltiu") {
			opcode = 0x0B;
		}
		else if(lowerCaseInstruction == "xori") {
			opcode = 0x0E;
		}

		
		Word w = new Word(immediate | (registerRt << 16) | (registerRs << 21) | (opcode << 26));
		Instruction[] instr = new Instruction[1];
		
		if(lowerCaseInstruction == "addi") {
			instr[0] = new AddImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "addiu") {
			instr[0] = new AddImmediateUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "andi") {
			instr[0] = new AndImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "beq") {
			instr[0] = new BranchOnEqualInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "bne") {
			instr[0] = new BranchOnNotEqualInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "lw") {
			instr[0] = new LoadWordInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "ori") {
			instr[0] = new OrImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "slti") {
			instr[0] = new SetLessThanImmediateInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "sltiu") {
			instr[0] = new SetLessThanImmediateUnsignedInstruction(memory, regFile, w);
		}
		else if(lowerCaseInstruction == "xori") {
			instr[0] = new XorImmediateInstruction(memory, regFile, w);
		}
		
		return instr;
	}
}
