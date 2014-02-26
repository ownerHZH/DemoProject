package com.zgan.yckz.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;

import org.kobjects.base64.Base64;

import com.zgan.yckz.R;
import com.zgan.yckz.fragment.PublicFragment;
import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.socket.SanySocketClient;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tcp.FrameTools;
import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_Activity;
import com.zgan.yckz.tools.YCKZ_NetworkDetector;
import com.zgan.yckz.tools.YCKZ_SQLHelper;
import com.zgan.yckz.tools.YCKZ_Static;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class User_Login extends YCKZ_Activity {
	/**
	 * 手机号
	 */
	EditText go_tel;
	/**
	 * 密码
	 */
	EditText go_pass;
	/**
	 * 登陆
	 */
	ImageView go;
	/**
	 * 注册
	 */
	ImageView reg;
	/**
	 * 一键找回密码
	 */
	ImageView call_KF;
	
	Context context=User_Login.this;
	
	public static  boolean success;

	SharedPreferences preferences;
	SharedPreferences repreferences;
	SharedPreferences.Editor editor;
	private static List<SanySocketClient> clientList;
	
	ProgressDialog dialog;
	Dialog reStartDialog;
	List<String> list_id;
	List<String> list_details;
	List<SheBei> list_shebei;

	YCKZ_SQLHelper yckz_SQLHelper;
	SQLiteDatabase db;
	
	String mac = null;
	SheBei sheBei = new SheBei();
	
	public static String MAC=null;

    public Handler ClientDatahandler = new Handler() {
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
				//Toast.makeText(User_Login.this, "网络已断开或不可用！", Toast.LENGTH_LONG).show();
				break;
			case Constant.DATASUCCESS:
				Log.i("userlogin", "1");

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
				if (frame.MainCmd == 1 && frame.SubCmd == 3
						&& frame.Platform == 1) {
					if (frame.strData.substring(0, 1).equals("0")) {
						Toast.makeText(User_Login.this, "注册成功，正在登录。。。",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(User_Login.this,
								"" + frame.strData.substring(1),
								Toast.LENGTH_SHORT).show();
					}
				}
					if (frame.MainCmd == 1 && frame.SubCmd == 1
							&& frame.Platform == 1) {
						if (frame.strData.substring(0, 1).equals("0")) {
							/*Toast.makeText(User_Login.this, "正在登录。。。",
									Toast.LENGTH_SHORT).show();*/
						} else {
							dialog.dismiss();
							Toast.makeText(User_Login.this,
									"" + frame.strData.substring(1),
									Toast.LENGTH_SHORT).show();							
						}
				}
				if (frame.MainCmd == 1 && frame.SubCmd == 4
						&& frame.Platform == 1) {
					String str = frame.strData.substring(1);

					Log.i("(f.strData).substring(2)", "" + str);
					String Port[] = str.split("-");
					if (Port.length > 1) {

						for (int i = 1; i < Port.length; i++) {

							Log.i("Port[" + i + "]", "" + Port[i]);
							String[] data = Port[i].split(":");
							/*for (int j = 0; j < data.length; j++) {
								
								}*/
							String strip = ("-" + data[0]);
							String ipport = int2ip(Long.parseLong(strip));							
							int nport=Integer.parseInt(data[1]);
							int serverId=Integer.parseInt(data[2].replaceAll("\\D+","").replaceAll("\r", "").replaceAll("\n", "").trim(),10);
							Log.i("ipport", "" + ipport);
							Log.i("nport", "" + nport);
							Log.i("serverId", "" + serverId);
							
							
							try {
								User_Login.startNewClient(ipport, nport, ClientDatahandler,
										11, serverId, Constant.INDEX_cbMessageVer);
								System.out.println("NewSocketInfo" + "21011");
								Thread.sleep(100);

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();

								System.out.println("NewSocketInfo" + "e.printStackTrace() "
										+ e.getMessage());
							

							}
							
							
							

						}
						if (success) {
							YCKZ_Static.Phone_number=go_tel.getText().toString();
							YCKZ_Static.USER_password=go_pass.getText().toString();
							//账号密码保存到sharedperferce当中
							editor.putString("user_name", YCKZ_Static.Phone_number);
							editor.putString("user_pas", YCKZ_Static.USER_password);
							editor.commit();
							Login();
						}
						
					}
					
				}
						
						
						if (frame.MainCmd == 1 && frame.SubCmd == 0
								&& "0".equals(frame.strData)) {
							
						}

						if (frame.MainCmd == 3 && frame.SubCmd == 0
								&& "0".equals(frame.strData)) {
							GetChildList();
						}
						if (frame.Platform == 9) {
						
							if (frame.MainCmd == 1 && frame.SubCmd == 0) {
								Log.i("f.strData", "" + frame.strData);
								Intent intent = new Intent();
								intent.putExtra("strData", frame.strData);
								intent.setAction("com.zgan.yckz");
								sendBroadcast(intent);
							}
							if (frame.MainCmd == 1 && frame.SubCmd == 4
								) {

								YCKZ_Static.ChildDeviceID = frame.strData.split("\t");
								list_id = new ArrayList<String>();
								list_shebei = new ArrayList<SheBei>();
								list_details = new ArrayList<String>();

								
								for (int i = 0; i < YCKZ_Static.ChildDeviceID.length; i++) {
									String userinfo = YCKZ_Static.ChildDeviceID[i];
									Log.i("设备ID" + i, "" + userinfo);
									
									//list.add(publicFragment);
									String[] user = userinfo.split(",");
									list_id.add(userinfo);
									list_details = new ArrayList<String>();

									for (int j = 0; j < user.length; j++) {
										String SubDev = user[j];
										Log.i("设备信息" + j, SubDev);
										list_details.add(SubDev);

									}
									

									if (list_details.size() == 9
											&& list_details.size() > 1) {									
										sheBei.setSubDevid(list_details.get(0));
										sheBei.setMAC(list_details.get(1).toUpperCase());
										sheBei.setPort(list_details.get(2));
										sheBei.setProductNo(list_details.get(3));
										sheBei.setDeviceNo(list_details.get(4));
										sheBei.setDeviceName(list_details.get(5));
										sheBei.setStatus(list_details.get(6));
										sheBei.setRegTime(list_details.get(7));
										sheBei.setJobStatus(list_details.get(8));
										Log.i("list_details.get(0)",
												"" + list_details.get(0));
										Log.i("list_details.get(1).toUpperCase()",
												"" + list_details.get(1).toUpperCase());
										Log.i("list_details.get(2)",
												"" + list_details.get(2));
										Log.i("list_details.get(3)",
												"" + list_details.get(3));
										Log.i("list_details.get(4)",
												"" + list_details.get(4));
										Log.i("list_details.get(5)",
												"" + list_details.get(5));
										Log.i("list_details.get(6)",
												"" + list_details.get(6));
										Log.i("list_details.get(7)",
												"" + list_details.get(7));
										Log.i("list_details.get(8)",
												"" + sheBei.getJobStatus());
										list_shebei.add(sheBei);

									
										mac = list_details.get(1).toUpperCase();
										Log.i("888888888888", mac);										
										Log.i("插入新数据", "插入新数据");
										
										InSertSQl(db, list_details.get(0),
												list_details.get(1).toUpperCase(),
												list_details.get(2),
												list_details.get(3),
												list_details.get(4),
												list_details.get(5),
												list_details.get(6),
												list_details.get(7),
												list_details.get(8));
										Cursor c = db
												.rawQuery(
														"select *from SubDevList where _MAC=?",
														new String[] { list_details.get(1).toUpperCase() });
										mac=list_details.get(1).toUpperCase();
										Log.i("888888888888", "888888888");
								if (c.moveToNext()) {
									Log.i("更新新数据", "更新新数据");

									UpdateSQL1(db,list_details.get(1).toUpperCase(),
											list_details.get(5));
								} else {
									Log.i("插入新数据", "插入新数据");

									InSertSQl1(db, list_details.get(1).toUpperCase(),
											list_details.get(5));
								}

								c.close();

									}
									Log.i("list_details.size()",
											"" + list_details.size());

								}
								Log.i("list_id.size()", "" + list_id.size());
								
								Intent intent = new Intent(User_Login.this,
										Index_Activity.class);
								startActivity(intent);
								dialog.dismiss();
								reStartDialog.dismiss();
								finish();
							}
							
						}

					
						break;

					}

				}

			
		
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);
		clientList = new ArrayList<SanySocketClient>();
		if (Integer.parseInt(VERSION.SDK) > 14
				|| Integer.parseInt(VERSION.SDK) == 14) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // or
																			// .detectAll()
																			// for
																			// all
																			// detectable
																			// problems
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}
		preferences = getSharedPreferences("yckz_user", MODE_PRIVATE);
		editor = preferences.edit();
		
		dialog=new ProgressDialog(User_Login.this);
		reStartDialog=new Dialog(User_Login.this, R.style.Transparent);
		
		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();
		
		db.execSQL("delete from  table_SubDev ");
		db.execSQL("delete from  SubDevList ");
		
		go_tel = (EditText) findViewById(R.id.go_tel);
		go_pass = (EditText) findViewById(R.id.go_pas);

		go = (ImageView) findViewById(R.id.go);
		reg = (ImageView) findViewById(R.id.reg);
		call_KF = (ImageView) findViewById(R.id.callke);

		go.setOnClickListener(listener);
		reg.setOnClickListener(listener);
		call_KF.setOnClickListener(listener);
		
		try {
			//cloudlogin1.zgantech.com/192.168.1.72
			startNewClient("cloudlogin1.zgantech.com", 21000,
					ClientDatahandler, 11, Constant.LOGIN_SERVERPLATFROM,
					Constant.INDEX_cbMessageVer);
			System.out.println("NewSocketInfo" + "21000");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("NewSocketInfo" + "e.printStackTrace() "
					+ e.getMessage());
		}
		
		if (YCKZ_Static.Phone_number!=null && YCKZ_Static.USER_password!=null) {
			go_tel.setText(YCKZ_Static.Phone_number);
			go_pass.setText(YCKZ_Static.USER_password);
		}

		//Detectnetwork();
		/**/
		if(getIntent().getStringExtra("restart")!=null)
		{
			if(getIntent().getStringExtra("restart").equals("restart"))
	        {
	        	reStart();
	        }
		}
        
	}

	public void SendTestInfo() throws Exception {
		// HandleClient();

		String strLoginInfo = go_tel.getText().toString()+"\t"+go_pass.getText().toString();
		
		byte[] logininfo = strLoginInfo.getBytes();

		 System.out.println("NewSocketInfo"+"#############################    "+strLoginInfo);
		boolean bSend = SendData(Constant.LOGIN_SERVERPLATFROM, logininfo,
				strLoginInfo.length(), Constant.LOGIN, Constant.LOGINSUBCMD,
				Constant.VERSION);
		//dialog.dismiss();
		if (bSend) {
			System.out.println("NewSocketInfo  " + strLoginInfo);
			//Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
		}else
		{
			//dialog.dismiss();
			//Toast.makeText(context, "登录失败，请重试！", Toast.LENGTH_SHORT).show();
		}		
	}

	public static boolean SendData(int ServerID, byte[] cbData, int nDataSize,
			byte cbMainCmdID, byte cbSubCmdID, byte cbMessageVer) {
		if (clientList.size() > 0) {
			System.out.println("NewSocketInfo " + clientList.size());
			for (int i = 0; i < clientList.size(); i++) {
				// Log.i("for", "for");
				SanySocketClient con = clientList.get(i);

				if (con.getServerID() == ServerID && con.clientSocket != null
						&& !con.clientSocket.isClosed()) {
					System.out.println("NewSocketInfo"
							+ "  int i = 0; i < clientList.size(); i++   "
							+ con.getServerID());
					// System.out.println("NewSocketInfo"+"(con.getServerID() == ServerID && con.clientSocket != null && !con.clientSocket.isClosed()");

					boolean bSend = con.CrateSendBuffer(cbData, nDataSize,
							cbMainCmdID, cbSubCmdID, cbMessageVer);
					// Log.i("bSend", "bSend");
					if (bSend) {
						System.out.println("NewSocketInfo" + "  请求服务器     "
								+ con.getServerID() + "   的数据   " + cbData);
					}
					return bSend;
				} else if (con.getServerID() == ServerID
						&& (con.clientSocket == null || con.clientSocket
								.isClosed())) {// 断线重连
					// System.out.println("NewSocketInfo"+
					// " int i = 0; i < clientList.size(); i++  con.isClosed   "+con.getServerID());

					System.out
							.println("NewSocketInfo"
									+ " con.getServerID() == ServerID && (con.clientSocket == null || con.clientSocket.isClosed())");

					String strip = con.getServerIp();
					int nPort = con.getServerport();
					Handler lhandler = con.getServerHandler();
					int nServer = con.getServerID();//
					int nplatfrom = con.GetPlatfrom();
					byte cbVersion = con.getVersion();

					HandleClient(i);
					if (nServer > Constant.LOGIN_SERVERPLATFROM) {
						SanySocketClient conn=null;
						try {
						conn = new SanySocketClient(strip,
								nPort, lhandler);
						conn.setClientID(i);
						conn.setPlatfrom(nplatfrom);// 平台代码
						conn.SetServerID(ServerID);
						conn.setVersion(cbVersion);
						clientList.add(conn);						
						success = conn.connect();
						} catch (StackOverflowError e) {
							//success=false;

							Log.e("检查网络StackOverflowError", "检查网络");
							e.printStackTrace();
						}catch(Exception e)
						{
							//success=false;
							//Toast.makeText(context, "网络已断开或网络无法使用，请检查网络！", 1).show();
							Log.e("检查网络Exception", "检查网络");
							e.printStackTrace();
						}

						System.out.println("NewSocketInfo" + "建立连接状态为:"
								+ success);
						// 每次发送之间需要时间间隔 身份验证
						// Thread.sleep(100);
						byte cbMainCmd = 0;
						if (nServer == 1) {
							cbMainCmd = 1;
						} else if (nServer == Constant.MSG_SERVERPLATFROM) {
							cbMainCmd = Constant.MSG_SVRID;
						} else if (nServer == Constant.FILE_SERVERPLATFROM) {
							cbMainCmd = Constant.FILE_SVRID;
						} else if (nServer == 9) {
							cbMainCmd = 0x03;
						}
						String strLoginInfo = YCKZ_Static.Phone_number;
						byte[] info = strLoginInfo.getBytes();
						boolean bSend = SendData(nServer, info,
								strLoginInfo.length(), cbMainCmd,
								Constant.USERID, Constant.VERSION);
						if (bSend) {
							System.out
									.println("NewSocketInfo  "
											+ strLoginInfo
											+ "断线重连     info,strLoginInfo.length(),conn sutdown,Constant.USERID,Constant.VERSION");

							bSend = conn.CrateSendBuffer(cbData, nDataSize,
									cbMainCmdID, cbSubCmdID, cbMessageVer);
							if (bSend)
								System.out.println("NewSocketInfo" + "  请求服务器"
										+ conn.getServerID() + "   的数据   "
										+ cbData);
						}
					}
				}
			}
		}
		return false;
	}

	public static void HandleClient(int i) {
		if (clientList.size() > i) {
			// SanySocketClient con = clientList.get(i);
			clientList.remove(i);
		}
	}

	public static void setHnadler(Handler handler) 
	{		
		if (clientList.size() > 0) {
			for (int i = 0; i < clientList.size(); i++) {
				SanySocketClient con = clientList.get(i);
				con.setHandler(handler);
				System.out.println("NewSocketInfo"
						+ "setHnadler(Handler handler)");
			}
		}else{
			
		}		
	}
	
	/**
	 * 获取保存在SharedPreferences中的clientList
	 * 保存位置在onPause()方法中
	 * @param handler
	 * @param context
	 *//*
	@SuppressWarnings("unchecked")
	public static void setHnadler(Handler handler,Context context) 
	{
		clientList=new ArrayList<SanySocketClient>();
		SharedPreferences  p = context.getSharedPreferences("clientList", MODE_PRIVATE);
		byte[] base64Bytes = Base64.decode(p.getString("clientList", null));  
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			clientList = (List<SanySocketClient>) ois.readObject(); 
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (clientList.size() > 0) {
			for (int i = 0; i < clientList.size(); i++) {
				SanySocketClient con = clientList.get(i);
				con.setHandler(handler);
				System.out.println("NewSocketInfo"
						+ "setHnadler(Handler handler)");
			}
		}else{
			
		}		
	}*/


	public static void startNewClient(String strip, int nport, Handler handler,
			int nPlatfrom, int ServerID, byte cbVersion) throws Exception {
		SanySocketClient con = new SanySocketClient(strip, nport, handler);
		if (clientList.size() > 0)
			con.setClientID(clientList.size());
		else
			con.setClientID(0);
		con.setPlatfrom(nPlatfrom);
		con.SetServerID(ServerID);
		con.setVersion(cbVersion);
		clientList.add(con);
		success = con.connect();
		System.out.println("NewSocketInfo" + "建立连接状态为:" + success);
		// 每次发送之间需要时间间隔
		Thread.sleep(100);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.go:
				if(YCKZ_NetworkDetector.detect((Activity) context))
				{
					dialog.setMessage("正在登陆...请稍后");
					dialog.show();
					if (success) {
						
					} else {
						try {
							// cloudlogin1.zgantech.com/192.168.1.72
							startNewClient("cloudlogin1.zgantech.com", 21000,
									ClientDatahandler, 11,
									Constant.LOGIN_SERVERPLATFROM,
									Constant.INDEX_cbMessageVer);
							System.out.println("NewSocketInfo" + "21000");

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

							System.out.println("NewSocketInfo"
									+ "e.printStackTrace() " + e.getMessage());
						}
					}
					
					if (go_tel.getText() != null && go_pass.getText() != null
							&& !"".equals(go_tel.getText().toString())
							&& !"".equals(go_pass.getText().toString())) {
						Log.i("登陆中", "登陆中");
						try {
							SendTestInfo();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

							System.out.println("NewSocketInfo" + "e.printStackTrace() "
									+ e.getMessage());
						}
					} else {

					}
				}else
				{
					Toast.makeText(context, "请连接网络！", Toast.LENGTH_LONG).show();
				}
				
				break;

			case R.id.reg:
				Intent intent = new Intent(User_Login.this, User_Reg.class);
				startActivity(intent);
				break;
			case R.id.callke:
				Intent intent2 = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:" + "10086"));
				startActivity(intent2);
				break;
			}
		}
	};
	public static void GetChildList() {
		// TODO Auto-generated method stub

		try {

			User_Login.SendData(9, null, 0, Constant.List_cbMainCmdID,
					Constant.List_cbSubCmdID, Constant.INDEX_cbMessageVer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("NewSocketInfo" + "e.printStackTrace() "
					+ e.getMessage());
		}
	}


	public static String int2ip(long ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}
	/**
	 * 更新数据
	 * @param db
	 * @param _SubDev
	 * @param _MAC
	 * @param _Port
	 * @param _ProductNo
	 * @param _DeviceNo
	 * @param _DeviceName
	 * @param _Status
	 * @param _RegTime
	 * @param _JobStatus
	 */
	public void UpdateSQL(SQLiteDatabase db, String _SubDev, String _MAC,
			String _Port, String _ProductNo, String _DeviceNo,
			String _DeviceName, String _Status, String _RegTime,
			String _JobStatus) {
		Log.i("数据库操作", "正在插入数据");
		
		ContentValues cv = new ContentValues();
		cv.put("_SubDvId", _SubDev);
		cv.put("_Port", _Port);
		cv.put("_ProductNo", _ProductNo);
		cv.put("_DeviceNo", _DeviceNo);
		cv.put("_DeviceName", _DeviceName);
		cv.put("_Status", _Status);
		cv.put("_RegTime", _RegTime);
		cv.put("_JobStatus", _JobStatus);
		String[] args = { String.valueOf(_MAC) };
		db.update("table_SubDev", cv, "_MAC=?", args);
		Log.i("数据库操作", "更新数据完成");

		// TODO Auto-generated method stub

	}
	public void UpdateSQL1(SQLiteDatabase db,  String _MAC,
			String _DeviceName) {
		Log.i("数据库操作", "正在插入数据");
		
		ContentValues cv = new ContentValues();	
		cv.put("_DeviceName", _DeviceName);
		
		String[] args = { String.valueOf(_MAC) };
		db.update("SubDevList", cv, "_MAC=?", args);
		Log.i("数据库操作", "更新数据完成");

		// TODO Auto-generated method stub

	}
	/**
	 * 插入数据
	 * @param db
	 * @param _SubDev
	 * @param _MAC
	 * @param _Port
	 * @param _ProductNo
	 * @param _DeviceNo
	 * @param _DeviceName
	 * @param _Status
	 * @param _RegTime
	 * @param _JobStatus
	 */
	public  void InSertSQl(SQLiteDatabase db, String _SubDev, String _MAC,
			String _Port, String _ProductNo, String _DeviceNo,
			String _DeviceName, String _Status, String _RegTime,
			String _JobStatus) {
		// TODO Auto-generated method stub
		Log.i("数据库操作", "正在插入数据");
		
		db.execSQL("insert into table_SubDev values(null,?,?,?,?,?,?,?,?,?)",
				new String[] { _SubDev, _MAC, _Port, _ProductNo, _DeviceNo,
						_DeviceName, _Status, _RegTime, _JobStatus });
		Log.i("数据库操作", "插入数据完成");

	}
	public void InSertSQl1(SQLiteDatabase db,  String _MAC,
			String _DeviceName) {
		// TODO Auto-generated method stub
		Log.i("数据库操作", "正在插入数据");
		
		db.execSQL("insert into SubDevList values(null,?,?)",
				new String[] {  _MAC,  _DeviceName, });
		Log.i("数据库操作", "插入数据完成");

	}
	
	public static void Login(){
		try {
			byte[] logininfo;

			String strLoginInfo = YCKZ_Static.Phone_number;
			logininfo = strLoginInfo.getBytes();
			User_Login.SendData(9, logininfo, strLoginInfo.length(),
					Constant.INDEX_cbMainCmdID, /* (byte)0x21 */
					Constant.INDEX_cbSubCmdID, Constant.INDEX_cbMessageVer);

			Thread.sleep(100);
			
			logininfo = strLoginInfo.getBytes();
			User_Login.SendData(7, logininfo, strLoginInfo.length(),
					Constant.INDEX_bjMainCmdID, /* (byte)0x21 */
					Constant.INDEX_bjSubCmdID, Constant.INDEX_cbMessageVer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("NewSocketInfo" + "e.printStackTrace() "
					+ e.getMessage());
		}
	}
	

	  protected void Detectnetwork() { // TODO Auto-generated method stub
	  Boolean networkState = YCKZ_NetworkDetector.detect(User_Login.this);
	 
	 if (!networkState) { new AlertDialog.Builder(this) .setTitle("网络错误")
	  .setMessage("网络连接失败，请确认网络连接") .setPositiveButton( "确定", new
	  android.content.DialogInterface.OnClickListener() {
	  
	  @Override public void onClick(DialogInterface arg0, int arg1) {
		  // TODO Auto-generated method stub
	 arg0.dismiss(); } }).show(); } }
	  @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		  if(db!=null){
			  db.close();
		  }
		  if (yckz_SQLHelper!=null) {
			  yckz_SQLHelper.close();
		}
		super.onDestroy();
	}
	  
	  //重新登录
	  public void reStart()
	  {
		  repreferences = context.getSharedPreferences("yckz_user", MODE_PRIVATE);

		  YCKZ_Static.Phone_number= repreferences.getString("user_name", null);
		  YCKZ_Static.USER_password= repreferences.getString("user_pas", null);
		  
		  if (YCKZ_Static.Phone_number!=null && YCKZ_Static.USER_password!=null) {
				go_tel.setText(YCKZ_Static.Phone_number);
				go_pass.setText(YCKZ_Static.USER_password);
			}
		  
		  if(YCKZ_NetworkDetector.detect((Activity) context))
			{				
				reStartDialog.show();
				if (success) {
					
				} else {
					try {
						// cloudlogin1.zgantech.com/192.168.1.72
						startNewClient("cloudlogin1.zgantech.com", 21000,
								ClientDatahandler, 11,
								Constant.LOGIN_SERVERPLATFROM,
								Constant.INDEX_cbMessageVer);
						System.out.println("NewSocketInfo" + "21000");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("NewSocketInfo"
								+ "e.printStackTrace() " + e.getMessage());
					}
				}
				
				if (go_tel.getText() != null && go_pass.getText() != null
						&& !"".equals(go_tel.getText().toString())
						&& !"".equals(go_pass.getText().toString())) {
					Log.i("登陆中", "登陆中");
					try {
						SendTestInfo();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						System.out.println("NewSocketInfo" + "e.printStackTrace() "
								+ e.getMessage());
					}
				} else {

				}
			}else
			{
				Toast.makeText(context, "请连接网络！", Toast.LENGTH_LONG).show();
			}
	  }

	@Override
	protected void onPause() {
		/*SharedPreferences  p = getSharedPreferences("clientList", MODE_PRIVATE);
		Editor e = p.edit();
		
		ByteArrayOutputStream toByte = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(toByte);
			oos.writeObject(clientList);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//对byte[]进行Base64编码
		String clientList = new String(Base64.encode(toByte.toByteArray()));
		// 存储
		e.putString("clientList", clientList);
		e.commit();*/
		
		super.onPause();
	}
}
