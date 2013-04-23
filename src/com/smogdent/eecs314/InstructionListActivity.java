package com.smogdent.eecs314;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//TODO: fix api level
@SuppressLint("NewApi")
public class InstructionListActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_catagory_list);
        
        String[] sets = getResources().getStringArray(R.array.add_instructions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sets);
        
        this.setListAdapter(adapter);

        ListView listView = getListView();

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // selected item
                String instruction = ((TextView) view).getText().toString();
                // bundling level, instruction
                Bundle bundle = new Bundle();
                bundle.putString("instruction", instruction);
                startActivity(new Intent(getApplicationContext(), ItemDetailActivity.class),bundle);

            }
        });
    }
}
