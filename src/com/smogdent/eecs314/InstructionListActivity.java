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

        if (savedInstanceState.containsKey("set")){

            //TODO: multiple instruction category implementation
            String[] instructions = getResources().getStringArray(R.array.add_instructions);

            this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_instruction_list, R.id.list, instructions));

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
}
