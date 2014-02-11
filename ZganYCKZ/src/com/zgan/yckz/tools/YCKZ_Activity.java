package com.zgan.yckz.tools;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

public class YCKZ_Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setTitle("是否退出程序")
					.setNegativeButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setPositiveButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).show();
			return true;

		}
		return super.onKeyDown(keyCode, event);

	}
	

}
