package com.smogdent.eecs314;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import mips.sim.Instruction;
import mips.sim.MIPSSystem;
import mips.sim.Memory;
import mips.sim.Register;
import mips.sim.RegisterFile;
import mips.sim.UnsupportedInstructionException;
import mips.sim.Word;
import mips.sim.MIPSSystem.StageType;
import mips.sim.instructions.AddInstruction;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NewProgramActivity extends ListActivity {

    
    List<Instruction> instructions = new ArrayList<Instruction>();
    int idCycles = 1;
    int exCycles = 1;
    int memCycles = 1;
    int wbCycles = 1;
    boolean exToEx = false;
    boolean memToEx = false;
    InstructionDbHelper mDb;
    int fileId = -1;
    String fileName = "";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	for (int i = 0; i < instructions.size(); i++) {
    		Log.d("INFO", "serializing " + instructions.get(i).toString());
        	outState.putSerializable("instruction" + i, instructions.get(i));    		
    	}
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_program);
        Bundle extras = getIntent().getExtras();
        int whatIsThisFile = extras.getInt("chosenfile");
        if(whatIsThisFile != -1) {
        	//it's a saved file, actually have to load it
        	fileId = whatIsThisFile;
        	mDb.open();
        	Cursor cursor = mDb.fetchFile(fileId);
        	String input = cursor.getString(2);
        	Scanner sc = new Scanner(input);
        	while(sc.hasNextLine()){
        		try{
        		Instruction[] arr = mips.sim.InstructionBuilder.buildInstruction(sc.nextLine());
        		for(Instruction i : arr){
        			instructions.add(i);
        		}
        		}
        		catch(UnsupportedInstructionException e){
        			Log.e("INFO", "You tried to use an unsupported instruction.");
        		}
        	}
        	mDb.close();

        }
        else {
        	//do nothing; it's a new file
        }
        
            Memory memory = new Memory();
            RegisterFile regFile = new RegisterFile();
        
        if (savedInstanceState == null || savedInstanceState.getSerializable("instruction0") == null) {
        	instructions.add(new AddInstruction(memory, regFile, new Word(0)));
        }
        else {
        	Log.d("INFO", "tried to get objects like you wanted!");
        }
        
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
                else if(view.getId() == R.id.saveButton){
                	StringBuilder builder = new StringBuilder();
                	Instruction i;
                	while((i = instructions.remove(0)) != null){
                		builder.append(i.toString());
                	}
                	mDb.open();
                	if(mDb.fetchFile(fileId) != null) {
                		//note already exists
                		mDb.updateFile(fileId, fileName, builder.toString());
                	}
                	else {
                		mDb.createFile(fileName, builder.toString());
                	}
                	mDb.close();
                }
                
                else if (view.getId() == R.id.goButton){
                	MIPSSystem system = new MIPSSystem(instructions, 
                			idCycles,exCycles,memCycles,wbCycles);
                	if (exToEx) {
                		system.setupForwarding(StageType.EX, StageType.EX);
                	}
                	if (memToEx) {
                		system.setupForwarding(StageType.MEM, StageType.EX);
                	}
                	while (!system.isDone()) {
                		system.run();
                	}
                	StringBuilder output = new StringBuilder();
                	output.append("Number of instructions:  " + system.numberOfInstructions() + "\n");
                	output.append("Stall percentage:  " + system.getStallPercentage() + "\n");
                	output.append("Frequency:   " + system.getFrequency() + "\n");
                	output.append("Time to completion:  " + system.getTimeInSecondsSoFar() + "s\n");
                	for (int i = 0; i < 34; i++) {
                		Register r = system.getRegFile().getRegister(i);
                		output.append("Register " + Instruction.lookup(r.getId()) + ":  " + r.getWord().asInt());
                	}
                	// TODO:  code to open up the display window!
                }
                
                else if (view.getId() == R.id.settingsButton){
                    startActivity(new Intent(getApplicationContext(), PipelineSettings.class));
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
                //intent.putExtra("instruction", (Serializable)insArr[position]);
                //startActivity(intent);
            }
        };
        
        getListView().setOnItemClickListener(listListener);

        View instructionButton = findViewById(R.id.newInstructionButton);
        View goButton = findViewById(R.id.goButton);
        View settingsButton = findViewById(R.id.settingsButton);
        if(instructionButton != null){
            instructionButton.setOnClickListener(listener);
        }
        if(goButton != null){
            goButton.setOnClickListener(listener);
        }
        if(settingsButton != null){
            settingsButton.setOnClickListener(listener);
        }
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_instruction, menu);
        return true;
    }
    
    protected void onResume(){
    
        super.onResume();
        
        //if we got a instruction, add the instruction to the instruction list! 
        if(getIntent() != null){
            Log.d("INFO", "has intent");
            Bundle extras = getIntent().getExtras();
            if((extras != null) && extras.containsKey("instructions")){
                Log.d("INFO", "got instructions bundle");
                Bundle iBundle = extras.getBundle("instructions");
                Set<String> keys = iBundle.keySet();
                Log.d("INFO", "got key set sized " + keys.size());
                for (String s : keys){
                	Instruction out = (Instruction)iBundle.getSerializable(s);
                	if (out != null) {
                		instructions.add(out);
                    	Log.d("INFO", "got instruction: " + s + " to " + iBundle.getSerializable(s));
                	}
                }
            }
            else if (extras != null && extras.containsKey("settings")){
            	Bundle sBundle = extras.getBundle("settings");
            	this.idCycles = sBundle.getInt("idCycles");
            	this.exCycles = sBundle.getInt("exCycles");
            	this.memCycles = sBundle.getInt("memCycles");
            	this.wbCycles = sBundle.getInt("wbCycles");
            	if (sBundle.getBoolean("memToEx")) {
            		this.memToEx = true;
            	} else {
            		this.memToEx = false;
            	}
            	if (sBundle.getBoolean("exToEx")) {
            		this.exToEx = true;
            	} else {
            		this.exToEx = false;
            	}
            }
        }
        Instruction[] insArr = (Instruction[]) instructions.toArray(new Instruction[0]);
        
        ListAdapter adapter = new InstructionObjectArrayAdapter(this, insArr);
        this.setListAdapter(adapter);
    }
}
