  
  
  
  
  

package com.hiibox.houseshelter.net;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;


    
  
  
  
public class InvadePhotoResult {

	public int totalNumber = 0;
	public int currIndex = 0;
	public int count = 0;
	
	private List<PhotoInfo> list = null;
	
	public List<PhotoInfo> getList() {
	    return list;
	}
	
	public class PhotoInfo {
		int type;       
		String date;       
		String url;          
		String fileId;
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getFileId() {
			return fileId;
		}
		public void setFileId(String fileId) {
			this.fileId = fileId;
		}
		
	}
	
	public int prasePhotoAlbum(Frame f) {
		if (null == f) {
			return -1;        
		}
		ArrayList<byte[]> aryData = FrameTools.split(f.aryData, '\t');
		if (aryData.size() < 3) {
			return -1;
		}
		if (null == list) {
		    list = new ArrayList<InvadePhotoResult.PhotoInfo>();
		}
		totalNumber = Integer.parseInt(FrameTools.getFrameData(aryData.get(1)));
		currIndex = Integer.parseInt(FrameTools.getFrameData(aryData.get(2)));
		count += currIndex;
		Log.i("CloudPhotoAlbumResult", "prasePhotoAlbum()  totalNumber = "+totalNumber+" ; currIndex = "+currIndex+" ; count = "+count);
		for (int m = 3; m < aryData.size(); m ++) {
			PhotoInfo info = new PhotoInfo();
			String[] ret = FrameTools.getFrameData(aryData.get(m)).split("\n");
			info.type = Integer.parseInt(ret[0]);
			info.date = ret[1];
			info.fileId = ret[2];
			info.url = ret[3];
			list.add(info);
			Log.d("CloudPhotoAlbumResult", "prasePhotoAlbum()  type = "+info.type+" ; date = "+info.date+" ; url = "+info.url+" ; fileId = "+info.fileId);
		}
		return (totalNumber - currIndex);
	}
	
}
