package com.smogdent.eecs314;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GroupListActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory_list);
      
        Bundle extras = getIntent().getExtras();
        //relies on clicked list position to do the right thing which is sub-par.
        int cat = 0;
        if (extras.containsKey("category")){
            cat = extras.getInt("category");
        } 
        
        String[] sets;
        if (cat == 0){
            sets = getResources().getStringArray(R.array.mathematical_sets);
        }
        else if (cat == 1){
            sets = getResources().getStringArray(R.array.logical_sets);
        }
        
        else if (cat == 2){
            sets = getResources().getStringArray(R.array.store_sets);
        }
        
        else{
            sets = getResources().getStringArray(R.array.branch_sets);
        }
        
        
        

        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sets));

        ListView listView = getListView();

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // selected item
                String set = ((TextView) view).getText().toString();
                Intent intent = new Intent(getApplicationContext(), GroupListActivity.class);
                intent.putExtra("set", set);
                startActivity(intent);

            }
        });
    }
}
