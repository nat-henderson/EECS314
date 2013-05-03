package com.smogdent.eecs314;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;

public class PipelineSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipeline_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onClick(View v){
        
        if(v.getId() == R.id.OKButton){
            Bundle settings = new Bundle();
            
            settings.putInt("memCycles", Integer.parseInt(((EditText)findViewById(R.id.memCycles)).getText().toString()));
            settings.putInt("wbCycles", Integer.parseInt(((EditText)findViewById(R.id.wbCycles)).getText().toString()));
            settings.putInt("idCycles", Integer.parseInt(((EditText)findViewById(R.id.idCycles)).getText().toString()));
            settings.putInt("exCycles", Integer.parseInt(((EditText)findViewById(R.id.exCycles)).getText().toString()));
            
            settings.putBoolean("memToEx", ((Checkable)findViewById(R.id.memToEx)).isChecked());
            settings.putBoolean("noForwarding", ((Checkable)findViewById(R.id.noForwarding)).isChecked());
            settings.putBoolean("exToEx", ((Checkable)findViewById(R.id.exToEx)).isChecked());
            
            Intent intent = new Intent(this, NewProgramActivity.class);
            intent.putExtra("settings", settings);
            startActivity(intent);
        }
        
    }
}
