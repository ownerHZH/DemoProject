package com.hiibox.houseshelter.util;

import android.content.Context;
import android.widget.Toast;

    
  
  
  
  
  
public final class MessageUtil {
	public static void alertMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void alertMessage(Context context, int msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void alertMessageLong(Context context, int msg) {
	    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
