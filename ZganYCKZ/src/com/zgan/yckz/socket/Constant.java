package com.zgan.yckz.socket;

public class Constant {
	/**
	 * 数据错误
	 */
	public final static int DATAERROR = 1000;
	public final static int DATASUCCESS = 2000;
	public final static int CHECKCODEERROR = 3000;
	public final static int NETERROR = 4000;
	public final static int SERVERERROR = 5000;

	public final static int HOUSE_PLATFROM = 4;
	public final static int LOGIN_SERVERPLATFROM = 1;
	// 家庭卫士代码
	public final static int HOUSE_SERVERPLATFROM = 6;
	public final static int MSG_SERVERPLATFROM = 7;
	public final static int FILE_SERVERPLATFROM = 8;

	public final static byte HOUSE_SVRID = 0x0e;
	public final static byte MSG_SVRID = 0x0d;
	public final static byte FILE_SVRID = 0x0F;

	public final static byte USERID = 0x15;

	public final static byte LOGIN = 0X01;
	public final static byte LOGINSUBCMD = 0X01;
	public final static byte VERSION = 0X01;
	// index
	public final static byte INDEX_cbMainCmdID = 0x03;
	public final static byte INDEX_cbSubCmdID = 0x00;
	public final static byte INDEX_cbMessageVer = 0x01;
	// list
	public final static byte List_cbMainCmdID = 0x01;
	public final static byte List_cbSubCmdID = 0x04;
	public final static byte List_cbMessageVer = 0x01;
	//报警
	public final static byte INDEX_bjMainCmdID = 0x0d;
	public final static byte INDEX_bjSubCmdID = 0x15;

}