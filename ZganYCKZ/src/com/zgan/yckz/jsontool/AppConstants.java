package com.zgan.yckz.jsontool;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppConstants {
	public static int REQUEST_TIMEOUT = 20;
	public static int SO_TIMEOUT = 120;
	
	public static String HttpHostAdress="http://msgservice1.zgantech.com/";
	
	public static String TARGET_NOT_FOUND_EXCEPTION = "Target host must not be null";
	public static String HTTPHOST_CONNECT_EXCEPTION = "HttpHostConnectException";
	
	public static Gson gson = GsonUtil.getGson();
	public static Type type_userList = new TypeToken<List<User>>() {
	}.getType();

}
