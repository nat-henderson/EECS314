package com.smogdent.eecs314;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateInstructionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        InstructionWrapper iw = new InstructionWrapper();
        
        if(extras.containsKey("instruction")){
            String instruction = extras.getString("instruction", "ADDI");

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
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, registers);
        ((Spinner)findViewById(R.id.spinner1)).setAdapter(adapter);
        ((Spinner)findViewById(R.id.spinner2)).setAdapter(adapter);
        if(findViewById(R.id.spinner3) != null){
            ((Spinner)findViewById(R.id.spinner3)).setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_instruction, menu);
        return true;
    }

}
