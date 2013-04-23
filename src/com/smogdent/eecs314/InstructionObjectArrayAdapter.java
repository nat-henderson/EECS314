package com.smogdent.eecs314;

import java.util.List;

import android.content.Context;
import mips.sim.Instruction;
import mips.sim.Register;

public class InstructionObjectArrayAdapter extends TwoLineArrayAdapter<Instruction> {

    Context context;
    
    public InstructionObjectArrayAdapter(Context context, Instruction[] ts) {
        super(context, ts);
        this.context = context;
    }

    @Override
    public String lineOneText(Instruction t) {
        return context.getResources().getString(t.getInstructionNameId());
        
    }

    @Override
    public String lineTwoText(Instruction t) {
        List<Register> inRegs = t.getInputRegisters();
        List<Register> outRegs = t.getOutputRegisters();
        
        return null;
    }

}
