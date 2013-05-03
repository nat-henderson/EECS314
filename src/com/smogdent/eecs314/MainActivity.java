package com.smogdent.eecs314;

import android.os.Bundle;
import android.os.Environment;
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
            startActivity(new Intent(this, NewProgramActivity.class).putExtra("chosenfile", "NEW_FILE"));
        }
        else if(view.getId() == R.id.loadButton){
        	//check to see if the user can load and save files
        	String state = Environment.getExternalStorageState();
        	
        	if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        		//either we can read or we can read and write, so we can definitely load
        		DialogFragment dialog = new LoadFileDialogFragment();
        		dialog.show(getFragmentManager(), "LoadFileDialogFragment");
        	}
        	else {
        		//We are in some other state with regards to file storage, best not to load
        		DialogFragment dialog = new LoadFailedDialogFragment();
        		dialog.show(getFragmentManager(), "LoadFailedDialogFragment");
        	}
        }
    }

    


}
