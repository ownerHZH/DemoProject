package com.hiibox.houseshelter.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.activity.CloudPhotoAlbumDetailsActivity;
import com.hiibox.houseshelter.net.CloudPhotoAlbumResult.PhotoInfo;
import com.hiibox.houseshelter.util.ScreenUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class CloudPhotoAlbumAdapter extends BaseAdapter {

    private Context context = null;
    private Activity activity = null;
    private List<Map<String, String>> infoList = null;
    private List<List<String>> picList = null;
    private List<List<String>> picIdList = null;
    private FinalBitmap finalBitmap = null;
    private String unit = null;
    private TreeMap<String, List<PhotoInfo>> mMap = null;
    
    public CloudPhotoAlbumAdapter(Context context, Activity activity, FinalBitmap finalBitmap) {
        super();
        this.context = context;
        this.activity = activity;
        this.finalBitmap = finalBitmap;
        unit = context.getResources().getString(R.string.unit_zhang);
        infoList = new ArrayList<Map<String,String>>();
        picList = new ArrayList<List<String>>(); 
        picIdList = new ArrayList<List<String>>(); 
    }
    
                            
                           
                      
         
                               
                          
         
                                
                           
         
                              
                         
         
        
    
    public void setMap(TreeMap<String, List<PhotoInfo>> map) {
    	this.mMap = map;
    	infoList.clear();
    	picList.clear();
    	picIdList.clear();
    	Set<Map.Entry<String, List<PhotoInfo>>> set	 = mMap.entrySet();
    	for (Iterator<Map.Entry<String, List<PhotoInfo>>> it = set.iterator(); it.hasNext();) {
    		Map.Entry<String, List<PhotoInfo>> entry = it.next();
    		List<PhotoInfo> l = entry.getValue();
    		List<String> itemList = new ArrayList<String>();
    		List<String> idList = new ArrayList<String>();
    		boolean isIntrude = false;          
    		boolean isCapture = false;          
    		for (int i = 0; i < l.size(); i ++) {
    			itemList.add(l.get(i).getUrl());
    			idList.add(l.get(i).getFileId());
    			if (l.get(i).getType() == 3) {       
    				isIntrude = true;
    			} else if (l.get(i).getType() == 15) {       
    			    isCapture = true;
    			}
    		}
    		picList.add(0, itemList);
    		picIdList.add(0, idList);
    		
    		Map<String, String> m = new HashMap<String, String>();
    		m.put("picDate", entry.getKey());
    		if (isIntrude) {
    			m.put("isIntrude", "1");
    		} else {
    		    m.put("isIntrude", "0");
    		}
    		if (isCapture) {
    		    m.put("isCapture", "1");
    		} else {
    		    m.put("isCapture", "0");
    		}
    		infoList.add(0, m);
    	}
    	Log.e("CloudPhotoAlbumAdapter", "setMap() picList.size() = "+picList.size());
    	notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, String> infoMap = infoList.get(position);
        final ArrayList<String> picL = (ArrayList<String>) picList.get(position);
        final ArrayList<String> picIdL = (ArrayList<String>) picIdList.get(position);
        ViewHolder holder = new ViewHolder();
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.lv_item_cloud_photo_album_layout, null);
        }
        holder.photoAlbumLayout = (LinearLayout) convertView.findViewById(R.id.photo_album_layout);
        holder.typeLayout = (LinearLayout) convertView.findViewById(R.id.type_layout);
        holder.row1Layout = (LinearLayout) convertView.findViewById(R.id.pic_row1_layout);
        holder.row2Layout = (LinearLayout) convertView.findViewById(R.id.pic_row2_layout);
        holder.photoNumbersTV = (TextView) convertView.findViewById(R.id.photo_numbers_tv);
        holder.dateTV = (TextView) convertView.findViewById(R.id.photo_date_tv);
        holder.intrudeTV = (TextView) convertView.findViewById(R.id.intrude_tv);
        holder.tempAbnormalTV = (TextView) convertView.findViewById(R.id.temperature_abnormal_tv);
        holder.pic1IV = (ImageView) convertView.findViewById(R.id.pic1_iv);
        holder.pic2IV = (ImageView) convertView.findViewById(R.id.pic2_iv);
        holder.pic3IV = (ImageView) convertView.findViewById(R.id.pic3_iv);
        holder.pic4IV = (ImageView) convertView.findViewById(R.id.pic4_iv);
        
        setLargeScreenParams(holder);
        int picNumbers = picL.size();
        
        final String picDate = infoMap.get("picDate");
        holder.photoNumbersTV.setText(picNumbers+unit);
        holder.dateTV.setText(picDate);
        if (infoMap.get("isIntrude").equals("0")) {
            holder.intrudeTV.setVisibility(View.GONE);
        }
        if (infoMap.get("isCapture").equals("0")) {
            holder.tempAbnormalTV.setVisibility(View.GONE);
        }
        
                
        switch (picNumbers) {
            case 0:
                holder.row1Layout.setVisibility(View.GONE);
                holder.row2Layout.setVisibility(View.GONE);
                break;
            case 1:
                displayPic1(picL, picIdL, holder, picDate);
                holder.pic2IV.setVisibility(View.INVISIBLE);
                holder.pic3IV.setVisibility(View.GONE);
                holder.pic4IV.setVisibility(View.GONE);
                holder.row2Layout.setVisibility(View.GONE);
                break;
            case 2:
                displayPic1(picL, picIdL, holder, picDate);
                displayPic2(picL, picIdL, holder, picDate);
                holder.pic3IV.setVisibility(View.GONE);
                holder.pic4IV.setVisibility(View.GONE);
                holder.row2Layout.setVisibility(View.GONE);
                break;
            case 3:
                displayPic1(picL, picIdL, holder, picDate);
                displayPic2(picL, picIdL, holder, picDate);
                displayPic3(picL, picIdL, holder, picDate);
                holder.pic4IV.setVisibility(View.INVISIBLE);
                break;
            case 4:
            default:
                displayPic1(picL, picIdL, holder, picDate);
                displayPic2(picL, picIdL, holder, picDate);
                displayPic3(picL, picIdL, holder, picDate);
                displayPic4(picL, picIdL, holder, picDate);
                break;
        }
        
        return convertView;
    }

    private void displayPic4(final ArrayList<String> picL, final ArrayList<String> picIdL, ViewHolder holder, final String picDate) {
        if (StringUtil.isNotEmpty(picL.get(3))) {
            finalBitmap.display(holder.pic4IV, picL.get(3));
            holder.pic4IV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectTo(picL, picIdL, picDate);
                }
            });
        }
    }

    private void displayPic3(final ArrayList<String> picL, final ArrayList<String> picIdL, ViewHolder holder, final String picDate) {
        if (StringUtil.isNotEmpty(picL.get(2))) {
            finalBitmap.display(holder.pic3IV, picL.get(2));
            holder.pic3IV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectTo(picL, picIdL, picDate);
                }
            });
        }
    }

    private void displayPic2(final ArrayList<String> picL, final ArrayList<String> picIdL, ViewHolder holder, final String picDate) {
        if (StringUtil.isNotEmpty(picL.get(1))) {
            finalBitmap.display(holder.pic2IV, picL.get(1));
            holder.pic2IV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectTo(picL, picIdL, picDate);
                }
            });
        }
    }

    private void displayPic1(final ArrayList<String> picL, final ArrayList<String> picIdL, ViewHolder holder, final String picDate) {
        if (StringUtil.isNotEmpty(picL.get(0))) {
            finalBitmap.display(holder.pic1IV, picL.get(0));
            holder.pic1IV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectTo(picL, picIdL, picDate);
                }
            });
        }
    }

    private void setLargeScreenParams(ViewHolder holder) {
        int screenHeight = ScreenUtil.getScreenHeight(activity);
        if (screenHeight > 854 && screenHeight <= 1280) {
            LayoutParams params1 = new LayoutParams(200, 200);
            holder.pic1IV.setLayoutParams(params1);
            holder.pic1IV.setScaleType(ScaleType.FIT_XY);
            
            LayoutParams params2 = new LayoutParams(200, 200);
            params2.leftMargin = 20;
            holder.pic2IV.setLayoutParams(params2);
            holder.pic2IV.setScaleType(ScaleType.FIT_XY);
            
            LayoutParams params3 = new LayoutParams(200, 200);
            holder.pic3IV.setLayoutParams(params3);
            holder.pic3IV.setScaleType(ScaleType.FIT_XY);
            
            LayoutParams params4 = new LayoutParams(200, 200);
            params4.leftMargin = 20;
            holder.pic4IV.setLayoutParams(params4);
            holder.pic4IV.setScaleType(ScaleType.FIT_XY);
            
                                                                                                                   
                                             
                                            
                                                               
                                                           
            
            LayoutParams typeParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            typeParams.leftMargin = 20;
            typeParams.topMargin = 20;
            holder.typeLayout.setLayoutParams(typeParams);
            
            LayoutParams row1Params = new LayoutParams(LayoutParams.MATCH_PARENT, 200);
            row1Params.topMargin = 20;
            row1Params.leftMargin = 20;
            row1Params.rightMargin = 20;
            row1Params.bottomMargin = 20;
            holder.row1Layout.setLayoutParams(row1Params);
            
            LayoutParams row2Params = new LayoutParams(LayoutParams.MATCH_PARENT, 200);
            row2Params.leftMargin = 20;
            row2Params.rightMargin = 20;
            row2Params.bottomMargin = 20;
            holder.row2Layout.setLayoutParams(row2Params);
        }
    }
    
    private void redirectTo(final ArrayList<String> picL, ArrayList<String> picIdL, final String picDate) {
        Intent intent = new Intent(context, CloudPhotoAlbumDetailsActivity.class);
        intent.putStringArrayListExtra("picUrlList", picL);
        intent.putStringArrayListExtra("picIdList", picIdL);
        intent.putExtra("picDate", picDate);
                                                          
        activity.startActivityForResult(intent, 0x202);
    }
    
    class ViewHolder {
        LinearLayout photoAlbumLayout;
        LinearLayout typeLayout;
        LinearLayout row1Layout;
        LinearLayout row2Layout;
        TextView photoNumbersTV;
        TextView dateTV;
        TextView intrudeTV;
        TextView tempAbnormalTV;
        ImageView pic1IV;
        ImageView pic2IV;
        ImageView pic3IV;
        ImageView pic4IV;
    }

}
