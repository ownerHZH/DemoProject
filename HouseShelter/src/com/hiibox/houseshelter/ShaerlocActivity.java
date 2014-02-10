package com.hiibox.houseshelter;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;

public class ShaerlocActivity extends FinalActivity {

    public static FinalBitmap finalBitmap = null;
    public static FinalHttp finalHttp = null;
    public Context mContext = null;
    public Activity mActivity = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity = this;
        mContext = mActivity.getApplicationContext();
        MianActivity.getScreenManager().addActivity(mActivity);
        finalBitmap = FinalBitmap.create(mContext, GlobalUtil.IMAGE_PATH);
        finalBitmap.configLoadfailImage(R.drawable.default_load_error_picture);
        finalBitmap.configLoadingImage(R.drawable.default_loading_picture);
        finalHttp = new FinalHttp();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }


}
