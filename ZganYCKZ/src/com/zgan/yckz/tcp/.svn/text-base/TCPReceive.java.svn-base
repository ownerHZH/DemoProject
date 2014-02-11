package com.zgan.yckz.tcp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

public class TCPReceive implements Runnable{
	private Selector selector;
	private Queue<byte[]> Queue=new LinkedList<byte[]>(); 
	
	public TCPReceive(Selector _selector,Queue _queue){
		this.selector=_selector;	
		Queue=_queue;
		
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		 try {
			while (selector!=null && selector.select() > 0) {
				for (SelectionKey sk : selector.selectedKeys()) {
					// 如果该SelectionKey对应的Channel中有可读的数据
			          if (sk.isReadable()) {
			            // 使用NIO读取Channel中的数据
			            SocketChannel sc = (SocketChannel) sk.channel();
			            ByteBuffer buffer = ByteBuffer.allocate(1024);
			            sc.read(buffer);
			            buffer.flip();
			            
			            // 为下一次读取作准备
			            sk.interestOps(SelectionKey.OP_READ);
			            
			            byte[] resultByte = new byte[buffer.limit()];            
			            
			            buffer.get(resultByte);
			            
			            Queue.offer(resultByte);
			        }
		          // 删除正在处理的SelectionKey
		          selector.selectedKeys().remove(sk);
				}
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
