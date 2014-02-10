package com.hiibox.houseshelter.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;

    
  
  
  
  
  
  
  
public class VoiceDialogActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.stop_voice_iv, click = "onClick") ImageView stopVoiceIV;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_dialog_layout);
    }
    
    public void onClick(View v) {
        if (v == stopVoiceIV) {
        	this.setResult(RESULT_OK);
            MianActivity.getScreenManager().exitActivity(mActivity);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
