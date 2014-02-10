package com.hiibox.houseshelter.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.util.LogUtil;
import com.hiibox.houseshelter.util.ShareUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

    
  
  
  
  
  
public class WXEntryActivity extends ShaerlocActivity implements IWXAPIEventHandler{
	
	                                 
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                                          
        
                                        
    	api = WXAPIFactory.createWXAPI(this, ShareUtil.WECHAT_API_ID, false);
    	                                               
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	                         
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			LogUtil.log("bluebox", "COMMAND_GETMESSAGE_FROM_WX");
			                  
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			LogUtil.log("bluebox", "COMMAND_SHOWMESSAGE_FROM_WX");
			                                            
			break;
		default:
			LogUtil.log("bluebox", "COMMAND_else ");
			break;
		}
	}

	                                 
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		LogUtil.log("bluebox", "result="+resp.errCode);
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		MianActivity.getScreenManager().exitActivity(mActivity);
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}