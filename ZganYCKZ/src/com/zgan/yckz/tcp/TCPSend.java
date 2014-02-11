package com.zgan.yckz.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

public class TCPSend implements Runnable {
	private SocketChannel socketChannel;
	private Queue<byte[]> send_Queue = new LinkedList<byte[]>();

	public TCPSend(String strServerIP, int intPort, Selector _selector,
			Queue _queue, Queue _rqueue) {

		this.send_Queue = _queue;

		try {
			// 打开监听信道并设置为非阻塞模式
			socketChannel = SocketChannel.open(new InetSocketAddress(
					strServerIP, intPort));
			socketChannel.configureBlocking(false);

			// 打开并注册选择器到信道
			socketChannel.register(_selector, SelectionKey.OP_READ);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 启动接收线程
		TCPReceive tr = new TCPReceive(_selector, _rqueue);
		TCPPing tp = new TCPPing();
	}

	@Override
	public void run() {
		while (true) {
			if (send_Queue.size() > 0) {
				byte[] sendByte = null;

				sendByte = send_Queue.poll();

				ByteBuffer writeBuffer = ByteBuffer.wrap(sendByte);

				try {
					//停止心跳
					FrameTools.Thread_Ping=false;					
					FrameTools.Thread_PingTime=0;

					socketChannel.write(writeBuffer);

					FrameTools.Thread_Ping=true;

					Log.i("TCPSend", "发送数据......");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
