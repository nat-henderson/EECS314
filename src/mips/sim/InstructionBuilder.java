package mips.sim;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import mips.sim.instructions.*;

public class InstructionBuilder {
	
	private static Map<String, Integer> registerMap;
	
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
	
	public static Instruction[] buildInstruction(String line) {
		if (registerMap == null) {
			buildRegisterMap();
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
			return buildJtypeInstruction(line, instruction, s);
		default:
			return buildExpandedInstruction(line, instruction, s);
		}
	}
	
	private static Instruction[] buildRtypeInstruction(String line, String instruction, Scanner s) {
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
			break;
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
			break;
		}
		return instr;
	}
	
}
