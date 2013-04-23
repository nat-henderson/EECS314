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
      
        String[] sets = getResources().getStringArray(R.array.mathematical_sets);

        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, R.id.list, sets));

        ListView listView = getListView();

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // selected item
                String set = ((TextView) view).getText().toString();

                // bundling level, instruction

                Bundle bundle = new Bundle();

                bundle.putString("set", set);

                startActivity(new Intent(getApplicationContext(), InstructionListActivity.class),bundle);

            }
        });
    }
}
