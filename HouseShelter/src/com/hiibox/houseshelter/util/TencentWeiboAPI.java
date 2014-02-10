package com.hiibox.houseshelter.util;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

    
  
  
  
  
  
public class TencentWeiboAPI {

	    
  
  
                                                                        
	public static final String API_SERVER = "https://graph.qq.com";

	    
  
  
	public static final String HTTPMETHOD_POST = "POST";

	    
  
  
	public static final String HTTPMETHOD_GET = "GET";

	private TencentTO tencentTO;
	private FinalHttp finalHttp ;
	    
  
  
	public TencentWeiboAPI(TencentTO tencentTO) {
		this.tencentTO = tencentTO;
		finalHttp = new FinalHttp();
	}

	    
  
  
  
  
  
  
  
	private void request(final String url, final AjaxParams params,
			final String httpMethod, AjaxCallBack<String> callBack) {
		params.put(ShareUtil.TX_API_APP_KEY, tencentTO.getAppkey());
		params.put(ShareUtil.TX_API_ACCESS_TOKEN, tencentTO.getAccessToken());
		params.put(ShareUtil.TX_API_OPEN_ID, tencentTO.getOpenId());
		                                                                   
		                                                     
		                                             
		params.put(ShareUtil.TX_API_FORMAT, "json");                      
		LogUtil.log("", ShareUtil.TX_API_OPEN_ID + "=" + tencentTO.getOpenId());
		                                                               
		if(httpMethod.equals(HTTPMETHOD_GET)){
			finalHttp.get(url,params, callBack);
		}else{
			finalHttp.post(url, params, callBack);
		}
	}

	    
  
  
  
  
  
  
  
  
	public void addWeibo(String content, String longitude, String latitude,
			int syncflag, int compatibleflag, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put(ShareUtil.TX_API_CONTENT, content);
		params.put(ShareUtil.TX_API_LONGITUDE, longitude);
		params.put(ShareUtil.TX_API_LATITUDE, latitude);
		params.put(ShareUtil.TX_API_SYNCFLAG, ""+syncflag);
		params.put(ShareUtil.TX_API_COMPATIBLEFLAG, ""+compatibleflag);
		request(API_SERVER + "/t/add_t", params, HTTPMETHOD_POST, listener);
	}

	    
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
	public void addPicWeibo(String content, String picPath, String longitude, String latitude,
			int syncflag, int compatibleflag, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put(ShareUtil.TX_API_CONTENT, content);
		params.put(ShareUtil.TX_API_LONGITUDE, longitude);
		params.put(ShareUtil.TX_API_LATITUDE, latitude);
		params.put(ShareUtil.TX_API_SYNCFLAG, ""+syncflag);
		params.put(ShareUtil.TX_API_COMPATIBLEFLAG, ""+compatibleflag);
		try {
			params.put(ShareUtil.TX_API_PIC, getLoaclFile(picPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		request(API_SERVER + "/t/add_pic_t", params, HTTPMETHOD_POST, listener);
	}
	
	    
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
	  








  
  

	    
  
  
  
  
	public void getUserInfo(AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		request(API_SERVER + "/user/get_user_info", params, HTTPMETHOD_GET, listener);
	}
	
	public void upLoadPic(String content, String picUrl,
			 AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put("photodesc", content);
		try {
			params.put("picture", getLoaclFile(picUrl));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		request(API_SERVER + "/photo/upload_pic", params, HTTPMETHOD_POST, listener);
		
	}
	public  File getLoaclFile(String path){
		File file = new File(path);
		if(file.exists()){
			return file;
		}
		return null;
	}

}
