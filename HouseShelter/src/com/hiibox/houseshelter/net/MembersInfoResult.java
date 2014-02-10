  
  
  
  
  

package com.hiibox.houseshelter.net;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

    
  
  
  
  
  
public class MembersInfoResult {

	public int totalMembers = -1;              
	public int currIndex = -1;             
	    
  
  
	public String cardNum;
	    
  
  
	public String nickname;
	    
  
  
	public byte[] url;
	    
  
  
	public int status;
	
	public static List<MembersInfoResult> membersList = null;           
	
	public static List<MembersInfoResult> getMembersList() {
		return membersList;
	}

	public static int parseMembersInfo(Frame f) {
		if (null == membersList) {
			membersList = new ArrayList<MembersInfoResult>();
		}
		MembersInfoResult result = null;
		ArrayList<byte[]> arrData = FrameTools.split(f.aryData, '\t');
		int len = arrData.size();
		if (len < 2) {
			return -1;
		}
		result = new MembersInfoResult();
		result.totalMembers = Integer.parseInt(FrameTools.getFrameData(arrData
				.get(1)));
		result.currIndex = Integer.parseInt(FrameTools.getFrameData(arrData
				.get(2)));
		Log.d("MembersInfoResult",
				"parse()  arrData.get(0) = " + FrameTools.getFrameData(arrData.get(0))
						+ " ; arrData.get(1) = " + FrameTools.getFrameData(arrData.get(1))
						+ " ; arrData.get(2) = " + FrameTools.getFrameData(arrData.get(2))
						+ " ; arrData.get(3) = " + FrameTools.getFrameData(arrData.get(3))
						+ " ; arrData.get(4) = " + FrameTools.getFrameData(arrData.get(4))
						+ " ; arrData.get(5) = " + FrameTools.getFrameData(arrData.get(5)));
		result.cardNum = FrameTools.getFrameData(arrData.get(3));
		result.nickname = FrameTools.getFrameData(arrData.get(4));
		result.status = Integer.parseInt(FrameTools.getFrameData(arrData.get(5)));
		if (len > 6) {
			result.url = arrData.get(6);
			Log.d("MembersInfoResult", "parse() arrData.get(6) = " + FrameTools.getFrameData(arrData.get(6)));
		}
		membersList.add(result);
		                                 
		                              
		                                                                                                                       
		return (result.totalMembers - result.currIndex);
	}
	
	public static MembersInfoResult parse(Frame f) {
		MembersInfoResult result = null;
		ArrayList<byte[]> arrData = FrameTools.split(f.aryData, '\t');
		int len = arrData.size();
		if (len < 3) {
			return result;
		}
		result = new MembersInfoResult();
		result.totalMembers = Integer.parseInt(FrameTools.getFrameData(arrData.get(1)));
		result.currIndex = Integer.parseInt(FrameTools.getFrameData(arrData.get(2)));
		result.cardNum = FrameTools.getFrameData(arrData.get(3));
		result.nickname = FrameTools.getFrameData(arrData.get(4));
		result.status = Integer.parseInt(FrameTools.getFrameData(arrData.get(5)));
		if (len > 6) {
                                   
                                              
                                                                                 
          
			result.url = arrData.get(6);
		}
        Log.d("MembersInfoResult", "parse()  cardNum = " + result.cardNum.trim() + " ; nickname = "
                + result.nickname.trim() + " ; status = " + result.status + " ; headPortraint = "
                + result.url);
		return result;
	}
}
