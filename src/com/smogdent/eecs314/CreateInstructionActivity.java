package com.smogdent.eecs314;

import java.io.Serializable;

import mips.sim.Instruction;
import mips.sim.InstructionBuilder;
import mips.sim.UnsupportedInstructionException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateInstructionActivity extends Activity {

    String instruction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        InstructionWrapper iw = new InstructionWrapper();
        
        if(extras.containsKey("instruction")){
            instruction = extras.getString("instruction", "ADDI");

            String lowerCaseInstruction = instruction.toLowerCase();
            if(lowerCaseInstruction.equals("addi") || lowerCaseInstruction.equals("addiu")
                    || lowerCaseInstruction.equals("andi") || lowerCaseInstruction.equals("beq")
                    || lowerCaseInstruction.equals("bne") || lowerCaseInstruction.equals("lui")
                    || lowerCaseInstruction.equals("lw") || lowerCaseInstruction.equals("ori")
                    || lowerCaseInstruction.equals("sw")|| lowerCaseInstruction.equals("slti") 
                    || lowerCaseInstruction.equals("sltiu") || lowerCaseInstruction.equals("xori")) {
                setContentView(R.layout.i_type_instruction);

            } else{ 
                setContentView(R.layout.r_type_instruction);
            }
            
            
            
            
            TextView name = (TextView)findViewById(R.id.name);
            name.setText(instruction);
            TextView description = (TextView)findViewById(R.id.description);
            description.setText(iw.getDescription(instruction));
            
        }
        
        
        
        String[] registers = getResources().getStringArray(R.array.registers);
        
        //put reg info in spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, registers);
        ((Spinner)findViewById(R.id.spinner1)).setAdapter(adapter);
        ((Spinner)findViewById(R.id.spinner2)).setAdapter(adapter);
        if(findViewById(R.id.spinner3) != null){
            ((Spinner)findViewById(R.id.spinner3)).setAdapter(adapter);
        }
        
        
        //click the button, receive instruction?
        OnClickListener listener = new OnClickListener(){

            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder(instruction);
                sb.append(" ");
                sb.append((((Spinner)findViewById(R.id.spinner1)).getSelectedItem()).toString());
                sb.append(" ");
                sb.append(((Spinner)findViewById(R.id.spinner2)).getSelectedItem().toString());
                if(findViewById(R.id.spinner3) != null){
                    sb.append(" ");
                    sb.append(((Spinner)findViewById(R.id.spinner3)).getSelectedItem().toString());
                }
                if(findViewById(R.id.immediate) != null){
                    sb.append(" ");
                    sb.append(((EditText)findViewById(R.id.immediate)).getText().toString());
                }
                try {
                    Log.d("INFO", "string: " + sb.toString());
                    Instruction[] instructions = InstructionBuilder.buildInstruction(sb.toString());
                    
                    Bundle instructionBundle = new Bundle();
                    for(int i = 0; i < instructions.length; i++){
                    	Log.d("INFO", "placing instruction:  " + instructions[i] + 
                    			" which is serialized to " + instructions[i]);
                        instructionBundle.putSerializable("instr" + i, (Serializable)instructions[i]);
                    }
                    
                    Intent intent = new Intent(getApplicationContext(), NewProgramActivity.class);
                    
                    intent.putExtra("instructions", instructionBundle);
                    
                    startActivity(intent);
                    
                } catch (UnsupportedInstructionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        findViewById(R.id.button1).setOnClickListener(listener);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_instruction, menu);
        return true;
    }

}
