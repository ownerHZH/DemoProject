package com.hiibox.houseshelter.util;

import android.util.Log;

    
  
  
  
  
  
public abstract class LogUtil {

	public static final boolean DEBUG = true;

	public static void log( String msg) {
		if (DEBUG) {
			Log.i("bluebox","~~~" + msg);
		}
	}
	
	public static void log(String TAG, String msg) {
		if (DEBUG) {
			Log.i("bluebox", TAG + "~~~" + msg);
		}
	}
	
}
