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

public class CloseGestureActivity extends ShaerlocActivity implements OnCompleteListener {

    @ViewInject(id = R.id.root_layout) LinearLayout rootLayout;
    @ViewInject(id = R.id.prompt_tv) TextView promptTV;
    @ViewInject(id = R.id.lock_screen_view) NineGridsView ninePoints;
    
    private String currPassword = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_setting_layout);
        currPassword = PreferenceUtil.getInstance(mContext).getString("gestureTracks", null);
        if (StringUtil.isEmpty(currPassword)) {
            Intent intent = new Intent();
            intent.putExtra("allowCloseGesture", true);
            this.setResult(RESULT_OK, intent);
            MianActivity.getScreenManager().exitActivity(mActivity);
            return;
        } else {
            promptTV.setText(R.string.draw_current_safe_gesture);
            ninePoints.setIsUnlock(true);
        }
        
        ninePoints.setOnCompleteListener(this);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("allowCloseGesture", false);
            this.setResult(RESULT_OK, intent);
            MianActivity.getScreenManager().exitActivity(mActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onComplete(String password) {
        if (currPassword.equals(password)) {                 
            Intent intent = new Intent();
            intent.putExtra("allowCloseGesture", true);
            this.setResult(RESULT_OK, intent);
            MianActivity.getScreenManager().exitActivity(mActivity);
            return;
        } else {
            MessageUtil.alertMessage(mContext, R.string.unlock_failure);
        }
        ninePoints.clearUnlockInfo();
        ninePoints.clearPassword();
        password = null;
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
