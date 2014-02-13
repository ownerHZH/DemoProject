package com.zgan.community.data;

import java.util.Date;

/**
 * 社区服务实体类
 * @author Hzh
 *
 */
public class CommunityService {

	private int id;               //服务ID
	private String name;          //名称
	private String address;       //地址
	private String telephone;     //电话
	private Date publishTime;     //发布时间
	private String publisher;     //发布人
	private int communityId;      //社区ID
	private String gpsCoordinates;//百度地址
	
	public CommunityService(String name, String address, String telephone) {
		this.name = name;
		this.address = address;
		this.telephone = telephone;
	}
	
	public CommunityService(){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
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
	public String getGpsCoordinates() {
		return gpsCoordinates;
	}
	public void setGpsCoordinates(String gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}

}
