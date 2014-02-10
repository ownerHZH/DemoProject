package com.hiibox.houseshelter.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.ImprintingAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.listener.HandlerCommandListener;
import com.hiibox.houseshelter.net.AlarmRecordsResult;
import com.hiibox.houseshelter.net.DefenceRecordsResult;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.MembersInfoResult;
import com.hiibox.houseshelter.net.OutRecordsResult;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;
import com.hiibox.houseshelter.view.PullToRefreshView;
import com.hiibox.houseshelter.view.PullToRefreshView.OnFooterRefreshListener;
import com.hiibox.houseshelter.view.PullToRefreshView.OnHeaderRefreshListener;

    
  
  
  
  
  
  
  
public class ImprintingActivity extends ShaerlocActivity implements OnHeaderRefreshListener, OnFooterRefreshListener, OnDateSetListener {

    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.pull_to_refresh_view) PullToRefreshView refreshView;
    @ViewInject(id = R.id.records_lv, itemClick = "onItemClick") ListView recordsLV;
    @ViewInject(id = R.id.access_records_tv, click = "onClick") TextView accessTV;
    @ViewInject(id = R.id.defence_records_tv, click = "onClick") TextView defenceTV;
    @ViewInject(id = R.id.warning_records_tv, click = "onClick") TextView warningTV;
    @ViewInject(id = R.id.date_tv, click = "onClick") TextView dateTV;
    @ViewInject(id = R.id.type_tv, click = "onClick") TextView typeTV;
    @ViewInject(id = R.id.root_layout) RelativeLayout rootLayout;
    @ViewInject(id = R.id.progress_bar) ProgressBar bar;
    
    private ImprintingAdapter adapter = null;
    private List<Map<String, String>> accessList = null;
    private List<Map<String, String>> defenceList = null;
    private List<Map<String, String>> warningList = null;
    private int index = 0;                           
    private HandlerCommandListener commandListener = null;
    private DatePickerDialog dateDialog = null;	
    private String queryType1, queryType2, queryType3, queryTime1, queryTime2, queryTime3;
    private int queryIndex = 0;
    private String currDate = null;
    private AlarmRecordsResult alarmRecordsResult = null;
    private OutRecordsResult outRecordsResult = null;
    private DefenceRecordsResult defenceRecordsResult = null;
    
    @SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg.obj) {
            	return;
            }
            Frame[] frame = (Frame[]) msg.obj;
            if (null == frame[0]) {
            	return;
            }
            int subCmd = frame[0].subCmd;
            if (msg.what == 0) {
            	if (null == frame[1]) {
            		return;
            	}
            	Log.d("ImprintingActivity", "handleMessage()  subCmd = "+subCmd+" ; data = "+frame[1].strData);
            	if (frame[1].strData.equals("1")) {
                                                                         
                    bar.setVisibility(View.GONE);
                    return;
                }
            	if (subCmd == 68) {           
            		int rc = outRecordsResult.getResult(frame[1]);
            		if (rc == 0) {
            			accessList = outRecordsResult.getList();
            			if (null != accessList && accessList.size() > 0) {
                        	adapter.setList(accessList, 0);
                        } else {
                        	MessageUtil.alertMessage(mContext, R.string.no_data);
                        }
            			bar.setVisibility(View.GONE);
            		}
            	} else if (subCmd == 69) {           
            		int rc = defenceRecordsResult.getResult(frame[1]);
            		if (rc == 0) {
            			defenceList = defenceRecordsResult.getList();
            			if (null != defenceList && defenceList.size() > 0) {
            				adapter.setList(defenceList, 1);
                        } else {
                        	MessageUtil.alertMessage(mContext, R.string.no_data);
                        }
            			bar.setVisibility(View.GONE);
            		}
            	} else if (subCmd == 70) {           
            		int rc = alarmRecordsResult.getAlarmRecords(frame[1]);
            		Log.e("ImprintingActivity", "handleMessage() [������¼��ѯ] rc = "+rc);
            		if (rc == 0) {
            			warningList = alarmRecordsResult.getAlarmList();
            			Log.e("ImprintingActivity", "handleMessage() [������¼��ѯ] len = "+warningList.size());
            			if (null != warningList && warningList.size() > 0) {
            				adapter.setList(warningList, 2);
            			} else {
            				MessageUtil.alertMessage(mContext, R.string.no_data);
            			}
            			bar.setVisibility(View.GONE);
            		}
            	}
            } else {       
            	MessageUtil.alertMessage(mContext, R.string.network_timeout);
            	switch (subCmd) {
					case 68:
						Log.d("ImprintingActivity", "queryOutRecords() ��ʱ");
						break;
					case 69:
						Log.d("ImprintingActivity", "queryAllDefineRecords() ��ʱ");
						break;
					case 70:
						Log.d("ImprintingActivity", "queryWarningRecords() ��ʱ");
						break;
					default:
						break;
				}
            	bar.setVisibility(View.GONE);
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprinting_layout);
        commandListener = new HandlerCommandListener(handler);
        queryIndex = getIntent().getIntExtra("queryIndex", 0);
        
        refreshView.setHeadRefresh(false);
        refreshView.setFooterRefresh(false);
        
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        currDate = getFormatDate(year, month, day);
        dateTV.setText(currDate);
        dateDialog = new DatePickerDialog(this, this, year, month, day);
        
        adapter = new ImprintingAdapter(mContext, finalBitmap);
        recordsLV.setAdapter(adapter);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        
        alarmRecordsResult = new AlarmRecordsResult();
        outRecordsResult = new OutRecordsResult();
        defenceRecordsResult = new DefenceRecordsResult();
        
        if (queryIndex == 1) {         
            index = 2;
        	sendRequest(0x10, null, null);
        	warningTV.setBackgroundResource(R.drawable.bg_warning_selected);
            accessTV.setBackgroundDrawable(null);
            defenceTV.setBackgroundDrawable(null);
        } else if (queryIndex == 2) {         
            index = 2;
        	sendRequest(0x11, null, null);
        	warningTV.setBackgroundResource(R.drawable.bg_warning_selected);
            accessTV.setBackgroundDrawable(null);
            defenceTV.setBackgroundDrawable(null);
        } else {
        	sendRequest(0x01, null, null);
        }
        
    }
    
    private String getFormatDate(int year, int month, int day) {
    	StringBuilder sb = new StringBuilder();
		sb.append(year).append("/");
		if (month < 9) {
			sb.append("0");
		}
		sb.append((month+1)).append("/");
		if (day < 10) {
			sb.append("0");
		}
		sb.append(day);
    	return sb.toString();
	}

	    
  
  
  
  
  
    private void sendRequest(int queryCode, String rfidCard, String date) {
    	if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        	return;
        }
		String phone = PreferenceUtil.getInstance(getApplicationContext()).getString("phone", null);
		if (StringUtil.isEmpty(phone)) {
			MessageUtil.alertMessage(mContext, R.string.please_login);
			if (null != MyApplication.mainClient) {
            	MyApplication.mainClient.stop();
            	MyApplication.mainClient = null;
            }
            if (null != MyApplication.fileClient) {
            	MyApplication.fileClient.stop();
                MyApplication.fileClient = null;
            }
            stopService(new Intent(mContext, PushMessageService.class));
			startActivity(new Intent(mContext, LoginActivity.class));
			MianActivity.getScreenManager().exitAllActivityExceptOne();
			return;
		}
		if (null == MyApplication.mainClient || !MyApplication.mainClient.isConnected()) {
    		MyApplication.initTcpManager();
            MyApplication.mainClient = MyApplication.tcpManager.getMainClient(phone, null, "1111111111111111", "66666666");
    	}
		Log.d("ImprintingActivity", "��¼��ѯ����   queryCode = "+queryCode+" ; rfidCard = "+rfidCard+" ; date = "+date+" ; mainClient = "+MyApplication.mainClient);
		if (null == MyApplication.mainClient) {
		    return;
		}
		switch (queryCode) {
			case 0x01:             
				outRecordsResult.clearList();
				MyApplication.mainClient.queryOutRecords(phone, commandListener);
				break;
			case 0x02:               
				outRecordsResult.clearList();
				MyApplication.mainClient.queryOutRecords(phone, rfidCard, commandListener);
				break;
			case 0x03:                 
				outRecordsResult.clearList();
				MyApplication.mainClient.queryOutRecordsTime(phone, date, commandListener);
				break;
			case 0x04:                     
				outRecordsResult.clearList();
				MyApplication.mainClient.queryOutRecords(phone, rfidCard, date, commandListener);
				break;
			case 0x05:               
				defenceRecordsResult.clearList();
				MyApplication.mainClient.queryDefineRecords(phone, commandListener);
				break;
			case 0x06:                 
				defenceRecordsResult.clearList();
				MyApplication.mainClient.queryDefineRecords(phone, date, commandListener);
				break;
			case 0x07:             
				defenceRecordsResult.clearList();
				MyApplication.mainClient.queryDefineRecords20(phone, commandListener);
				break;
			case 0x08:             
				defenceRecordsResult.clearList();
				MyApplication.mainClient.queryDefineRecords31(phone, commandListener);
				break;
			case 0x09:             
				alarmRecordsResult.clearList();
				MyApplication.mainClient.queryWarnRecord(phone, commandListener);
				break;
			case 0x10:                         
				alarmRecordsResult.clearList();
				MyApplication.mainClient.queryTemperatureWarnRecord(phone, commandListener);
				break;
			case 0x11:                         
				alarmRecordsResult.clearList();
				MyApplication.mainClient.queryInvadeWarnRecord(MyApplication.phone, commandListener);
				break;
			case 0x12:                
				alarmRecordsResult.clearList();
				MyApplication.mainClient.queryWarnRecord(MyApplication.phone, date, commandListener);
				break;
			case 0x13:             
				defenceRecordsResult.clearList();
			    MyApplication.mainClient.queryDefineRecords34(phone, date, commandListener);
			    break;
			case 0x14:             
				defenceRecordsResult.clearList();
			    MyApplication.mainClient.queryDefineRecords35(phone, date, commandListener);
			    break;
			default:
				break;
		}
		bar.setVisibility(View.VISIBLE);
    }
    
    
    public void onClick(View v) {
        int vId = v.getId();
        Intent intent = new Intent();
        switch (vId) {
            case R.id.back_iv:
            	startActivity(new Intent(mContext, HomepageActivity.class));
                MianActivity.getScreenManager().exitActivity(mActivity);
                return;
            case R.id.access_records_tv:
                index = 0;
                if (StringUtil.isEmpty(queryType1)) {
                	typeTV.setText(getString(R.string.all));
                } else {
                	typeTV.setText(queryType1);
                }
                if (StringUtil.isEmpty(queryTime1)) {
                    dateTV.setText(currDate);
                } else {
                    dateTV.setText(queryTime1);
                }
                accessTV.setBackgroundResource(R.drawable.bg_access_selected);
                defenceTV.setBackgroundDrawable(null);
                warningTV.setBackgroundDrawable(null);
                if (null == accessList) {
                    bar.setVisibility(View.VISIBLE);
            		sendRequest(0x01, null, null);
            	} else {
            		adapter.setList(accessList, 0);
            	}
                return;
            case R.id.defence_records_tv:
                index = 1;
                if (StringUtil.isEmpty(queryType2)) {
                	typeTV.setText(getString(R.string.all));
                } else {
                	typeTV.setText(queryType2);
                }
                if (StringUtil.isEmpty(queryTime2)) {
                    dateTV.setText(currDate);
                } else {
                    dateTV.setText(queryTime2);
                }
                defenceTV.setBackgroundResource(R.drawable.bg_defence_selected);
                accessTV.setBackgroundDrawable(null);
                warningTV.setBackgroundDrawable(null);
                if (null == defenceList) {
                    bar.setVisibility(View.VISIBLE);
                	sendRequest(0x05, null, null);
            	} else {
            		adapter.setList(defenceList, 1);
            	}
                return;
            case R.id.warning_records_tv:
                index = 2;
                if (StringUtil.isEmpty(queryType3)) {
                	typeTV.setText(getString(R.string.all));
                } else {
                	typeTV.setText(queryType3);
                }
                if (StringUtil.isEmpty(queryTime3)) {
                    dateTV.setText(currDate);
                } else {
                    dateTV.setText(queryTime3);
                }
                warningTV.setBackgroundResource(R.drawable.bg_warning_selected);
                accessTV.setBackgroundDrawable(null);
                defenceTV.setBackgroundDrawable(null);
                if (null == warningList) {
                    bar.setVisibility(View.VISIBLE);
                	sendRequest(0x09, null, null);
            	} else {
            		adapter.setList(warningList, 2);
            	}
                return;
            case R.id.date_tv:
                                                                             
                                 
                                                                                                 
                        
                 
                dateDialog.show();
                         
                return;
            case R.id.type_tv:
                Intent typeIntent = new Intent(mActivity, FamilyMembersActivity.class);
                typeIntent.putExtra("selectedIndex", index);
                startActivityForResult(typeIntent, 0x201);
                return;
            default:
                break;
        }
        Log.i("ImprintingActivity", "onClick()  intent.getClass() = "+intent.getClass());
        if (intent.getClass() != null) {
            startActivity(intent);
            MianActivity.getScreenManager().exitActivity(mActivity);
        }
    }
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        if (index == 2) {
            Intent intent = new Intent();
            Map<String, String> map = warningList.get(position);
            String filedId = map.get("filedId");
                             
            if (map.get("warningType").equals("16")) {         
                intent.setClass(this, TemperatureAbnormalActivity.class);
                intent.putExtra("time", map.get("time"));
            } else {       
                intent.setClass(this, InvadeActivity.class);
            }
            intent.putExtra("filedId", filedId);
            startActivity(intent);
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x201 && resultCode == RESULT_OK) {
            String member = data.getStringExtra("member");
            Log.i("ImprintingActivity", "onActivityResult()  member = "+member);
            if (StringUtil.isNotEmpty(member)) {
                typeTV.setText(member);
                if (index == 0) {
                	queryType1 = member;
                	queryAccessRecords(member, dateTV.getText().toString());
                } else if (index == 1) {
                	queryType2 = member;
                	queryDefenceRecords(member, dateTV.getText().toString());
                                                                            
                                                   
                                                                            
                                                   
                                                                        
                                                   
                     
                } else if (index == 2) {
                	queryType3 = member;
                	if (member.equals(getString(R.string.temperature_abnormal))) {
                		sendRequest(0x10, null, null);
                	} else if (member.equals(getString(R.string.invade))) {
                		sendRequest(0x11, null, null);
                	} else if (member.equals(getString(R.string.all))) {
                		sendRequest(0x09, null, null);
                	}
                }
            }
        }
    }
    
        
  
  
  
  
    private void queryAccessRecords(String member, String time) {
    	Log.i("ImprintingActivity", "�����¼��ѯ�������ѯ�� queryAccessRecords()   member = "+member+" ; time = "+time);
    	String tp = getString(R.string.all);
    	String dt = currDate;
    	String rfidCard = null;
    	List<MembersInfoResult> membersList = HomepageActivity.membersList;
    	if (null !=  membersList) {
    		int len = membersList.size();
    		for (int i = 0; i < len; i ++) {
    			if (membersList.get(i).nickname.trim().equals(member)) {
    				rfidCard = membersList.get(i).cardNum;
    				break;
    			}
    		}
    	}
    	if (tp.equals(member) && dt.equals(time)) {
    		sendRequest(0x01, null, null);
    	} else if (tp.equals(member) && !dt.equals(time)) {
    		sendRequest(0x03, null, time);
    	} else if (!tp.equals(member) && dt.equals(time)) {
    	    Log.i("ImprintingActivity", "�����¼��ѯ�������ѯ�� queryAccessRecords()   rfidCard = "+rfidCard);
    		if (null != rfidCard) {
    			sendRequest(0x02, rfidCard, null);
    		}
    	} else if (!tp.equals(member) && !dt.equals(time)) {
    		sendRequest(0x04, rfidCard, time);
    	}
    }
    
        
  
  
  
  
    private void queryDefenceRecords(String defenceType, String time) {
        Log.i("ImprintingActivity", "������¼��ѯ�������ѯ�� queryDefenceRecords()   DefencetType = "+defenceType+" ; time = "+time);
        String tp = getString(R.string.all);
        String dt = currDate;
        if (tp.equals(defenceType) && dt.equals(time)) {         
            sendRequest(0x05, null, null);
        } else if (tp.equals(defenceType) && !dt.equals(time)) {           
            sendRequest(0x06, null, time);
        } else if (!tp.equals(defenceType) && dt.equals(time)) {           
            if (defenceType.equals(getString(R.string.defence))) {
                sendRequest(0x07, null, null);
            } else if (defenceType.equals(getString(R.string.cancel_defence))) {
                sendRequest(0x08, null, null);
            }
        } else if (!tp.equals(defenceType) && !dt.equals(time)) {                
            if (defenceType.equals(getString(R.string.defence))) {
                sendRequest(0x13, null, time);
            } else if (defenceType.equals(getString(R.string.cancel_defence))) {
                sendRequest(0x14, null, time);
            }
        }
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
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
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(mContext, HomepageActivity.class));
			MianActivity.getScreenManager().exitActivity(mActivity);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		String date = getFormatDate(year, monthOfYear, dayOfMonth);
		dateTV.setText(date);
		if (index == 0) {
			queryTime1 = date;
			queryAccessRecords(typeTV.getText().toString(), date);
		} else if (index == 1) {
			queryTime2 = date;
			queryDefenceRecords(typeTV.getText().toString(), date);
		} else if (index == 2) {
			queryTime3 = date;
			sendRequest(0x12, null, queryTime3);
		}
	}
    
}
