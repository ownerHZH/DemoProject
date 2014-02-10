package com.hiibox.houseshelter.util;

import android.text.TextUtils;
import android.util.Log;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.AsyncWeiboRunner;
import com.weibo.sdk.android.net.RequestListener;

    
  
  
  
  
  
public class SinaWeiboAPI {

	    
  
  
	public static final String API_SERVER = "https://api.weibo.com/2";

	private static final String URL_USERS = API_SERVER + "/users";

	private static final String URL_STATUSES = API_SERVER + "/statuses";

	private static final String URL_ACCOUNT = API_SERVER + "/account";
	
	private static final String FRIENDSHIPS = API_SERVER + "/friendships";

	    
  
  
	public static final String HTTPMETHOD_POST = "POST";

	    
  
  
	public static final String HTTPMETHOD_GET = "GET";

	private Oauth2AccessToken oAuth2accessToken;

	private String accessToken;

	    
  
  
  
  
  
	public SinaWeiboAPI(Oauth2AccessToken oauth2AccessToken) {
		this.oAuth2accessToken = oauth2AccessToken;
		if (oAuth2accessToken != null) {
			accessToken = oAuth2accessToken.getToken();
		}
	}

	    
  
  
  
  
  
  
  
	private void request(final String url, final WeiboParameters params,
			final String httpMethod, RequestListener listener) {
		params.add("access_token", accessToken);
		AsyncWeiboRunner.request(url, params, httpMethod, listener);
	}

	    
  
  
  
  
  
  
	public void show(long uid, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		request(URL_USERS + "/show.json", params, HTTPMETHOD_GET, listener);
	}

	    
  
  
  
  
  
  
  
  
  
  
	public void update(String content, String lat, String lon,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("status", content);
		if (!TextUtils.isEmpty(lon)) {
			params.add("long", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			params.add("lat", lat);
		}
		request(URL_STATUSES + "/update.json", params, HTTPMETHOD_POST,
				listener);
	}

	    
  
  
  
  
  
  
  
  
  
  
  
  
	public void upload(String content, String file, String lat, String lon,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("status", content);
		params.add("pic", file);
		if (!TextUtils.isEmpty(lon)) {
			params.add("long", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			params.add("lat", lat);
		}
		request(URL_STATUSES + "/upload.json", params, HTTPMETHOD_POST,
				listener);
	}
	
	    
  
  
  
  
  
  
  
  
  
  
  
  
	public void uploadUrlText(String content, String picUrl, String lat, String lon,
	                   RequestListener listener) {
	    WeiboParameters params = new WeiboParameters();
	    params.add("status", content);
	    params.add("url", picUrl);
	    if (!TextUtils.isEmpty(lon)) {
	        params.add("long", lon);
	    }
	    if (!TextUtils.isEmpty(lat)) {
	        params.add("lat", lat);
	    }
	    request(URL_STATUSES + "/upload_url_text.json", params, HTTPMETHOD_POST,
	        listener);
	}

	    
  
  
  
  
	public void endSession(RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		request(URL_ACCOUNT + "/end_session.json", params, HTTPMETHOD_POST,
				listener);
	}
	
	    
  
  
  
  
  
  
  
  
	public void friends( long uid, int count, int cursor, boolean trim_status,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("source", ShareUtil.SINA_WEIBO_API_KEY);
		Log.i("source", ShareUtil.SINA_WEIBO_API_KEY);
		params.add("uid", uid);
		params.add("count", count);
		params.add("cursor", cursor);
		if (trim_status) {
			params.add("trim_status", 1);
		} else {
			params.add("trim_status", 0);
		}
		request( FRIENDSHIPS + "/friends.json", params, HTTPMETHOD_GET, listener);
	}
}
