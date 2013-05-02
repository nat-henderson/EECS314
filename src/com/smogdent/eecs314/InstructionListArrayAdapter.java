package com.smogdent.eecs314;

import android.content.Context;

public class InstructionListArrayAdapter extends TwoLineArrayAdapter<String> {

    InstructionWrapper iw;
    
    public InstructionListArrayAdapter(Context context,
            int listItemLayoutResourceId, String[] names) {
        super(context, listItemLayoutResourceId, names);
        iw = new InstructionWrapper();
    }
    
    public InstructionListArrayAdapter(Context context, String[] ts) {
        super(context, ts);
        iw = new InstructionWrapper();
    }

    @Override
    public String lineOneText(String t) {
        return t;
        }

    @Override
    public String lineTwoText(String t) {
        return iw.getDescription(t);
    }

}
