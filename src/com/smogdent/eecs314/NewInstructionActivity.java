package com.smogdent.eecs314;


import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NewInstructionActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //dummy instruction
        String[] inst = {"addi", "lw"};
        
        //TODO: actually fetch instructions that have been added
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1, inst);
        this.setListAdapter(adapter);
        
        //clicky button listener 
        //DOES NOT and CANNOT do list item clicks
        //TODO: list click listener
        OnClickListener listener = new OnClickListener(){
            public void onClick(View view){
                if (view.getId() == R.id.newInstructionButton){
                    startActivity(new Intent(getApplicationContext(), InstructionListActivity.class));
                }
            }
        };
        
        //list click listener
        OnItemClickListener listListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                //TODO: make it do real things
                // selected item
                String instruction = ((TextView) view).getText().toString();
                // bundling level, instruction
                Bundle bundle = new Bundle();
                bundle.putString("instruction", instruction);
                startActivity(new Intent(getApplicationContext(), GroupListActivity.class),bundle);
            }
        };
        
        getListView().setOnItemClickListener(listListener);
        findViewById(R.id.newInstructionButton).setOnClickListener(listener);
        setContentView(R.layout.activity_new_instruction);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_instruction, menu);
        return true;
    }

}
