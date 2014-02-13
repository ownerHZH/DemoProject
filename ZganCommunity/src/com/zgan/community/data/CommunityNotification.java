package com.zgan.community.data;

import java.util.Date;
/**
 * 社区通知实体类
 * @author Hzh
 *
 */
public class CommunityNotification {
	private int id;	         //社区公告ID
	private String title;    //标题
	private String content;  //公告内容
	private Date contentTime;//发布时间
	private String publisher;//发布人
	private int communityId; //社区ID
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getContentTime() {
		return contentTime;
	}
	public void setContentTime(Date contentTime) {
		this.contentTime = contentTime;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getCommunityId() {
		return communityId;
	}
	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

}
