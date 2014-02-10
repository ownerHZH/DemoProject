package com.hiibox.houseshelter.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.hiibox.houseshelter.R;

    
  
  
  
  
  
  
  
public class VoiceDialog extends Dialog implements android.view.View.OnClickListener {

    public VoiceDialog(Context context, int theme) {
        super(context, R.style.voiceDialogStyle);
    }

    public VoiceDialog(Context context) {
        super(context);
        setContentView(R.layout.voice_dialog_layout);
        findViewById(R.id.stop_voice_iv).setOnClickListener(this);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

}
