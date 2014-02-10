package com.hiibox.houseshelter.net;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

    
  
  
  
  
  
  
  
  
public class CommandResult {

    private List<String> mList = null;
    private int[] temp = null;
    
    public CommandResult() {
        super();
        mList = new ArrayList<String>();
        temp = new int[10];
    }

    public List<String> getList() {
        return mList;
    }
    
    public int[] getTempAry() {
        return temp;
    }
    
        
  
  
  
    public void praseAdsUrl(Frame f) {
        if (null == f) {
            return;
        }
        ArrayList<byte[]> arrData = FrameTools.split(f.aryData, '\t');
        if (arrData.size() < 2) {
            return;
        }
        String[] records = FrameTools.getFrameData(arrData.get(1)).split("\n");
        for (int i = 0; i < records.length; i ++) {
            mList.add(records[i]);
                                                                          
        }
    }
    
        
  
  
  
    public void praseTemperatureAry(Frame f) {
        if (null == f) {
            return;
        }
        if (null == f.aryData) {
            return;
        }
        ArrayList<byte[]> arrData = FrameTools.split(f.aryData, '\t');
        if (arrData.size() < 2) {
            return;
        }
        for (int i = 1; i < arrData.size(); i ++) {
            String s = FrameTools.getFrameData(arrData.get(i));
            temp[i-1] = Math.round(Float.valueOf(s));
            Log.i("CommandResult", "ÎÂ¶ÈÇúÏß±í" + i + " = "+temp[i-1]);
        }
    }
}
