package com.smogdent.eecs314;

import java.io.File;
import java.io.FilenameFilter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class LoadFileDialogFragment extends DialogFragment{
	
	private String[] mFileList;
	private String mChosenFile;
	private static final String TAG = "MainActivity";
	InstructionDbHelper mDb;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	mDb.open();
    	Cursor cursor = mDb.fetchAllFiles();
    	Dialog dialog = null;
    	AlertDialog.Builder builder = new Builder(getActivity());
    		builder.setTitle("Choose your file");
    		if(mFileList == null) {
    			Log.e(TAG, "Showing file picker before loading the file list");
    			dialog = builder.create();
    			return dialog;
    		}
    		builder.setItems(mFileList, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				mChosenFile = mFileList[which];
    				startActivity(new Intent(getActivity(), NewProgramActivity.class).putExtra("chosenfile", mChosenFile));
    			}
    		});
    	dialog = builder.show();
    	return dialog;
    }
}
