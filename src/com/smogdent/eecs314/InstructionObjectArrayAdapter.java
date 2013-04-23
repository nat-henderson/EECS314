package com.smogdent.eecs314;

import java.util.List;

import android.content.Context;
import mips.sim.Instruction;
import mips.sim.Register;
import mips.sim.instructions.ITypeInstruction;

public class InstructionObjectArrayAdapter extends TwoLineArrayAdapter<Instruction> {

    Context context;
    
    public InstructionObjectArrayAdapter(Context context, Instruction[] ts) {
        super(context, ts);
        this.context = context;
    }

    @Override
    public String lineOneText(Instruction t) {
        return t.getInstructionName();
        
    }

    @Override
    public String lineTwoText(Instruction t) {
        List<Register> inRegs = t.getInputRegisters();
        List<Register> outRegs = t.getOutputRegisters();
        
        StringBuilder args = new StringBuilder();
        
        for (Register r: inRegs){
            args.append("$" + r.getId() + " ");
        }
        
        for (Register r: outRegs){
            args.append("$" + r.getId() + " ");
        }
        
        if (t instanceof mips.sim.instructions.ITypeInstruction){
            args.append(((ITypeInstruction) t).getImmediate());
        }
        
        return args.toString();
    }

}
