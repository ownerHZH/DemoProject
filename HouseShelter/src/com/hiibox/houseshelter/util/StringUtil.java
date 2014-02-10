package com.hiibox.houseshelter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public final class StringUtil {

	public static final String EMPTY_SRING = "";
	public static final String NULL_STRING = "null";
	private static final String DEFAULT_SPLIT = "\\s|,|\\|";

	public static String toString(Object value) {
		return String.valueOf(value);
	}

	    
  
  
	public static boolean isEmpty(String str) {
		return str == null || EMPTY_SRING.equals(str)
				|| NULL_STRING.equals(str);
	}

	    
  
  
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	    
  
  
  
  
  
	public static String[] split(String string) {
		if (isEmpty(string)) {
			return null;
		}
		return string.split(DEFAULT_SPLIT);
	}

	public static String[] split(String string, String split) {
		if (isEmpty(string)) {
			return null;
		}
		if (isEmpty(split)) {
			return new String[] { string };
		}
		return string.split(split);
	}

	public static String filter(String str, String filterStr) {
		if (isEmpty(str))
			return EMPTY_SRING;
		if (isEmpty(filterStr)) {
			return str;
		}
		return str.replaceAll(filterStr, EMPTY_SRING);
	}

	    
  
  
  
  
  
	public static boolean checkEmail(String email) {
		                   
		                                                          
		   
		                                
		                
		            
		                 
		     
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		System.out.println(matcher.matches());
		return matcher.matches();
	}

	    
  
  
	public static String encryptEmail(String email) {

		String fistString = email.substring(0, 3);

		String[] twoString = email.split("@");

		return fistString + "****@" + twoString[1];

	}

	    
  
  
  
  
  

	public static String catString(String string) {

		return string.substring(0, 4) + string.substring(5, 7);

	}

	public static String toUTF8(String strInput) {
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < strInput.length(); i++) {
			output.append("\\u" + Integer.toString(strInput.charAt(i), 16));
		}
		return output.toString();
	}

	public static String stringToUnicode(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			if (ch > 255)
				str += "\\u" + Integer.toHexString(ch);
			else
				str += "\\" + Integer.toHexString(ch);
		}
		return str;
	}

	    
  
  
  
  
  
	public static void writeJSONObjectToSdCard(JSONObject person, String path) {
		File file = new File(path);
		                   
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		         
		PrintStream outputStream = null;
		try {
			outputStream = new PrintStream(new FileOutputStream(file));
			outputStream.print(person.toString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	    
  
  
  
	public static String getFileToString(String path) {
		File file = new File(path);
		String str = "";

		if (!file.exists()) {
			return str;
		}

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();
		} catch (FileNotFoundException e) {
			                                   
			e.printStackTrace();
		} catch (IOException e) {
			                                   
			e.printStackTrace();
		}

		return sb.toString();

	}

}
