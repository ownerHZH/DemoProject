package com.zgan.community.jsontool;


import com.zgan.community.R;
import com.zgan.community.tools.MyProgressDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;


public class DialogUtil {

	private Context context;

	public DialogUtil(Context context) {
		this.context = context;
	}

	// 历史上报长按的弹出框
	String result = null;

	public String showDialogForLishiShangbao() {
		final String[] arrayFruit = new String[] { "苹果", "橘子", "草莓", "香蕉" };
		Dialog alertDialog = new AlertDialog.Builder(context).setTitle("信息选项")
				.setIcon(null)//设置Dialog的ico图标
				.setItems(arrayFruit, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						result = arrayFruit[which];
						// Toast.makeText(context, arrayFruit[which],
						// Toast.LENGTH_SHORT).show();
						// 转到页面
						// Intent intent = new Intent();
						// intent.setClass(LishiShangbao.this,
						// ShijianXubao.class);
						// startActivity(intent);
						// LishiShangbao.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).create();
		alertDialog.show();
		return result;
	}

	public static void setAttr4progressDialog(MyProgressDialog pdialog) {
		/*pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		pdialog.setTitle(null);// 设置ProgressDialog // 标题
		pdialog.setMessage("正在访问网络中..."); // 设置ProgressDialog
						// 提示信息
		pdialog.setIcon(null);// 设置ProgressDialog
												// 标题图标
		pdialog.setIndeterminate(false);// 设置ProgressDialog
										// 的进度条是否不明确
		pdialog.setCancelable(true); // 设置ProgressDialog
		pdialog.setCanceledOnTouchOutside(false);// 这样一切都和4.0之前的一样
		pdialog.show(); // 让ProgressDialog显示*/
		pdialog.start("正在访问网络中...");
	}

}
