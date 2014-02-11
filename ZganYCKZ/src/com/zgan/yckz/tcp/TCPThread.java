package com.zgan.yckz.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.zgan.yckz.activity.Main_Acitivty;
import com.zgan.yckz.activity.User_Login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TCPThread implements Runnable {
	Socket _socket;
	String ServerIP = "";
	Context context;
	int ServerPort = 0;
	byte[] _buff = null;

	public TCPThread(Context context, String strIP, int intPort, byte[] b) {
		ServerIP = strIP;
		ServerPort = intPort;
		this.context = context;
		_buff = b;
		new Thread(this).start();
	}

	public TCPThread(String strIP, int intPort, byte[] b) {
		ServerIP = strIP;
		ServerPort = intPort;
		_buff = b;
		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			_socket = new Socket(ServerIP, ServerPort);
			OutputStream ops = _socket.getOutputStream();
			ops.write(_buff);
			ops.flush();

			InputStream sin = null;
			byte[] resultByte = null;

			while (true) {

				resultByte = new byte[1024];
				sin = _socket.getInputStream();

				sin.read(resultByte);
				// ¼ì²é°üÍ·
				if (resultByte != null && resultByte.length > 0
						&& resultByte[0] == 36 && resultByte[1] == 90
						&& resultByte[2] == 71 && resultByte[3] == 38) {

					FrameTools.Queue_Receive.offer(resultByte);
				} else {
					sin.close();
					_socket.close();
				}

			}

			// _socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
