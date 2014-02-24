package com.zgan.yckz.activity;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.List;

import com.zgan.yckz.R;

import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.socket.SanySocketClient;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tcp.FrameTools;
import com.zgan.yckz.tools.YCKZ_Activity;
import com.zgan.yckz.tools.YCKZ_NetworkDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class User_Reg extends Activity {
	/**
	 * 用户手机号
	 */
	EditText user_tel;
	/**
	 * 用户密码
	 */
	EditText user_pass;
	/**
	 * 用户再次确认密码
	 */
	EditText reuser_pass;
	/**
	 * 用户住址
	 */
	EditText user_address;
	/**
	 * 用户名
	 */
	EditText user_name;
	/**
	 * 用户设备号
	 */
	EditText user_shebei;
	/**
	 * 取消注册
	 */
	ImageView user_cancel;
	/**
	 * 提交
	 */
	ImageView user_commit;
	/**
	 * 二维码
	 */
	ImageView erweima;
	private static List<SanySocketClient> clientList;
	Handler SubDatahandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			switch (msg.what) {
			case Constant.DATAERROR:
				System.out.println("NewSocketInfo  Constant.DATAERROR  "
						+ msg.obj);
				break;
			case Constant.CHECKCODEERROR:
				System.out.println("NewSocketInfo  Constant.CHECKCODEERROR "
						+ msg.obj);
				break;
			case Constant.NETERROR:
				System.out.println("NewSocketInfo  Constant.NETERROR  "
						+ msg.obj);
				break;
			case Constant.SERVERERROR:
				System.out.println("NewSocketInfo  Constant.SERVERERROR "
						+ msg.obj);
				break;
			case Constant.DATASUCCESS:
				byte[] buffer;
				// buffer = new byte[Constant.MSG_NUM];
				// Arrays.fill(buffer, (byte)0);
				buffer = (byte[]) msg.obj;

				Log.i("buffer", "" + buffer);
				Frame frame = new Frame(buffer);
				System.out.println("接收数据...平台代码" + frame.Platform);
				System.out.println("接收数据...版本号" + frame.Version);
				System.out.println("接收数据...主功能命令字" + frame.MainCmd);
				System.out.println("接收数据...子功能命令字" + frame.SubCmd);
				System.out.println("接收数据...数据" + frame.strData);

				System.out.println("NewSocketInfo  Constant.DATASUCCESS "
						+ buffer);

				Toast.makeText(User_Reg.this, "" + frame.strData.substring(1),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_reg);

		//User_Login.setHnadler(SubDatahandler);

		user_tel = (EditText) findViewById(R.id.user_tel);
		user_pass = (EditText) findViewById(R.id.user_pasword);
		reuser_pass = (EditText) findViewById(R.id.reuser_pasword);
		user_name = (EditText) findViewById(R.id.user_name);
		user_address = (EditText) findViewById(R.id.user_add);
		user_shebei = (EditText) findViewById(R.id.user_shebei);

		user_cancel = (ImageView) findViewById(R.id.cancel);
		user_commit = (ImageView) findViewById(R.id.commit);
		erweima = (ImageView) findViewById(R.id.erweima);

		user_cancel.setOnClickListener(listener);
		user_commit.setOnClickListener(listener);
		erweima.setOnClickListener(listener);
		Detectnetwork();
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) getSystemService(User_Reg.this.TELEPHONY_SERVICE);
		user_tel.setText(mTelephonyMgr.getLine1Number());
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.cancel:
				finish();
				break;
			case R.id.commit:
				Reg();
				break;
			case R.id.erweima:
				Intent intent = new Intent(User_Reg.this, CaptureActivity.class);
				startActivityForResult(intent, 100);
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (20 == resultCode) {
			String id = data.getExtras().getString("id");
			user_shebei.setText(id);
			Log.i("id", id);
		}
		super.onActivityResult(requestCode, resultCode, data);

	};

	protected void Reg() {
		// TODO Auto-generated method stub

		if (!user_pass.getText().toString()
				.equals(reuser_pass.getText().toString())) {
			Toast.makeText(User_Reg.this, "两次密码不一样", Toast.LENGTH_SHORT).show();
		} else {
			
			try {
				SendTestInfo();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void SendTestInfo() throws Exception {
		// HandleClient();

		String strLoginInfo = user_tel.getText().toString() + "\t"
				+ user_pass.getText().toString() + "\t" + "2B2B2B2B2B2B2B2B"
				+ "\t" + 66666666 + "\t"
				+ (new String(("重庆").getBytes("GBK"), "ISO8859_1")) + "\t"
				+ (new String(("重庆").getBytes("GBK"), "ISO8859_1"));
		byte[] logininfo = strLoginInfo.getBytes();

		// System.out.println("NewSocketInfo"+"#############################    "+logininfo);
		boolean bSend = User_Login.SendData(Constant.LOGIN_SERVERPLATFROM,
				logininfo, strLoginInfo.length(), Constant.LOGIN,
				Constant.INDEX_cbMainCmdID, Constant.VERSION);
		if (bSend) {
			System.out.println("NewSocketInfo  " + strLoginInfo);
		}

	}

	/**
	 * 检测网络是否可用
	 */
	protected void Detectnetwork() {
		// TODO Auto-generated method stub
		Boolean networkState = YCKZ_NetworkDetector.detect(User_Reg.this);

		if (!networkState) {
			new AlertDialog.Builder(this)
					.setTitle("网络错误")
					.setMessage("网络连接失败，请确认网络连接")
					.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
								}
							}).show();
		}
	}
}