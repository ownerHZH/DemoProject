package com.hiibox.houseshelter.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

                          

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
					                                   
			          if (sk.isReadable()) {
			                                 
			            SocketChannel sc = (SocketChannel) sk.channel();
			            ByteBuffer buffer = ByteBuffer.allocate(1024);
			            sc.read(buffer);
			            buffer.flip();
			            
			                        
			            sk.interestOps(SelectionKey.OP_READ);
			            
			            byte[] resultByte = new byte[buffer.limit()];            
			            
			            buffer.get(resultByte);
			            
			                  
			            if(resultByte!=null && resultByte.length>0 &&
			            		resultByte[0]==36 && resultByte[1]==90 
			            		&&resultByte[2]==71 && resultByte[3]==38){
			            	Queue.offer(resultByte);			            	
			            }else{
			            	selector.close();
			            } 			            
			        }		          
			          
		                                
		          selector.selectedKeys().remove(sk);
				}
			 }
		} catch (IOException e) {
			                                  
			e.printStackTrace();
		}		
	}

}
