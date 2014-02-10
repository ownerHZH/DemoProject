package com.hiibox.houseshelter.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

                          

public class TCPSend      {
	private SocketChannel socketChannel;
	private Queue<byte[]> send_Queue = new LinkedList<byte[]>();
	                                   
	public TCPSend(String strServerIP, int intPort, Selector _selector,
			Queue<byte[]> _queue) {

		this.send_Queue = _queue;

		try {
			                           
			socketChannel = SocketChannel.open(new InetSocketAddress(
					strServerIP, intPort));
			socketChannel.configureBlocking(false);

			                    
			socketChannel.register(_selector, SelectionKey.OP_READ);

		} catch (IOException e) {
			                                  
			e.printStackTrace();
		}

		              
		                                                     
		                               
	}

	public synchronized int send() {
		if (send_Queue.size() > 0) {
			byte[] sendByte = null;

			sendByte = send_Queue.poll();

			ByteBuffer writeBuffer = ByteBuffer.wrap(sendByte);

			try {
				
				
				socketChannel.write(writeBuffer);

			} catch (IOException e) {
				
				e.printStackTrace();
				return -1;
			}
			return 1;
		}
		return 0;
	}
	
}
