package com.hiibox.houseshelter.net;

import com.hiibox.houseshelter.util.StringUtil;

public class SpliteUtil {
	
	public static boolean getRuquestStatus(String data) {
		if (StringUtil.isEmpty(data)) {
			return false;
		}
		if (!data.contains("\t")) {
			return false;
		}
		String[] datas = data.split("\t");
		return datas[0].equals("0");
	}
	
	public static String getResult(String data) {
		if (StringUtil.isEmpty(data)) {
			return "";
		}
		if (!data.contains("\t")) {
			return "";
		}
		String[] datas = data.split("\t");
		if (datas.length < 2) {
			return "";
		}
		return datas[1];
	}

}
