package com.zgan.community.httptools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.a.a.c.c;

import android.text.Html;
import android.util.Log;

public class ZganZgDataTools {
	String data_type = "ZganJob.aspx";
	int did = 1;
	int sid = 1;
	String content;

	public ZganZgDataTools(String type, int Did, int Sid) {
		type = this.data_type;
		Did = this.did;
		Sid = this.sid;
		// TODO Auto-generated method stub
		HttpGet get = new HttpGet("http://community1.zgantech.com/" + data_type
				+ "?did=" + did + "&" + "sid=" + sid);

		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(get);
			HttpEntity entity = httpResponse.getEntity();
			StatusLine line = httpResponse.getStatusLine();
			Log.i("linecode", "" + line.getStatusCode());
			if (line.getStatusCode() == 200) {
				String st = EntityUtils.toString(entity);

				Log.i("st", "" + st);
				String ht = Html.fromHtml(st).toString();

				JSONObject jsonObject = new JSONObject(ht);

				String Data = jsonObject.getString("data");
				Log.i("Data", "" + Html.fromHtml(Data).toString());

				JSONArray array = new JSONArray(Data);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject2 = array.getJSONObject(1);
					content = jsonObject2.getString("content");

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
