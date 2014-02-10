package com.hiibox.houseshelter.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.hiibox.houseshelter.R;

public class GestureDialog extends Dialog {

    private Activity activity = null;
	
	public GestureDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public GestureDialog(Context context, int theme) {
		super(context, theme);
	}

	public GestureDialog(Activity activity, View splashView) {
		super(activity, R.style.gestureDialogStyle);
		this.activity = activity;
		init(activity, splashView);
	}
	
	private void init(Context context, View splashView) {
		this.setContentView(splashView);
		this.setCancelable(false);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    Log.e("GestureDialog", "onKeyDown()  isShowing = "+isShowing());
	    if (isShowing()) {
	        dismiss();
	        activity.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
