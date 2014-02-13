
	
package com.zgan.community.tools;


import com.zgan.community.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProgressDialog extends Dialog {
	private Context context = null;
	private static MyProgressDialog myProgressDialog = null;
	
	public MyProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static MyProgressDialog createDialog(Context context){
		myProgressDialog = new MyProgressDialog(context,R.style.CustomProgressDialog);
		myProgressDialog.setContentView(R.layout.customprogressdialog);
		myProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		
		return myProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (myProgressDialog == null){
    		return;
    	}
    	
        ImageView imageView = (ImageView) myProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
 
    /**
     * 
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public MyProgressDialog setTitile(String strTitle){
    	return myProgressDialog;
    }
    
    /**
     * 
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public MyProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)myProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return myProgressDialog;
    }
    
    public void start(String str){
		if (myProgressDialog == null){
			myProgressDialog = MyProgressDialog.createDialog(context);
			myProgressDialog.setMessage(str);
		}		
		myProgressDialog.show();
	}
	
	public void stop(){
		if (myProgressDialog != null){
			myProgressDialog.dismiss();
			myProgressDialog = null;
		}
	}
}
