package com.smogdent.eecs314;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mips.sim.Instruction;
import mips.sim.InstructionBuilder;
import mips.sim.Memory;
import mips.sim.RegisterFile;
import mips.sim.UnsupportedInstructionException;
import mips.sim.Word;
import mips.sim.instructions.AddInstruction;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NewProgramActivity extends ListActivity {

    
    List<Instruction> instructions;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_program);
                
            instructions = new ArrayList<Instruction>();
        
            Memory memory = new Memory();
            RegisterFile regFile = new RegisterFile();
        
        instructions.add(new AddInstruction(memory, regFile, new Word(0)));
        
        Instruction[] insArr = (Instruction[]) instructions.toArray(new Instruction[0]);
        
        ListAdapter adapter = new InstructionObjectArrayAdapter(this, insArr);
        this.setListAdapter(adapter);
        
        //clicky button listener 
        //DOES NOT and CANNOT do list item clicks
        OnClickListener listener = new OnClickListener(){
            public void onClick(View view){
                if (view.getId() == R.id.newInstructionButton){
                    startActivity(new Intent(getApplicationContext(), CategoryListActivity.class));
                }
                
                if (view.getId() == R.id.goButton){
                    //TODO: GO!
                }
            }
        };
        
        //list click listener
        OnItemClickListener listListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                //FEATURE CUT!
                //TODO: make it do real things
                // selected item
                //String instruction = ((TextView) view).getText().toString();
                // bundling level, instruction
                //Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                //intent.putExtra("insruction", (Serializable)insArr[position]);
                //startActivity(intent);
            }
        };
        
        getListView().setOnItemClickListener(listListener);

        View instructionButton = findViewById(R.id.newInstructionButton);
        View goButton = findViewById(R.id.goButton);
        if(instructionButton != null){
            instructionButton.setOnClickListener(listener);
        }
        if(goButton != null){
            goButton.setOnClickListener(listener);
        }
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_instruction, menu);
        return true;
    }
    
    protected void onRestart(){
        
        //if we got a instruction, add the instruction to the instruction list! 
        Bundle extras = getIntent().getExtras();
        if((extras != null) && extras.containsKey("instructions")){
            
            Bundle iBundle = extras.getBundle("instructions");
            Set<String> keys = iBundle.keySet();
            for (String s : keys){
                instructions.add((Instruction)extras.getSerializable("s"));
            }
        }
    }
}
