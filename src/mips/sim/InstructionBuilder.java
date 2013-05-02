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
		switch(instruction.toLowerCase()) {
		case "add": case "addu": case "and": case "jr":
		case "or": case "sll": case "slt": case "sltu":
		case "sra": case "srl": case "sub": case "subu":
		case "xor": case "nor":
			return buildRtypeInstruction(line, instruction, s);
		case "addi": case "addiu": case "andi": case "beq":
		case "bne": case "lui": case "lw": case "ori":
		case "sw": case "slti": case "sltiu": case "xori":
			return buildItypeInstruction(line, instruction, s);
		case "j": case "jal":
			throw new UnsupportedInstructionException();
		default:
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
		
		switch(instruction) {
		case "add": case "addu": case "and": case "or":
		case "slt": case "sltu": case "sub": case "subu":
		case "xor": case "nor":
			registerRd = registerMap.get(s.next().replaceAll(",", ""));
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			break;
		case "sll": case "sra": case "srl":
			registerRd = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			shamt = s.nextInt();
			break;
		case "jr":
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			break;
		default:
			throw new UnsupportedInstructionException();
		}
		
		Instruction[] instr = new Instruction[1];
		int funct = 0;
		Word w;
		switch(instruction) {
		case "add":
			funct = 0x20;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AddInstruction(memory, regFile, w);
			break;
		case "addu":
			funct = 0x21;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AddUnsignedInstruction(memory, regFile, w);
			break;
		case "and":
			funct = 0x24;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new AndInstruction(memory, regFile, w);
			break;
		case "jr":
			funct = 0x08;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new JumpRegisterInstruction(memory, regFile, w);
			break;
		case "nor":
			funct = 0x27;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new NorInstruction(memory, regFile, w);
			break;
		case "xor":
			funct = 0x26;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new XorInstruction(memory, regFile, w);
			break;
		case "or":
			funct = 0x25;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new OrInstruction(memory, regFile, w);
			break;
		case "slt":
			funct = 0x2A;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SetLessThanInstruction(memory, regFile, w);
			break;
		case "sltu":
			funct = 0x2B;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SetLessThanUnsignedInstruction(memory, regFile, w);
			break;
		case "sll":
			funct = 0x00;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftLeftLogicalInstruction(memory, regFile, w);
			break;
		case "srl":
			funct = 0x02;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftRightLogicalInstruction(memory, regFile, w);
			break;
		case "sra":
			funct = 0x03;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new ShiftRightArithmeticInstruction(memory, regFile, w);
			break;
		case "sub":
			funct = 0x22;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractInstruction(memory, regFile, w);
			break;
		case "subu":
			funct = 0x23;
			w = new Word(funct | (shamt << 6) | (registerRd << 11) | (registerRt << 16) |
					(registerRs << 21) | (opcode << 26));
			instr[0] = new SubtractUnsignedInstruction(memory, regFile, w);
			break;
		default:
			throw new UnsupportedInstructionException();
		}
		return instr;
	}
	
	private static Instruction[] buildItypeInstruction(String line, String instruction, Scanner s) 
			throws UnsupportedInstructionException {
		int immediate;
		int registerRt;
		int registerRs;
		
		switch(instruction) {
		case "addi": case "addiu": case "andi": case "ori":
		case "slti": case "sltiu": case "xori":
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			immediate = s.nextInt();
			break;
		case "sw": case "lw":
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
			break;
		case "bne": case "beq":
			registerRs = registerMap.get(s.next().replaceAll(",", ""));
			registerRt = registerMap.get(s.next().replaceAll(",", ""));
			immediate = s.nextInt(); // assumes direct address, doesn't yet handle labels
			break;
		default:
			throw new UnsupportedInstructionException();
		}
		
		int opcode = 0x00;
		switch(instruction) {
			case "addi": opcode = 0x08; break;
			case "addiu": opcode = 0x09; break;
			case "andi": opcode = 0x0C; break;
			case "beq": opcode = 0x04; break;
			case "bne": opcode = 0x05; break;
			case "lw": opcode = 0x23; break;
			case "ori": opcode = 0x0D; break;
			case "slti": opcode = 0x0A; break;
			case "sltiu": opcode = 0x0B; break;
			case "xori": opcode = 0x0E; break;
		}
		
		Word w = new Word(immediate | (registerRt << 16) | (registerRs << 21) | (opcode << 26));
		Instruction[] instr = new Instruction[1];
		switch(instruction) {
		case "addi": instr[0] = new AddImmediateInstruction(memory, regFile, w); break;
		case "addiu": instr[0] = new AddImmediateUnsignedInstruction(memory, regFile, w); break;
		case "andi": instr[0] = new AndImmediateInstruction(memory, regFile, w); break;
		case "beq": instr[0] = new BranchOnEqualInstruction(memory, regFile, w); break;
		case "bne": instr[0] = new BranchOnNotEqualInstruction(memory, regFile, w); break;
		case "lw": instr[0] = new LoadWordInstruction(memory, regFile, w); break;
		case "ori": instr[0] = new OrImmediateInstruction(memory, regFile, w); break;
		case "slti": instr[0] = new SetLessThanImmediateInstruction(memory, regFile, w); break;
		case "sltiu": instr[0] = new SetLessThanImmediateUnsignedInstruction(memory, regFile, w); break;
		case "xori": instr[0] = new XorImmediateInstruction(memory, regFile, w); break;
		}
		
		return instr;
	}
}
