package com.smogdent.eecs314;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CatagoryListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory_list);
      
        String[] categories = getResources().getStringArray(R.array.instruction_categories);

        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, categories));

        ListView listView = getListView();

        OnItemClickListener listListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // selected item
                String category = ((TextView) view).getText().toString();
                // bundling level, instruction
                Bundle bundle = new Bundle();
                bundle.putString("category", category);
                startActivity(new Intent(getApplicationContext(), GroupListActivity.class),bundle);
            }
        };
        
        listView.setOnItemClickListener(listListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.catagory_list, menu);
        return true;
    }

}
