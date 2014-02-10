package com.hiibox.houseshelter.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.core.FileNameGenerator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.IBreakUtil;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.ShareUtil;
import com.hiibox.houseshelter.util.SinaWeiboUtil;
import com.hiibox.houseshelter.util.TencentWeiboUtil;
import com.hiibox.houseshelter.util.WeiboListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

    
  
  
  
  
  
  
  
public class ShareActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.send_to_sina_layout, click = "onClick") LinearLayout sinaLayout;
    @ViewInject(id = R.id.send_to_tencent_layout, click = "onClick") LinearLayout tencentLayout;
    @ViewInject(id = R.id.send_to_cycle_of_friends_layout, click = "onClick") LinearLayout friendsLayout;
    @ViewInject(id = R.id.cancel_tv, click = "onClick") TextView cancelTV;
    
    private String picPath = null;                    
    private String lon = "";
    private String lat = "";
    private String weiboContent = null;
    private boolean sinaWeiboAuth = false;
    private boolean tencentWeiboAuth = false;
    private IWXAPI api = null;
    private static final int THUMB_SIZE = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPicPath();
        getLocation();
        weiboContent = getResources().getString(R.string.weibo_content);
        sinaWeiboAuth = SinaWeiboUtil.getInstance(mActivity).isAuth();
                 
        api = WXAPIFactory.createWXAPI(this, ShareUtil.WECHAT_API_ID);
        api.registerApp(ShareUtil.WECHAT_API_ID);
        tencentWeiboAuth = TencentWeiboUtil.getInstance(mActivity).isAuth();
        setContentView(R.layout.activity_share_layout);
    }

        
  
  
    private void getLocation() {
        Location location = LocationUtil.getCurrentLocation(mContext);
        if (location != null) {
            lon = location.getLongitude() + "";
            lat = location.getLatitude() + "";
        }
    }

        
  
  
    private void getPicPath() {
        String picUrl = getIntent().getStringExtra("picUrl");           
        String picMD5Name = FileNameGenerator.generator(picUrl);
        picPath = GlobalUtil.IMAGE_PATH + "/" + picMD5Name + ".0";            
        File picFile = new File(picPath);
        if (picFile.exists()) {             
            String format = picUrl.substring(picUrl.lastIndexOf("."), picUrl.length());
            if (!format.equals(".png") || !format.equals(".jpeg") || !format.equals(".gif")) {
                format = ".png";
            }
            picPath = GlobalUtil.IMAGE_PATH + "/" + picMD5Name + format;
            picFile.renameTo(new File(picPath));
        } else {                         
            String pngPath = GlobalUtil.IMAGE_PATH + "/" + picMD5Name + ".png";
            File pngFile = new File(pngPath);
            if (pngFile.exists()) {
                picPath = pngPath;
            }
            
            String jpegPath = GlobalUtil.IMAGE_PATH + "/" + picMD5Name + ".jpeg";
            File jpegFile = new File(jpegPath);
            if (jpegFile.exists()) {
                picPath = jpegPath;
            }
            
            String gifPath = GlobalUtil.IMAGE_PATH + "/" + picMD5Name + ".gif";
            File gifFile = new File(gifPath);
            if (gifFile.exists()) {
                picPath = gifPath;
            }
            
            if (picPath.endsWith(".0")) {
                Bitmap bitmap = finalBitmap.getBitmapFromMemoryCache(picUrl);
                if (null != bitmap) {
                    String format = picUrl.substring(picUrl.lastIndexOf("."), picUrl.length());
                    if (!format.equals(".png") || !format.equals(".jpeg") || !format.equals(".gif")) {
                        format = ".png";
                    }
                    picPath = GlobalUtil.IMAGE_PATH + "/" + picMD5Name + format;
                    File file = new File(picPath);
                    FileOutputStream fos = null;
                    try {
                        file.createNewFile();
                        fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (null != fos) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        Log.i("ShareActivity", "getPicPath()    picPath = "+picPath);
    }
    
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.send_to_sina_layout:
                if (sinaWeiboAuth) {
                    if (picPath.endsWith(".0")) {
                        MessageUtil.alertMessage(mContext, R.string.picture_is_not_exist);
                    } else {
                        SinaWeiboUtil.getInstance(mActivity).upload(weiboContent, picPath, lon, lat);            
                    }
                    MianActivity.getScreenManager().exitActivity(mActivity);
                } else {
                    SinaWeiboUtil.getInstance(mActivity).auth(new WeiboListener(){         
                        @Override
                        public void onResult() {
                            super.onResult();
                            if (picPath.endsWith(".0")) {
                                MessageUtil.alertMessage(mContext, R.string.picture_is_not_exist);
                            } else {
                                SinaWeiboUtil.getInstance(mActivity).upload(weiboContent, picPath, lon, lat);            
                            }
                            MianActivity.getScreenManager().exitActivity(mActivity);
                        }
                    });
                }
                break;
            case R.id.send_to_tencent_layout:
                if (tencentWeiboAuth) {
                    if (picPath.endsWith(".0")) {
                        MessageUtil.alertMessage(mContext, R.string.picture_is_not_exist);
                    } else {
                        TencentWeiboUtil.getInstance(mActivity).addPicWeibo(weiboContent, picPath, lon, lat, 1, 0);            
                    }
                    MianActivity.getScreenManager().exitActivity(mActivity);
                } else {
                    TencentWeiboUtil.getInstance(mActivity).auth(new WeiboListener(){         
                        @Override
                        public void onResult() {
                            super.onResult();
                            if (picPath.endsWith(".0")) {
                                MessageUtil.alertMessage(mContext, R.string.picture_is_not_exist);
                            } else {
                                TencentWeiboUtil.getInstance(mActivity).addPicWeibo(weiboContent, picPath, lon, lat, 1, 0);            
                            }
                            MianActivity.getScreenManager().exitActivity(mActivity);
                        }
                    });
                }
                break;
            case R.id.send_to_cycle_of_friends_layout:
                if (picPath.endsWith(".0")) {
                    MessageUtil.alertMessage(mContext, R.string.picture_is_not_exist);
                } else {
                    Bitmap bmp = IBreakUtil.imageZoom(ImageOperation.getLoacalBitmap(picPath));
                    Log.i("ShareActivity", "send_to_cycle_of_friends  bmp = "+bmp+" ; LoacalBitmap = "+ImageOperation.getLoacalBitmap(picPath));
                    if (null == bmp) {
                        MessageUtil.alertMessage(mContext, R.string.picture_is_not_exist);
                    } else {
                        WXImageObject imgObj = new WXImageObject(bmp);
                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = imgObj;
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                        msg.thumbData = IBreakUtil.bmpToByteArray(thumbBmp, true);          
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("img");
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                        api.sendReq(req);
                    }
                    MianActivity.getScreenManager().exitActivity(mActivity);
                }
                break;
            case R.id.cancel_tv:
                MianActivity.getScreenManager().exitActivity(mActivity);
                break;
            default:
                break;
        }
    }
    
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MianActivity.getScreenManager().exitActivity(mActivity);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ShareActivity","===onActivityResult===  requestCode = "+ requestCode);
                    
        if (requestCode == 32973) {         
            SinaWeiboUtil.getInstance(mActivity).authCallBack(requestCode, resultCode, data);
        } else if (requestCode == 1) {             
            TencentWeiboUtil.getInstance(mActivity).webAuthOnResult();
        } else if (requestCode == 5657) {            
            TencentWeiboUtil.getInstance(mActivity).authOnResult(requestCode, resultCode, data);
        }
    }
}
