package com.hiibox.houseshelter.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

    
  
  
  
  
  
  
  
public class AdvertisementAdapter extends PagerAdapter {

    private Context mContext = null;
    private List<String> mList = null;
    private FinalBitmap mFinalBitmap = null;
    
    public AdvertisementAdapter(Context mContext, List<String> mList, FinalBitmap mFinalBitmap) {
        super();
        this.mContext = mContext;
        this.mList = mList;
        this.mFinalBitmap = mFinalBitmap;
    }
    
    public void setList(List<String> mList) {
    	this.mList = mList;
    	notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
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
        ImageView iv = new ImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(params);
        iv.setScaleType(ScaleType.FIT_XY);
        iv.setTag(position);
                                                                                          
        mFinalBitmap.display(iv, mList.get(position));
        container.addView(iv, 0);
        return iv;
    }
    
    @Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
