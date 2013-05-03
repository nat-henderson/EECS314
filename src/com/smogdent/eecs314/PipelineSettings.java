package com.smogdent.eecs314;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Checkable;
import android.widget.EditText;

public class PipelineSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipeline_settings);
        
        OnClickListener listener = new OnClickListener(){

            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.OKButton){
                Bundle settings = new Bundle();
                
                int mem = 0;
                int wb = 0;
                int id = 0;
                int ex = 0;
                
                try{
                    mem = Integer.parseInt(((EditText)findViewById(R.id.memCycles)).getText().toString());
                    wb = Integer.parseInt(((EditText)findViewById(R.id.wbCycles)).getText().toString());
                    id = Integer.parseInt(((EditText)findViewById(R.id.idCycles)).getText().toString());
                    ex = Integer.parseInt(((EditText)findViewById(R.id.exCycles)).getText().toString());
                    
                }
                catch (Exception E){
                    //ignore
                }
                
                settings.putInt("memCycles", mem);
                settings.putInt("wbCycles", wb);
                settings.putInt("idCycles", id);
                settings.putInt("exCycles", ex);
                
                settings.putBoolean("memToEx", ((Checkable)findViewById(R.id.memToEx)).isChecked());
                settings.putBoolean("noForwarding", ((Checkable)findViewById(R.id.noForwarding)).isChecked());
                settings.putBoolean("exToEx", ((Checkable)findViewById(R.id.exToEx)).isChecked());
                
                Intent intent = new Intent(getApplicationContext(), NewProgramActivity.class);
                intent.putExtra("settings", settings);
                startActivity(intent);
                }
                
            }
            
        };
        
        findViewById(R.id.OKButton).setOnClickListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
