package com.hiibox.houseshelter.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.MembersInfoResult;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.util.MessageUtil;

    
  
  
  
  
  
  
  
public class RFIDCardAdapter extends BaseAdapter {

    private Context context = null;
    @SuppressWarnings("unused")
    private FinalBitmap finalBitmap = null;
    private List<MembersInfoResult> list = null;
    private String deleteMemberStr = null;
    private ProgressDialog loginDialog = null;
    
    public RFIDCardAdapter(Context context, FinalBitmap finalBitmap) {
        super();
        this.context = context;
        this.finalBitmap = finalBitmap;
        deleteMemberStr = context.getResources().getString(R.string.delete_member);
        loginDialog = new ProgressDialog(context);
        loginDialog.setCancelable(true);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.setMessage(context.getResources().getString(R.string.deleting_member));
    }

    public void setList(List<MembersInfoResult> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	MembersInfoResult map = (MembersInfoResult) getItem(position);
        ViewHolder holder = new ViewHolder();
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.lv_item_rfid_card_layout, null);
        }
        holder.nameTV = (TextView) convertView.findViewById(R.id.member_name_tv);
        holder.codeTV = (TextView) convertView.findViewById(R.id.rfid_card_code_tv);
        holder.portraitIV = (ImageView) convertView.findViewById(R.id.head_portrait_iv);
        holder.deleteIV = (ImageView) convertView.findViewById(R.id.delete_iv);
        
        final String name = map.nickname;
        final String cardNum = map.cardNum;
        holder.nameTV.setText(name);
        holder.codeTV.setText(cardNum);
                                                                          
        
        holder.deleteIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(context)
                .setMessage(deleteMemberStr + name)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sendOrder(cardNum, position);
                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
                dialog.show();
            }
        });
        
        return convertView;
    }
    
        
  
  
    private void sendOrder(final String cardNum, final int position) {
    	loginDialog.show();
    	boolean isConnect = MyApplication.mainClient.isConnected();
    	Log.d("RFIDCardAdapter", "sendOrder()  connected = "+isConnect);
    	if (MyApplication.mainClient.isConnected()) {         
    		deleteMember(cardNum, position, handler);
    	} else {          
    		MyApplication.initTcpManager();
    		MyApplication.mainClient = MyApplication.tcpManager.getMainClient(MyApplication.phone, MyApplication.password, "1111111111111111", "66666666");
    		MyApplication.mainClient.userAuth(MyApplication.phone, new CommandListener() {
    			@Override
    			public void onTimeout(Frame src, Frame f) {
    				Log.e("RFIDCardAdapter", "sendOrder()  userAuth()  ��ͥ��ʿ���������ӳ�ʱ������");
                            
                                                                       
                         
    				handler.sendEmptyMessage(-1);
    			}
    			
    			@Override
    			public int onReceive(Frame src, Frame f) {
    				if (null != f && SpliteUtil.getRuquestStatus(f.strData)) {
    					Log.e("RFIDCardAdapter", "sendOrder()  userAuth()  ��ͥ��ʿ���������ӳɹ�������");
    					deleteMember(cardNum, position, handler);
    				} else {
    					handler.sendEmptyMessage(2);
    					Log.e("RFIDCardAdapter", "sendOrder()  userAuth()  ��ͥ��ʿ����������ʧ�ܡ�����");
    				}
    				return 0;
    			}
    		});
    	}
    }

    
        
  
  
  
  
    private void deleteMember(String cardNum, final int position, final Handler handler) {
    	MyApplication.mainClient.unregCard(MyApplication.phone, cardNum, new CommandListener() {
			@Override
			public void onTimeout(Frame src, Frame f) {
				Log.e("AddRFIDCardAdapter", "[ɾ��ü�ͥ��Ա]    time out...");
				loginDialog.dismiss();
                        
                                                                   
                     
				handler.sendEmptyMessage(-1);
			}
			
			@Override
			public int onReceive(Frame src, Frame f) {
				Log.e("AddRFIDCardAdapter", "[ɾ��ü�ͥ��Ա]    data = "+f.strData+" ; subCmd = "+f.subCmd);
				loginDialog.dismiss();
                        
				if (f.strData.equals("0")) {
					list.remove(position);
					handler.sendEmptyMessage(0);
                                                                   
                                                                                                      
				} else {
                                                                                                     
					handler.sendEmptyMessage(1);
				}
                     
				Log.e("AddRFIDCardAdapter", "[ɾ��ü�ͥ��Ա]   onReceive()  before return data......");
				return 0;
			}
		});
    }
    
    class ViewHolder {
        TextView nameTV;
        TextView codeTV;
        ImageView portraitIV;
        ImageView deleteIV;
    }
    
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if (what == 0) {
				MessageUtil.alertMessage(context, context.getResources().getString(R.string.operate_success));
				notifyDataSetChanged();
			} else if (what == 1) {
				MessageUtil.alertMessage(context, context.getResources().getString(R.string.operate_failed));
			} else if (what == 2) {
				MessageUtil.alertMessage(context, context.getResources().getString(R.string.network_not_response));
			} else if (what == -1) {
				MessageUtil.alertMessage(context, R.string.network_timeout);
			}
		}
    };

}
