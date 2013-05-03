package com.smogdent.eecs314;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//TODO: fix api level
@SuppressLint("NewApi")
public class InstructionListActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory_list);
        Bundle extras = getIntent().getExtras();
        
        String[] instructions;
        String set = "";
        
        if (extras.containsKey("set")){
            set = extras.getString("set");
        } 
        
        
        //ought to _all_ be string resources
        if(set.equals("Add")){
            instructions = getResources().getStringArray(R.array.add_instructions);
        }
        else if(set.equals("Subtract")){
            instructions = getResources().getStringArray(R.array.subtract_instructions);
        }
        else if(set.equals("Multiply")){
            instructions = getResources().getStringArray(R.array.multiply_instructions);
        }
        else if(set.equals("Divide")){
            instructions = getResources().getStringArray(R.array.divide_instructions);
        }
        else if(set.equals("And/Or/Xor")){
            instructions = getResources().getStringArray(R.array.and_instructions);
        }
        else if(set.equals("Shift")){
            instructions = getResources().getStringArray(R.array.shift_instructions);
        }
        else if(set.equals("Load")){
            instructions = getResources().getStringArray(R.array.load_instructions);
        }
        else if(set.equals("Branch")){
            instructions = getResources().getStringArray(R.array.branch_instructions);
        }
        else {
            instructions = getResources().getStringArray(R.array.jump_instructions);
        }

        ArrayAdapter<String> adapter = new InstructionListArrayAdapter(this, instructions);
        
        this.setListAdapter(adapter);

        ListView listView = getListView();

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                String instruction = "";
                
                // find first text item
                if (view instanceof ViewGroup){
                    int count = ((ViewGroup)view).getChildCount();
                    for(int i = 0; i < count; i++){
                        View child = ((ViewGroup)view).getChildAt(i);
                        if (child instanceof TextView){
                            instruction =((TextView)child).getText().toString();
                            break;
                        }
                    }
                }
                        
                
                // bundling level, instruction
                Intent intent = new Intent(getApplicationContext(), CreateInstructionActivity.class);
                intent.putExtra("instruction", instruction);
                
                startActivity(intent);

            }
        });
    }
}
