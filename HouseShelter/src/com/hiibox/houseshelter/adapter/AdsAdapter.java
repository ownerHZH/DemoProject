package com.hiibox.houseshelter.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.hiibox.houseshelter.R;

    
  
  
  
  
  
  
  
public class AdsAdapter extends PagerAdapter {

	private Context context;
	private int[] resid = new int[] {R.drawable.ads_1, R.drawable.ads_2};

	public AdsAdapter(Context cxt) {
		super();
		this.context = cxt;
	}

	public void setList(List<String> list) {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return resid.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	                                           
	                                                                   
	                                                   
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.activity_view_pager_details, null);
		ImageView iv = (ImageView) view.findViewById(R.id.activity_poster_iv);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setBackgroundResource(resid[position]);
		view.setTag(position);
		container.addView(view);
		return view;
	}

	                                         
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(container.findViewWithTag(position));
	}

}
