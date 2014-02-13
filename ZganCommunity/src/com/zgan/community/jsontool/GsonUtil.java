package com.zgan.community.jsontool;
import java.lang.reflect.Type;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		// gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		return gsonBuilder.setDateFormat("yyyy/MM/dd HH:mm:ss").create();
	}

	public static JsonEntity parseObj2JsonEntity(Object obj, Context con,
			boolean useDefaultToastMsg) {
		JsonEntity jsonEntity = null;
		if (obj != null) {
			if ("null".equals(obj)) {
				try {
					jsonEntity = new JsonEntity();
					jsonEntity.setStatus(401);
					//jsonEntity.setData(null);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				try {
					jsonEntity = GsonUtil.getGson().fromJson(obj.toString(),
							JsonEntity.class);

				} catch (com.google.gson.JsonSyntaxException e) {
					e.printStackTrace();
					Log.e("error occured!!!",
							"JsonSyntaxException 返回字符串格式不正确，未能转换为jsonEntity，字符串为:" + obj);
					jsonEntity = new JsonEntity();
					jsonEntity.setStatus(401);
				} catch (java.lang.IllegalStateException e4) {
					Log.e("error occured!!!",
							"IllegalStateException 返回字符串格式不正确，未能转换为jsonEntity，字符串为：" + obj);
					jsonEntity = new JsonEntity();
					jsonEntity.setStatus(401);
				}
			}
		} else {

			Log.e("error occured!!!!!!!!!!!!", "返回空串" + obj);
			jsonEntity = new JsonEntity();
			jsonEntity.setStatus(401);
		}
		showToastMsg(jsonEntity, con, useDefaultToastMsg);
		// com.google.gson.JsonSyntaxException
		return jsonEntity;
	}
	
	/**
	 * 由于返回来的data数据List没有被Json化，在这里多转一次
	 * @param jsonEntity
	 * @param type
	 * @return
	 */
    public static Object getData(JsonEntity jsonEntity,Type type)
    {
    	String jstr=GsonUtil.getGson().toJson(jsonEntity.getData());
		return AppConstants.gson.fromJson(jstr,type);
    }

	private static void showToastMsg(JsonEntity jsonEntity, Context con,
			boolean useDefaultToastMsg) {
		if (useDefaultToastMsg) {
			if (jsonEntity.getStatus() == 0) {
				Toast.makeText(con, "未查询到数据", Toast.LENGTH_SHORT).show();
			} else if (jsonEntity.getStatus() == 2) {
				Toast.makeText(con, "服务端异常", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
