package com.smogdent.eecs314;

import java.util.HashMap;

public class InstructionWrapper {

    //can't make me spell three syllable words
    //maps instructions to descriptions 
    private static HashMap<String, String> dMap;
    
    public InstructionWrapper(){
        dMap = new HashMap<String, String>();
        dMap.put("add", "Adds two registers and stores the result in a register");
        dMap.put("addi", "Adds a register and a sign-extended immediate value and stores the result in a register");
        dMap.put("addiu", "Adds a register and a sign-extended immediate value and stores the result in a register without overflow");
        dMap.put("addu", "Adds two registers and stores the result in a register without overflow");
        dMap.put("and", "Bitwise ands two registers and stores the result in a register");
        dMap.put("andi", "Bitwise ands a register and an immediate value and stores the result in a register");
        dMap.put("beq", "Branches if the two registers are equal");
        dMap.put("beqz", "Branches if the register is greater than or equal to zero");
        dMap.put("blez", "Branches if the register is less than or equal to zero");
        dMap.put("bltz", "Branches if the register is less than zero");
        dMap.put("bne", "Branches if the two registers are not equal");
        dMap.put("div", "Divides $s by $t and stores the quotient in $LO and the remainder in $HI");
        dMap.put("divu", "Divides $s by $t and stores the quotient in $LO and the remainder in $HI");
        dMap.put("j", "Jumps to the calculated address");
        dMap.put("jal", "Jumps to the calculated address and stores the return address in $31");
        dMap.put("jr", "Jump to the address contained in register $s");
        dMap.put("lb", "A byte is loaded into a register from the specified address");
        dMap.put("lui", "The immediate value is shifted left 16 bits and stored in the register. The lower 16 bits are zeroes");
        dMap.put("lw", "A word is loaded into a register from the specified address");
        dMap.put("mfhi", "The contents of register HI are moved to the specified register");
        dMap.put("mflo", "The contents of register LO are moved to the specified register");
        dMap.put("mult", "Multiplies $s by $t and stores the result in $LO");
        dMap.put("multu", "Multiplies $s by $t as unsigned integers and stores the result in $LO");
        dMap.put("or", "Bitwise logical ors two registers and stores the result in a register");
        dMap.put("ori", "Bitwise ors a register and an immediate value and stores the result in a register");
        dMap.put("sb", "The least significant byte of $t is stored at the specified address");
        dMap.put("sll", "Shifts a register value left by the shift amount listed in the instruction and places the result in a third register. Zeroes are shifted in");
        dMap.put("sllv", "Shifts a register value left by the value in a second register and places the result in a third register. Zeroes are shifted in");
        dMap.put("slt", "If $s is less than $t, $d is set to one. It gets zero otherwise");
        dMap.put("slti", "If $s is less than immediate, $t is set to one. It gets zero otherwise");
        //so booooored
        dMap.put("sltiu", "If $s is less than the unsigned immediate, $t is set to one. It gets zero otherwise");
        dMap.put("sltu", "If $s is less than $t, $d is set to one. It gets zero otherwise");
        dMap.put("sra", "Shifts a register value right by the shift amount (shamt) and places the value in the destination register. The sign bit is shifted in");
        dMap.put("srl", "Shifts a register value right by the shift amount (shamt) and places the value in the destination register. Zeroes are shifted in");
        dMap.put("srlv", "Shifts a register value right by the amount specified in $s and places the value in the destination register. Zeroes are shifted in");
        //are we even implementing all these?
        dMap.put("sub", "Subtracts two registers and stores the result in a register");
        dMap.put("subu", "Subtracts two registers and stores the result in a register");
        dMap.put("sw", "The contents of $t is stored at the specified address");
        //FUCKYOU SYSCALL
        dMap.put("xor", "Exclusive ors two registers and stores the result in a register");
        dMap.put("xori", "Bitwise exclusive ors a register and an immediate value and stores the result in a register");
        
        
    }
    
    /**
     * 
     * @param instruction
     * @return the description of the instruction
     */
    public static String getDescription(String instruction){
        return dMap.get(instruction.toLowerCase());
    }
    
    /**
     * returns a character representation of the type of the instruction
     * @param instruction
     * @return char representation
     */
    public char type(String instruction){
        return 'r';
    }
}
