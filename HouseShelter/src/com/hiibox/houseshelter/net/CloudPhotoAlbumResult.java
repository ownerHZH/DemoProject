  
  
  
  
  

package com.hiibox.houseshelter.net;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.util.Log;


    
  
  
  
public class CloudPhotoAlbumResult {

	public int totalNumber = 0;
	public int currIndex = 0;
	public int count = 0;
	
	private TreeMap<String, List<PhotoInfo>> map = null;
	
	public TreeMap<String, List<PhotoInfo>> getMap() {
		return map;
	}
	
	public void clearMap() {
	    if (null != map) {
	        map.clear();
	    }
	    currIndex = 0;
	    totalNumber = 0;
	    count = 0;
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
		if (null == map) {
			map = new TreeMap<String, List<PhotoInfo>>();
		}
		totalNumber = Integer.parseInt(FrameTools.getFrameData(aryData.get(1)));
		currIndex = Integer.parseInt(FrameTools.getFrameData(aryData.get(2)));
		count += currIndex;
		Log.e("CloudPhotoAlbumResult", "prasePhotoAlbum()  totalNumber = "+totalNumber+" ; currIndex = "+currIndex+" ; count = "+count);
		int m = 3;
		for (int i = 0; i < currIndex; i ++) {
			PhotoInfo info = new PhotoInfo();
			String[] ret = FrameTools.getFrameData(aryData.get(m)).split("\n");
			info.type = Integer.parseInt(ret[0]);
			info.date = ret[1].substring(0, 10);                   
			info.fileId = ret[2];
			info.url = ret[3];
                                                                                                                                                    
			if (map.containsKey(info.date)) {
				map.get(info.date).add(info);
			} else {
				List<PhotoInfo> temp = new ArrayList<CloudPhotoAlbumResult.PhotoInfo>();
				temp.add(info);
				map.put(info.date, temp);
			}
			m ++;
		}
		return (totalNumber - count);
	}
	
}
