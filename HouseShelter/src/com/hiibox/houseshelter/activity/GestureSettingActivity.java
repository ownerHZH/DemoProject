package com.hiibox.houseshelter.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;
import com.hiibox.houseshelter.view.NineGridsView;
import com.hiibox.houseshelter.view.NineGridsView.OnCompleteListener;

    
  
  
  
  
  
  
  
public class GestureSettingActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.root_layout) LinearLayout rootLayout;
    @ViewInject(id = R.id.prompt_tv) TextView promptTV;
    @ViewInject(id = R.id.lock_screen_view) NineGridsView ninePoints;
    
    private boolean needVerify = true;
    private String currPassword = null;
    private boolean enterCloudEyes = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_setting_layout);
        enterCloudEyes = getIntent().getBooleanExtra("enterCloudEyes", false);
        currPassword = PreferenceUtil.getInstance(mContext).getString("gestureTracks", null);
        if (StringUtil.isEmpty(currPassword)) {                  
            needVerify = false;
            promptTV.setText(R.string.draw_gesture);
        } else {           
            promptTV.setText(R.string.draw_current_safe_gesture);
            ninePoints.setIsUnlock(true);
        }
        
        ninePoints.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String password) {
                if (needVerify) {
                    if (currPassword.equals(password)) {                 
                        if (enterCloudEyes) {
                                                                                             
                            Intent intent = new Intent();
                            intent.putExtra("unlockSuccess", true);
                            GestureSettingActivity.this.setResult(RESULT_OK, intent);
                            MianActivity.getScreenManager().exitActivity(mActivity);
                            return;
                        } else {
                            promptTV.setText(R.string.draw_gesture);
                            ninePoints.setIsUnlock(false);
                            needVerify = false;
                        }
                    } else {         
                        MessageUtil.alertMessage(mContext, R.string.unlock_failure);
                    }
                    ninePoints.clearUnlockInfo();
                    ninePoints.clearPassword();
                    password = null;
                } else {
                    MessageUtil.alertMessage(mContext, R.string.set_gesture_success);
                                            
                        Intent intent = new Intent();
                        intent.putExtra("unlockSuccess", true);
                        GestureSettingActivity.this.setResult(RESULT_OK, intent);
                        
                    MianActivity.getScreenManager().exitActivity(mActivity);
                }
            }
        });
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && enterCloudEyes) {
            Intent intent = new Intent();
            intent.putExtra("unlockSuccess", false);
            setResult(RESULT_OK, intent);
            MianActivity.getScreenManager().exitActivity(mActivity);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                                              
                                                                             
                                                            
    }
    
    @Override
    protected void onStop() {
        super.onStop();
                                                  
    }
    
}
