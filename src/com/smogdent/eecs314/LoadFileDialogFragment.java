package com.smogdent.eecs314;

import java.io.File;
import java.io.FilenameFilter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class LoadFileDialogFragment extends DialogFragment{
	
	private String[] mFileList;
	private File mPath = new File(Environment.getExternalStorageDirectory() + "com.smogdent.eecs314");
	private String mChosenFile;
	private static final String FTYPE = ".txt";
	private static final int DIALOG_LOAD_FILE = 1000;
	private static final String TAG = "MainActivity";
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
    			}
    		});
    	dialog = builder.show();
    	return dialog;
    }
    
    
    private void loadFileList() {
    	try {
    		mPath.mkdirs();
    	}
    	catch(SecurityException e) {
    		Log.e(TAG, "unable to write on the sd card " + e.toString());
    	}
    	if(mPath.exists()) {
    		FilenameFilter filter = new FilenameFilter() {
    			public boolean accept(File dir, String filename) {
    				File sel = new File(dir, filename);
    				return filename.contains(FTYPE) || sel.isDirectory();
    			}
    		};
    		mFileList = mPath.list(filter);
    	}
    	else {
    		mFileList = new String[0];
    	}
    }

}
