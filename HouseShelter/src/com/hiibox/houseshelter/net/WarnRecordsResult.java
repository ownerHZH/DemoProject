  
  
  
  
  

package com.hiibox.houseshelter.net;

import java.util.ArrayList;

    
  
  
  
public class WarnRecordsResult {
	
	
	public static class WarnRecord {
		    
  
  
		public String warnType;
		    
  
  
		public String time;
		    
  
  
		public String url;
		    
  
  
		public String temperature;
	}
	
	int replyCmd;
	int frameCount;
	ArrayList<WarnRecord> records = new ArrayList<WarnRecord>();

	    
  
  
  
	public int getReplyCmd() {
		return replyCmd;
	}

	    
  
  
  
	public int getFrameCount() {
		return frameCount;
	}

	    
  
  
  
	public ArrayList<WarnRecord> getRecords() {
		return records;
	}
	
	public static WarnRecordsResult prase(Frame f) {
		WarnRecordsResult result = null;
		ArrayList<byte[]> arrData = FrameTools.split(f.aryData, '\t');
		if(arrData.size() < 2) {
			return result;
		}
		result = new WarnRecordsResult();
		result.replyCmd = Integer.parseInt(FrameTools.getFrameData(arrData.get(0)));
		result.frameCount = Integer.parseInt(FrameTools.getFrameData(arrData.get(1)));
		int sz = (arrData.size()-2)/2;
		int j = 0;
		for(int i = 0 ; i < sz; i ++) {
			j = i * 2 + 2;
			WarnRecord temp = new WarnRecord();
			temp.warnType = FrameTools.getFrameData(arrData.get(j));
			if (temp.warnType.equals("0")) {
				temp.temperature = FrameTools.getFrameData(arrData.get(j+1));
			} else {
				temp.url = FrameTools.getFrameData(arrData.get(j+1));
			}
			result.records.add(temp);
		}
		return result;
	}

	    
  
  
  
  
	  
 








  




  
  



  
}
