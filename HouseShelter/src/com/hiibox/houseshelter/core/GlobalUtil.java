package com.hiibox.houseshelter.core;

import android.os.Environment;

public class GlobalUtil {

	               
	public static final String NETWORK_NONE = "NONE";

	          
	public static final String CAMERA_PATH = Environment
			.getExternalStorageDirectory() + "/DCIM/Camera";

	        
	public static final String GLOBAL_PATH = Environment
			.getExternalStorageDirectory() + "/HouseShelter";

	          
	public static final String IMAGE_PATH = GLOBAL_PATH + "/images";

	         
	public static final int PORT = 80;
	public static final String HOST = "app.cqjbhb.gov.cn/cqjbhbapp/";
	public static final String REMOTE_HOST = "http://" + HOST;

	        
	public static int mScreenWidth = 720;
	public static int mScreenHeight = 1245;

}
