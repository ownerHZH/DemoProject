package com.hiibox.houseshelter.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TCPServiceClient implements Runnable {
	public static interface FrameReceiveListener {
		public void onReceive(Frame f) ;
	}
	public static class QueueRecevier implements FrameReceiveListener {
		public ArrayList<FrameReceiveListener> listeners = new ArrayList<FrameReceiveListener>();
		public void onReceive(Frame f) {
			for (FrameReceiveListener recv : listeners) {
				recv.onReceive(f);
			}
		}
	}
	    
  
    
	public Queue<byte[]> queueSend = new LinkedList<byte[]>(); 
	
	    
  
    
	public Queue<byte[]> queueReceive = new LinkedList<byte[]>(); 
	
	
	QueueRecevier receivers = new QueueRecevier();
	String host;
	int port;
	boolean connected = false;
	boolean running = true;
	Selector selector=null;	
	Thread thread = null;
	TCPSend sender = null;
	TCPReceive receiver = null;
	private SocketChannel socketChannel;
	
	public TCPServiceClient(String h,int p) {
		host = h;
		port = p;
	}
	public boolean isConnected() {
		return connected;
	}
	public synchronized void addFrameReceiveListener(FrameReceiveListener l) {
		receivers.listeners.add(l);
	}
	public synchronized void removeFrameReceiveListener(FrameReceiveListener l) {
		receivers.listeners.remove(l);
	}
	public synchronized void clearSendQueue() {
		this.queueSend.clear();
	}
	boolean connectInner() {
		
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open(new InetSocketAddress(
					host, port));
			socketChannel.configureBlocking(false);

			                     
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			                                   
			e.printStackTrace();
			return false;
		}
		sender = new TCPSend(host,port,selector,this.queueSend);
		receiver = new TCPReceive(selector, this.queueReceive);
		running = true;
		thread = new Thread(this);
		
		thread.start();
		  









  
		return true;
	}
	public boolean connect() {
		if (isConnected()) {
			return true;
		}
		connected = connectInner();
		if (!isConnected()) {
			return false;
		}
		return true;
	}
	
	public void run() 
	{
		int status = 0;
		while (running) {
			status = send();
			if ( status == -1) {
				stop();
			}
			recv();
			byte[] resultByte=null;
			while(this.queueReceive.size()>0){
				
				
				resultByte=queueReceive.poll();
				                       
				Frame f=new Frame(resultByte);
				this.receivers.onReceive(f);
				  






   
			}
		}
		connected = false;
		close();
	}
	public void close() {
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public void stop() {
		running =false;
	}
	public boolean isRunning() {
		return running;
	}
	public synchronized void send(Frame f) {
		byte[] buff = FrameTools.getFrameBuffData(f);
		this.queueSend.offer(buff);
	}
	public void recv() {
		try {
			while (selector!=null && selector.select() > 0) {
				for (SelectionKey sk : selector.selectedKeys()) {
					                                           
			          if (sk.isReadable()) {
			                                      
			            SocketChannel sc = (SocketChannel) sk.channel();
			            ByteBuffer buffer = ByteBuffer.allocate(1024);
			            sc.read(buffer);
			            buffer.flip();
			            
			                             
			            sk.interestOps(SelectionKey.OP_READ);
			            
			            byte[] resultByte = new byte[buffer.limit()];            
			            
			            buffer.get(resultByte);
			            
			            this.queueReceive.offer(resultByte);
			        }
		                                     
		          selector.selectedKeys().remove(sk);
				}
			 }
		} catch (IOException e) {
			                                   
			e.printStackTrace();
		}
	}
	public synchronized int send() {
		if (this.queueSend.size() > 0) {
			byte[] sendByte = null;

			sendByte = queueSend.poll();

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
