package com.zgan.yckz.tcp;

import java.io.UnsupportedEncodingException;
import java.nio.channels.Selector;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.channels.Selector;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

public class FrameTools {

	public static final int Frame_Max = 512;
	public static final int Frame_Len = 12;
	public static final byte Frame_MainCmd_Login = 0x01;
	public static final byte Frame_MainCmd_Centr = 0x0C;
	public static final byte Frame_MainCmd_Client = 0x0E;
	public static final byte Frame_MainCmd_Ping = 0x00;
	public static Selector selector;
	
	public static boolean Thread_Ping=true;
	public static int Thread_PingTime=0;
	
	/**
	 * 发送队列
	 * */
	public static Queue<byte[]> Queue_Send = new LinkedList<byte[]>(); 
	
	/**
	 * 接收队列
	 * */
	public static Queue<byte[]> Queue_Receive = new LinkedList<byte[]>(); 
	
	public static void toSendTcpData(Frame f){
		byte[] Buff=null;
		Buff=FrameTools.getFrameBuffData(f);
		
		if(Buff!=null){
			Queue_Send.offer(Buff);
		}
	}
	
	/**
	 * 创建数据包
	 * */
	public static byte[] getFrameBuffData(Frame f) {
		byte[] Buff = null;
		byte[] dataBuff = null;
		int intDataLen = 0;

		if (f.strData != null && !f.strData.equals("")) {
			try {
				dataBuff = f.strData.getBytes("UTF-8");

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			intDataLen = dataBuff.length;
		}

		Buff = new byte[Frame_Len + intDataLen];

		/** 包头 ************/
		Buff[0] = 36;
		Buff[1] = 90;
		Buff[2] = 71;
		Buff[3] = 38;
		/**************/

		// 平台代码
		IntToHighLowByte(Buff, 4, f.Platform);

		// 版本号
		Buff[6] = (byte) f.Version;

		// 主功能命令字
		Buff[7] = (byte) f.MainCmd;

		// 子功能命令字
		Buff[8] = (byte) f.SubCmd;

		// 数据长度
		IntToHighLowByte(Buff, 9, intDataLen);
		// 数据内容
		
		if(intDataLen>0){
			System.arraycopy(dataBuff, 0, Buff, 11, intDataLen);
		}	

		// 校验码
		Buff[Buff.length - 1] = getCheckSum(Buff);

		return Buff;
	}

	public static void getByteToFrame(byte[] Buff, Frame f) {
		int intVersion = 0;

		// 检查版本
		intVersion = Buff[2] & 0xFF;
		Log.i("Buff[6] & 0xFF", ""+intVersion);
		switch (intVersion) {
		case 1:
			getByteToFrame_Version_1(Buff, f);
			break;
		}

	}
	

	/**
	 * 解析数据包 Version:1
	 * */
	private static void getByteToFrame_Version_1(byte[] Buff, Frame f) {
		byte CheckSum = 0;
		int intDataLen=0;
		byte[] aryData=null;
		
		// 校验码校验
		CheckSum = Buff[Buff.length-1];

		if (CheckSum == getCheckSum(Buff)) {
			// 平台代码
			f.Platform = HighLowToInt(Buff[0], Buff[1]);
			Log.i("Buff[6] & 0xFF", "f.Platform"+f.Platform);

			// 版本号
			f.Version = Buff[2] & 0xFF;
			Log.i("Buff[6] & 0xFF", "f.Version"+f.Version);

			// 主功能命令字
			f.MainCmd = Buff[3];
			Log.i("Buff[6] & 0xFF", "f.MainCmd"+f.MainCmd);

			// 子功能命令字
			f.SubCmd = Buff[4] & 0xFF;
			Log.i("Buff[6] & 0xFF", ""+f.SubCmd);

			// 数据长度
			intDataLen=HighLowToInt(Buff[5], Buff[6]);
			
			aryData=new byte[intDataLen];
			
			System.arraycopy(Buff, 7, aryData, 0, intDataLen);
			
			f.aryData=aryData;
			
			f.strData=getFrameData(f.aryData);
		}
	}

	private static String getFrameData(byte[] buff) {
		String strData = "";

		if(buff!=null){
			try {
				strData=new String(buff,"GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return strData;
	}

	private static byte getCheckSum(byte[] Buff) {
		byte b = 0;

		for (int i = 0; i < (Buff.length - 1); i++) {
			b ^= Buff[i];
		}

		return b;
	}

	/**
	 * 数字型转换高低位
	 * */
	private static void IntToHighLowByte(byte[] aryData, int intS, int intData) {
		int hValue = (intData & 0xFF00) >> 8;
		int lValue = intData & 0xFF;

		aryData[intS] = (byte) hValue;
		aryData[intS + 1] = (byte) lValue;
	}

	/**
	 * 高低位转换成数字型
	 * */
	public static int HighLowToInt(byte hb, byte lb) {
		int intH = hb & 0xFF;
		int intL = lb & 0xFF;

		String strBinary = DecToBinary(intH, 8)+ DecToBinary(intL, 8);

		return Integer.valueOf(strBinary, 2);
	}
	
	/**
	 * 十进制转二进制
	 * **/
	public static String DecToBinary(int intDec) {
		return Integer.toBinaryString(intDec);
	}
	
	/**
	 * 十进制转二进制
	 * **/
	public static String DecToBinary(int intDec, int intLen) {
		String strBinary = "";
		String strZ = "";
		int intStrLen = 0;

		strBinary = Integer.toBinaryString(intDec);

		intStrLen = intLen - strBinary.length();

		if (intStrLen > 0) {
			for (int i = 0; i < intStrLen; i++) {
				strZ += "0";
			}

			strBinary = strZ + strBinary;
		}

		return strBinary;
	}
}
