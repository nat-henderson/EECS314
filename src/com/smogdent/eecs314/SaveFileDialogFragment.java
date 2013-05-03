package com.smogdent.eecs314;

import java.io.File;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;

public class SaveFileDialogFragment extends DialogFragment{
	
	private String[] mFileList;
	private File mPath = new File(Environment.getExternalStorageDirectory() + "com.smogdent.eecs314");
	private String mChosenFile;
	private static final String FTYPE = ".txt";
	private static final int DIALOG_SAVE_FILE = 2000;
	private static final String TAG = "NewProgramActivity";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceBundle) {
		Dialog dialog = null;
		
		return dialog;
	}

}
