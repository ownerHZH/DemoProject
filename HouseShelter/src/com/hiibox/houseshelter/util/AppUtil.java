package com.hiibox.houseshelter.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
    
  
  
  
  
public class AppUtil {
	    
  
  
	public static int getAppVersionCode(Context context) {
		int versionCode = 0;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}

}
