package com.zgan.community.data;

import java.io.Serializable;

/**
 * 办公地点
 * @author HZH
 *
 */
public class MSZW_BGDD implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//		ID
	private String name;//		名字
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
}
