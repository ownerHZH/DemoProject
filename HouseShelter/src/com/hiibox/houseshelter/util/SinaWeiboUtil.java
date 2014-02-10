package com.hiibox.houseshelter.util;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.hiibox.houseshelter.R;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;

    
  
  
  
  
  
public class SinaWeiboUtil {

	private static final String TAG = "SinaWeiboUtil";

	private static Context mContext;
	private static Activity mActivity;
	private static SinaWeiboUtil mInstantce;

	private static Weibo mWeibo;

	       
	private static Oauth2AccessToken mAccessToken;

	       
	private static SsoHandler mSsoHandler;

	private WeiboListener listener;
	
                            
                               

	public SinaWeiboUtil() {
		mWeibo = Weibo.getInstance(ShareUtil.SINA_WEIBO_API_ID,
				ShareUtil.WEIBO_REDICT_URL);
                                                                   
                                  
                                                  
                                                 
            
	}

	public static SinaWeiboUtil getInstance(Activity activity) {
		mActivity = activity;
		mContext = activity.getApplicationContext();
		if (mInstantce == null) {
			mInstantce = new SinaWeiboUtil();
		}
		return mInstantce;
	}

	    
  
  
  
	@SuppressLint("SimpleDateFormat")
	public boolean isAuth() {
		String token = PreferenceUtil.getInstance(mContext).getString(
				ShareUtil.PREF_SINA_ACCESS_TOKEN, "");
		long expiresTime = PreferenceUtil.getInstance(mContext).getLong(
				ShareUtil.PREF_SINA_EXPIRES_TIME, 0);
		String uid = PreferenceUtil.getInstance(mContext).getString(
				ShareUtil.PREF_SINA_UID, "");
		String userName = PreferenceUtil.getInstance(mContext).getString(
				ShareUtil.PREF_SINA_USER_NAME, "");
		String remindIn = PreferenceUtil.getInstance(mContext).getString(
				ShareUtil.PREF_SINA_REMIND_IN, "");
		mAccessToken = new Oauth2AccessToken();
		mAccessToken.setToken(token);
		mAccessToken.setExpiresTime(expiresTime);
		Log.i(TAG, "isAuth()~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
		Log.i(TAG, "accessToken = " + mAccessToken);
		Log.i(TAG, "accessToken.getToken() = " + mAccessToken.getToken());
		Log.i(TAG, "accessToken.getExpiresTime() = " + mAccessToken.getExpiresTime());
		Log.i(TAG, "uid = " + uid);
		Log.i(TAG, "userName = " + userName);
		Log.i(TAG, "remindIn = " + remindIn);

		if (mAccessToken.isSessionValid()) {            
			String date = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
					.format(new java.util.Date(mAccessToken.getExpiresTime()));
			Log.i(TAG, "access_token 仍在有效期内,无需再次登录: \naccess_token:"
					+ mAccessToken.getToken() + "\n有效期：" + date + "\nuid:"
					+ uid + "\nuserName:" + userName + "\nremindIn:" + remindIn);
			return true;
		} else {
			Log.i(TAG, "使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，"
					+ "目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
			return false;
		}
	}

	    
  
  
  
  
	public void auth(WeiboListener l) {
		Log.i(TAG, "auth()~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
		         
		mSsoHandler = new SsoHandler(mActivity, mWeibo);
		mSsoHandler.authorize(new AuthDialogListener());
		if(l!=null){
			listener = l;
		}else{
			listener = new WeiboListener();
		}

		             
		                                                         
	}
	
	    
  
  
  
  
	class AuthDialogListener implements WeiboAuthListener {

        public AuthDialogListener() {
            super();
        }
        
		@Override
		public void onCancel() {
			MessageUtil.alertMessage(mContext, R.string.user_auth_cancel);
		}

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString(ShareUtil.SINA_ACCESS_TOKEN);
			String uid = values.getString(ShareUtil.SINA_UID);
			String userName = values.getString(ShareUtil.SINA_USER_NAME);
			String expiresIn = values.getString(ShareUtil.SINA_EXPIRES_IN);                                      
			String remindIn = values.getString(ShareUtil.SINA_REMIND_IN);
			Log.i(TAG, "isSessionValid~~~~~~~token = " + token + " uid = " + uid
                + " userName = " + userName + " expiresIn = " + expiresIn + " remindIn = "
                + remindIn);
			mAccessToken = new Oauth2AccessToken(token, expiresIn);
			if (mAccessToken.isSessionValid()) {
                                              
                                                        
                    
			    
				PreferenceUtil.getInstance(mContext).saveString(
						ShareUtil.PREF_SINA_ACCESS_TOKEN, token);
				PreferenceUtil.getInstance(mContext).saveString(
						ShareUtil.PREF_SINA_UID, uid);
				PreferenceUtil.getInstance(mContext).saveLong(
						ShareUtil.PREF_SINA_EXPIRES_TIME,
						mAccessToken.getExpiresTime());             
				PreferenceUtil.getInstance(mContext).saveString(
						ShareUtil.PREF_SINA_REMIND_IN, remindIn);
				if (TextUtils.isEmpty(userName)) {
					show(Long.parseLong(uid));
				} else {
					PreferenceUtil.getInstance(mContext).saveString(
							ShareUtil.PREF_SINA_USER_NAME, userName);
					if (listener != null) {
						listener.onResult();
					}
				}
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
		    Log.i(TAG, "AuthDialogListener  onError() message = " + e.getMessage());
			MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_send_failed) + e.getMessage());
		}

		@Override
		public void onWeiboException(WeiboException e) {
		    Log.i(TAG, "AuthDialogListener  onWeiboException() message = " + e.getMessage());
			MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_send_failed) + e.getMessage());
		}
	}

	    
  
  
  
  
  
  
	public void authCallBack(int requestCode, int resultCode, Intent data) {
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	    
  
  
  
  
	public void show(long uid) {
		SinaWeiboAPI api = new SinaWeiboAPI(mAccessToken);
		api.show(uid, new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				Log.i(TAG, "onIOException---e = " + e.getMessage());
				MessageUtil.alertMessage(mContext, mContext.getString(R.string.user_auth_fail) + e.getMessage());
			}

			@Override
			public void onError(WeiboException e) {
				Log.i(TAG, "WeiboException---e = " + e.getMessage());
				MessageUtil.alertMessage(mContext, mContext.getString(R.string.user_auth_fail) + e.getMessage());
			}

			@Override
			public void onComplete(String json) {
				JSONObject object;
				try {
					object = new JSONObject(json);
					String userName = object.optString(ShareUtil.SINA_NAME);
					Log.i(TAG, "show---onComplete---userName = " + userName);
					PreferenceUtil.getInstance(mContext).saveString(
							ShareUtil.PREF_SINA_USER_NAME, userName);
					if (listener != null) {
						listener.onResult();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	    
  
  
  
  
  
  
  
  
  
  
	public void update(String content, String lat, String lon) {
	    Log.i(TAG, "update()    content = "+content+" ; lat = "+lat+" ; lon = "+lon);
		SinaWeiboAPI api = new SinaWeiboAPI(mAccessToken);
		api.update(content, lat, lon, new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				Log.i(TAG, "onIOException---e = " + e.getMessage());
				Looper.prepare();
				MessageUtil.alertMessage(mContext, mContext.getString(R.string.user_auth_fail) + e.getMessage());
				Looper.loop();
			}

			@Override
			public void onError(WeiboException e) {
				Log.i(TAG, "onError---e = " + e.getMessage()
						+ " e.getStatusCode() = " + e.getStatusCode());
				Looper.prepare();
				if (e.getStatusCode() == 400) {                 
					MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_error_repeat_content)
							+ e.getMessage());
				} else {
					MessageUtil.alertMessage(mContext,
							mContext.getString(R.string.user_auth_fail)+ e.getMessage());
				}
				Looper.loop();
			}

			@Override
			public void onComplete(String str) {
				Log.i(TAG, "onComplete---str = " + str);
				Looper.prepare();
				MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_send_sucess));
				Looper.loop();
			}
		});
	}
	
	    
  
  
  
  
  
  
  
  
  
  
	public void upload(String content, String picUrl, String lat, String lon) {
	    Log.i(TAG, "upload()    content = "+content+" ; picUrl = "+picUrl+" ; lat = "+lat+" ; lon = "+lon);
	    SinaWeiboAPI api = new SinaWeiboAPI(mAccessToken);
	    api.upload(content, picUrl, lat, lon, new RequestListener() {
	        
	        @Override
	        public void onIOException(IOException e) {
	            Log.i(TAG, "onIOException---e = " + e.getMessage());
	            MessageUtil.alertMessage(mContext, mContext.getString(R.string.user_auth_fail) + e.getMessage());
	        }
	        
	        @Override
	        public void onError(WeiboException e) {
	            Log.i(TAG, "onError---e = " + e.getMessage()
	                + " e.getStatusCode() = " + e.getStatusCode());
	            Looper.prepare();
	            if (e.getStatusCode() == 400) {                 
	                MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_error_repeat_content)
	                    + e.getMessage());
	            } else {
	                MessageUtil.alertMessage(mContext,
	                    mContext.getString(R.string.user_auth_fail)+ e.getMessage());
	            }
	            Looper.loop();
	        }
	        
	        @Override
	        public void onComplete(String str) {
	            Log.i(TAG, "onComplete---str = " + str);
	            Looper.prepare();
	            MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_send_sucess));
	            Looper.loop();
	        }
	    });
	}
	
	    
  
  
  
  
  
  
  
  
  
  
	public void uploadUrlText(String content, String picUrl, String lat, String lon) {
	    Log.i(TAG, "uploadUrlText()    content = "+content+" ; picUrl = "+picUrl+" ; lat = "+lat+" ; lon = "+lon);
	    SinaWeiboAPI api = new SinaWeiboAPI(mAccessToken);
	    api.uploadUrlText(content, picUrl, lat, lon, new RequestListener() {
	        
	        @Override
	        public void onIOException(IOException e) {
	            Log.i(TAG, "onIOException---e = " + e.getMessage());
	            MessageUtil.alertMessage(mContext, mContext.getString(R.string.user_auth_fail) + e.getMessage());
	        }
	        
	        @Override
	        public void onError(WeiboException e) {
	            Log.i(TAG, "onError---e = " + e.getMessage()
	                + " e.getStatusCode() = " + e.getStatusCode());
	            Looper.prepare();
	            if (e.getStatusCode() == 400) {                 
	                MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_error_repeat_content)
	                    + e.getMessage());
	            } else {
	                MessageUtil.alertMessage(mContext,
	                    mContext.getString(R.string.user_auth_fail)+ e.getMessage());
	            }
	            Looper.loop();
	        }
	        
	        @Override
	        public void onComplete(String str) {
	            Log.i(TAG, "onComplete---str = " + str);
	            Looper.prepare();
	            MessageUtil.alertMessage(mContext, mContext.getString(R.string.weibosdk_send_sucess));
	            Looper.loop();
	        }
	    });
	}

	    
  
  
  
  
	public void logout(WeiboListener l) {
		PreferenceUtil.getInstance(mContext).remove(
				ShareUtil.PREF_SINA_ACCESS_TOKEN);
		l.onResult();
	}


}
