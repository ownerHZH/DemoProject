package com.zgan.community.data;

import java.io.Serializable;

/**
 * 时事政务
 * @author HZH
 *
 */
public class ContentData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String CID;
	private String Title;
	private String CContent;
	private String ContentTime;
	private String Publishers;
	
	public String getCID() {
		return CID;
	}
	public void setCID(String cID) {
		CID = cID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getCContent() {
		return CContent;
	}
	public void setCContent(String cContent) {
		CContent = cContent;
	}
	public String getContentTime() {
		return ContentTime;
	}
	public void setContentTime(String contentTime) {
		ContentTime = contentTime;
	}
	public String getPublishers() {
		return Publishers;
	}
	public void setPublishers(String publishers) {
		Publishers = publishers;
	}
}
