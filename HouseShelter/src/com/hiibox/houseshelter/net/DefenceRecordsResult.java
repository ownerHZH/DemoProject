  
  
  
  
  

package com.hiibox.houseshelter.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

    
  
  
  
public class DefenceRecordsResult {

	private List<Map<String, String>> list = null;
	private int currRecords = 0;
	
	public DefenceRecordsResult() {
		super();
		list = new ArrayList<Map<String,String>>();
	}
	
	public List<Map<String, String>> getList() {
		Log.d("OutRecordsResult", "getList()  list = "+list);
		return list;
	}
	
	public void clearList() {
		if (null != list && list.size() > 0) {
			currRecords = 0;
			list.clear();
		}
	}

	    
  
  
  
  
	public int getResult(Frame f) {
                                                 
		ArrayList<byte[]> arrData = FrameTools.split(f.aryData, '\t');
		if(arrData.size() < 2) {
			return -1;
		}
		int totalRecords = Integer.parseInt(FrameTools.getFrameData(arrData.get(0)));
		if (totalRecords == 0) {
			return 0;
		}
		currRecords += Integer.parseInt(FrameTools.getFrameData(arrData.get(1)));
                                                                               
		for (int m = 3; m < arrData.size(); m ++) {
			String[] records = FrameTools.getFrameData(arrData.get(m)).split(",");
			Map<String,String> map = new HashMap<String, String>();
			String[] timeStr = records[1].split(" ");
			map.put("date", timeStr[0]);
			map.put("time", timeStr[1]);
			map.put("name", records[0]);
			if (records.length == 3) {
				map.put("accessFlag", records[2]);
			} else {
				map.put("accessFlag", "0");
			}
			list.add(map);            
		}
		return (totalRecords - currRecords);
	}
                                                                
                                                 
                                                                   
                             
                        
      
                                                                                  
                             
                        
      
                                                      
                                                                                   
                                                                               
               
                                              
           
                                                                             
                                                              
                                                
                                   
                                   
                                   
                                         
                           
                                             
             
       
           
      
                       
     

}
