package com.smogdent.eecs314;

import android.content.Context;

public class InstructionListArrayAdapter extends TwoLineArrayAdapter<String> {

    public InstructionListArrayAdapter(Context context,
            int listItemLayoutResourceId, String[] names) {
        super(context, listItemLayoutResourceId, names);
    }
    
    public InstructionListArrayAdapter(Context context, String[] ts) {
        super(context, ts);
    }

    @Override
    public String lineOneText(String t) {
        //shutup it's easier this way
        return t.split(":")[0];
    }

    @Override
    public String lineTwoText(String t) {
        String[] strings = t.split(":");
        if (strings.length > 1){
            return strings[1];
        }
        else return null;
    }

}
