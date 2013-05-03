package com.smogdent.eecs314;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity{
	


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onClick(View view){
        if (view.getId() == R.id.newButton){
            startActivity(new Intent(this, NewProgramActivity.class).putExtra("chosenfile", -1));
        }
        else if(view.getId() == R.id.loadButton){
        	DialogFragment dialog = new LoadFileDialogFragment();
        	dialog.show(getFragmentManager(), "LoadFileDialogFragment");	
        }
        else if (view.getId() == R.id.pipelineButton){
            startActivity(new Intent(this, PipelineSettings.class));
        }
    }

    


}
