package com.hiibox.houseshelter.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.activity.VoiceDialogActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.net.MembersInfoResult;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;

public class MembersPagerAdapter extends PagerAdapter {

	private Context context = null;
	private Activity activity = null;
	private List<List<MembersInfoResult>> list = null;
	private Drawable maleDrawable = null;          
	private Drawable maleLightdDrawable = null;          
	private Drawable recorderDrawable = null;           
	private Drawable recorderLightDrawable = null;           

	public MembersPagerAdapter(Context context, Activity activity    ) {
		super();
		this.context = context;
		this.activity = activity;
                     
		Resources res = context.getResources();
		maleDrawable = res.getDrawable(R.drawable.male_gray);
		maleDrawable.setBounds(0, 0, maleDrawable.getMinimumWidth(),
				maleDrawable.getMinimumHeight());
		maleLightdDrawable = res.getDrawable(R.drawable.male_light);
		maleLightdDrawable.setBounds(0, 0,
				maleLightdDrawable.getMinimumWidth(),
				maleLightdDrawable.getMinimumHeight());
		recorderDrawable = res.getDrawable(R.drawable.recorder_gray);
		recorderDrawable.setBounds(0, 0, recorderDrawable.getMinimumWidth(),
				recorderDrawable.getMinimumHeight());
		recorderLightDrawable = res.getDrawable(R.drawable.recorder_light);
		recorderLightDrawable.setBounds(0, 0,
				recorderLightDrawable.getMinimumWidth(),
				recorderLightDrawable.getMinimumHeight());
	}

	public void setList(List<List<MembersInfoResult>> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(container.findViewWithTag(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		List<MembersInfoResult> sList = list.get(position);
		ViewHolder holder = new ViewHolder();
		View view = (ViewGroup) View.inflate(context, R.layout.pager_item_family_members_layout, null);
		holder.tv1 = (TextView) view.findViewById(R.id.member1_tv);
		holder.tv2 = (TextView) view.findViewById(R.id.member2_tv);
		holder.tv3 = (TextView) view.findViewById(R.id.member3_tv);
		holder.tv4 = (TextView) view.findViewById(R.id.member4_tv);
		holder.tv5 = (TextView) view.findViewById(R.id.member5_tv);

		TextView[] memberView = new TextView[] { holder.tv1, holder.tv2,
				holder.tv3, holder.tv4, holder.tv5 };
		int len = sList.size();
		len = (len > 5) ? 5 : len;
                                                                   
		for (int i = 0; i < len; i++) {
		    memberView[i].setText(sList.get(i).nickname.trim());
                                    
                        
                                                            
                                                                                              
                                                                           
                
                                                                                        
                                                                       
         
		    Drawable d = (sList.get(i).status == 0) ? maleDrawable : maleLightdDrawable;
            memberView[i].setCompoundDrawables(null, d, null, null);
                                    
                                                              
      
			Log.d("MembersPagerAdapter", "instantiateItem()  nickname = "+sList.get(i).nickname.trim()+" ; status = "+sList.get(i).status);
		}
		for (int j = 0; j < 5 - len; j++) {
			memberView[4 - j].setVisibility(View.GONE);
		}
		view.setTag(position);
		container.addView(view, 0);
		return view;
	}

	class ViewHolder {
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
	}
	
	class OnClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (LocationUtil.checkNetWork(context).endsWith(GlobalUtil.NETWORK_NONE)) {
            	MessageUtil.alertMessage(context, R.string.sys_network_error);
            	context.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
            	return;
            }
                                                                                                
			activity.startActivityForResult(new Intent(activity, VoiceDialogActivity.class), 0x901);
		}
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
}
