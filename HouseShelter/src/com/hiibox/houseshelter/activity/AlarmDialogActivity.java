package com.hiibox.houseshelter.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.util.DateUtil;

public class AlarmDialogActivity extends ShaerlocActivity {

	@ViewInject(id = R.id.alarm_time_tv) TextView timeIV;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_dialog_layout);
        timeIV.setText(getString(R.string.invade_warning)+" "+DateUtil.getTime());
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	MianActivity.getScreenManager().exitActivity(mActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
