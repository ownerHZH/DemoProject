package com.zgan.yckz.tcp;


public class Frame {
	
	/**
	 * 平台代码 
	 * 比如：终端Platform = 3
	 * */
	public int Platform=0;
	
	/**
	 * 协议版本
	 * 初始版本为1，以后如果存在重大功能变更则修改协议版本，代码分支，以便扩展并支持前期服务。
	 * */
	public int Version=1;
	
	/**
	 * 主功能命令字 
	 * 登陆为0x01，中心服务器返回为0x0c，其余操作为0x0e【终端信息】
	 * */
	public byte MainCmd=0;
	
	/**
	 * 子功能命令字
	 * */
	public int SubCmd=0;	

	/**
	 * Data数据
	 * */
	public String strData="";
	
	/**
	 * Data数据
	 * */
	public byte[] aryData=null;
    
    public Frame(){
    	
    }
    
    public Frame(byte[] Buff){
    	FrameTools.getByteToFrame(Buff, this);
    }      
}
