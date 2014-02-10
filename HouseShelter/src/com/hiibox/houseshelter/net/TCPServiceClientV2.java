package com.hiibox.houseshelter.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import android.util.Log;
import java.util.HashMap;


  
  
  

   
  
  
public class TCPServiceClientV2 implements Runnable {
    public final static int PLATFORM_APP = 0x04;
    public final static int PLATFORM_MSG = 0x07;
    public final static int VERSION_1 = 0x01;
    final static byte[] EMPTY=new byte[0];
    private static final String TAG = "TCPServiceClientV2";

       
  
  
  
  
    public static interface ClientListener {
           
  
  
        public void onClientStop();

           
  
  
        public void onClientStart();

           
  
  
        public void onClientClose();

           
  
  
  
  
        public void onClientException(IOException ex);

           
  
  
        public void onLoginFail();
    }

       
  
  
    public static interface CommandListener {
           
  
  
  
  
  
        public int onReceive(Frame src, Frame f);

           
  
  
        void onTimeout(Frame src, Frame f);
    }

       
  
  
  
  
  
    public static class Command {
        public Frame source;

           
  
  
  
  
  
        public Command(Frame f, CommandListener l) {
            init(FrameTools.getFrameBuffData(f), l);
            source = f;
        }

           
  
  
  
  
  
        public Command(Frame f, CommandListener l, int t) {
            init(FrameTools.getFrameBuffData(f), l);
            source = f;
            timeout = t;
        }

           
  
  
  
  
  
        private void init(byte[] d, CommandListener l) {
            data = d;
            listener = l;
        }

           
  
  
  
  
  
  
        @SuppressWarnings("unused")
        private void init(byte[] d, CommandListener l, int t) {
            data = d;
            listener = l;
            timeout = t;
        }

        public byte[] data;
        public CommandListener listener;
        int timeout = 60;
    }

       
  
    
    public Queue<Command> queueSend = new LinkedList<Command>();

       
  
    
    public Queue<byte[]> queueReceive = new LinkedList<byte[]>();


    public ClientListener listener;
    String host;
    InetAddress haddr;
    int port;
    String user;
    String pwd;
    boolean connected = false;
    boolean running = false;
    Selector selector = null;
    Thread thread = null;
    Thread sendThread = null;
    public Vector<Command> listSended = new Vector<Command>();

    TCPReceive receiver = null;
    private SocketChannel socketChannel;
    InetSocketAddress serverInfo;

                 
    HashMap<Integer,CommandListener> otherListener = new HashMap<Integer,CommandListener>();

    public void registe(int subcmd,CommandListener l) {
        otherListener.put(subcmd,l);
    }
    public void onOtherFrame(Frame f) {
        if(otherListener.containsKey(f.subCmd)) {
            otherListener.get(f.subCmd).onReceive(null,f);
        }
    }
                                        
    int logined = 0;

    public TCPServiceClientV2(InetAddress h, int p, boolean needlogin) {
        haddr = h;
        port = p;
        serverInfo = new InetSocketAddress(haddr, port);
        if (needlogin) {
            logined = 0;
        } else {
            logined = 2;
        }
    }

    public TCPServiceClientV2(String h, int p, boolean needlogin) {
        host = h;
        port = p;
        serverInfo = new InetSocketAddress(host, port);
        if (needlogin) {
            logined = 0;
        } else {
            logined = 2;
        }
    }

    public boolean isConnected() {
        return connected;
    }

       
  
  
    public synchronized void clearSendQueue() {
        this.queueSend.clear();
    }

    public int sendQueueSize() {
        return this.queueSend.size();
    }

    public boolean connectInner() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(serverInfo);
            socketChannel.configureBlocking(false);
                          
            socketChannel.register(selector, SelectionKey.OP_READ);
            Log.i("TCPServiceClientV2", "connectInner()    socketChannel已打开");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
                                                                  
                       
        return true;
    }

    public void connect() {
        start();
    }

       
  
  
  
  
    public boolean connect(String user) {
        this.user = user;
        start();
        return true;
    }

       
  
  
  
  
    public boolean connect(String user, String pwd) {
        this.user = user;
        this.pwd = pwd;
        start();
        return true;
    }

    private void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
        
                     
                                                 
                              
                            
                  
                                                 
                                
                                    
                
                     
                                  
                
                                
                                          
                                      
              
                                          
                                          
                             
                                         
              
        
             
                           
                                      
            
             
                             
        
    }
                                    
    public Command findSource(Frame f) {
    	for(Iterator<Command> it = listSended.iterator();it.hasNext();) {
    		Command cmd = it.next();
    		if(cmd.source.mainCmd == f.mainCmd && cmd.source.subCmd==f.subCmd) {
    			it.remove();
    			return cmd;
    		}
    	}
    	return null;
    }

                                          
               
                              
                                             
                                                                                               
           
  
       

    @SuppressWarnings("unused")
    @Override
    public void run() {
        connected = connectInner();
        Log.d("TCPServiceClientV2", "run()  connected = " + connected + "  ; logined = " + logined);
        if (!isConnected()) {
            if (listener != null) {
                listener.onLoginFail();
            }
            return;
        }
        if (listener != null) {
            listener.onClientStart();
        }
       
        if (0 == logined) {        
            loginReal();
        }
        if (-1 == logined) {        
            if (listener != null) {
                listener.onLoginFail();
            }
            running = false;
        }
        
        if (running) {
        	           
            sendThread = new Thread(new Runnable(){
            	public void run() {
            		while(running) {
            			try {
            				 Command cmd = sendFirstToServer();
            				 if(cmd != null) {
            					 listSended.add(cmd);
            				 }
            				 else {
            					 Thread.sleep(100);
            				 }
    					} catch (IOException e) {
    					    if (null != listener) {
    					        listener.onClientException(e);
    					    }
    						break;
    					} catch (InterruptedException e2) {
    						e2.printStackTrace();
    						                                 
    						break;
    					}
            		}
            		running =false;
            		                          
            	}
            });
            sendThread.start();
        }
        
        int count = 0;
        while (running) {
            try {
            	 Frame f = recvFrame(45);
                 if (f != null) {
                	 count = 0;
                	 Command cmd = this.findSource(f);
                	 if(cmd == null) {
                		 
                      	Log.e("TCPServiceClientV2", "run()  f.mainCmd = "+f.mainCmd+" ; f.subCmd = "+f.subCmd+" ; class = "+this.getClass().getName());
                          onOtherFrame(f);
                          continue;
                	 }
                 	if(cmd.listener == null) {
                 		Log.i("TCPServiceClientV2","收到回应，但无法处理:f.mainCmd = "+f.mainCmd+" ; f.subCmd = "+f.subCmd+" ; class = "+this.getClass().getName());
                 		continue;
                 	}
                     int rc = cmd.listener.onReceive(cmd.source, f);
                      Log.i("TCPServiceClientV2", "run()  rc = " + rc);
                                          
                     while (rc >0) {
                     	Log.i(TAG, "run()   cmd.listener  "+cmd.listener.getClass().getName());
                         Frame temp = recvFrame(30);
                                                                    
                         Log.i("TCPServiceClientV2", "run()  temp = " + temp);
                         if (temp == null) {
                             cmd.listener.onTimeout(cmd.source, null);
                             break;
                         } else {
                             rc = cmd.listener.onReceive(cmd.source, temp); 
                             
                         }
                     }
                 }
                 else {        
                	 if(count >=3) {
                		 Log.i("TCPServiceClientV2", "连续三次未收到数据,断开 " );
                		 break;
                	 }
                	 queueLinkTestFrame();
                	 count += 1;
                 }
               
                   


  





  
  



  
  




  
            } catch (IOException ex) {
                if (listener != null) {
                    listener.onClientException(ex);
                }
                break;
            }
        }
        close();
    }
    
    protected void loginReal() {
        Command login = createLogin(user, pwd);
        try {
        	Log.i("TCPServiceClientV2", "loginReal()  user = "+user+" ; pwd = "+pwd+" ; login = "+login);
            sendToServer(login);
            if (login != null) {
                Frame f = recvFrame(30);
                                                                                    
                if (f != null && f.strData.compareTo("0") == 0) {
                    logined = 2;
                                                                               
                } else {
                    logined = -1;
                                                                                
                }
            }
        } catch (IOException e) {
            if (listener != null) {
                listener.onClientException(e);
            }
            logined = -1;
            running = false;
        }
    }

    protected void createLinkTestFrame() throws IOException {
        Frame f1 = new Frame();
        f1.platform = 4;
        f1.version = 1;
        f1.mainCmd = 0x00;
        f1.subCmd = 0;
        Log.d("TCPServiceClientV2", "发送心跳包...");
        this.sendToServer(FrameTools.getFrameBuffData(f1));
    }
    protected void queueLinkTestFrame() throws IOException {
        Frame f1 = new Frame();
        f1.platform = 4;
        f1.version = 1;
        f1.mainCmd = 0x00;
        f1.subCmd = 0;
        Log.d("TCPServiceClientV2", "发送心跳包...");
        this.sendToQueue(f1, null);
                                                             
    }

       
  
  
    public void close() {
    	Log.e(TAG, this.getClass().getName()+" : close .......................................");
    	try {
        	if (null != socketChannel) {
        		this.socketChannel.close();
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    	user = null;
    	pwd = null;
        if (listener != null) {
            listener.onClientStop();
        }
        connected = false;
        running = false;
        
        if (listener != null) {
            listener.onClientClose();
        }
    }

       
  
  
    public void stop() {
        running = false;
    }

       
  
  
    public boolean isRunning() {
        return running;
    }

       
  
  
  
  
  
  
    public Frame execute(Frame f, int timeout) throws IOException {
        Command c = new Command(f, null, timeout);
        this.sendToServer(c);
        Frame f2 = this.recvFrame(timeout);
        return f2;
    }

       
  
  
  
  
    public synchronized void sendToQueue(Frame f, CommandListener l) {
        sendToQueue(f, l, 60);
    }

       
  
  
  
  
  
    public synchronized void sendToQueue(Frame f, CommandListener l, int timeout) {
        byte[] buff = FrameTools.getFrameBuffData(f);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buff.length; i++) {
            sb.append(buff[i]);
        }
        Log.d("TCPServiceXlientV2", "sendToQueue() ...... send data = " + sb.toString()
                + "  , running = " + running + "; connected = " + isConnected());
        this.queueSend.offer(new Command(f, l, timeout));
    }

    public byte[] recvBytesAll(int maxLen, int timeout) throws IOException {
    	byte buf[] = new byte[maxLen];
    	int pos = 0;
    	long bstart = System.currentTimeMillis()/1000;
    	long bend = System.currentTimeMillis()/1000;
    	Log.d(TAG, "recvBytesAll()  maxLen = "+maxLen+"; "+" ; bstart = "+bstart+" ; bend = "+bend+" ; timeout = "+timeout);
    	while (pos < maxLen && (bend-bstart < timeout)) {
    		byte[] temp = recvBytes(maxLen-pos,(int)(timeout-(bend-bstart)));
    		if (temp == null) {
    			Log.d(TAG, "recvBytesAll()  maxLen = "+maxLen+"; "+(timeout-(bend-bstart)));
    			return null;
    		}
    		System.arraycopy(temp, 0, buf, pos, temp.length);
    		pos += temp.length;
    		bend = System.currentTimeMillis()/1000;
    	}
    	
    	if(pos == maxLen) {
    		return buf;
    	}
    	
    	return null;
    }
       
  
  
  
  
  
  
    public byte[] recvBytes(int maxLen, int timeout) throws IOException {
    	
        if (selector != null && selector.select(timeout * 1000) > 0) {
            for (SelectionKey sk : selector.selectedKeys()) {
                                                   
                if (sk.isReadable()) {
                                         
                    SocketChannel sc = (SocketChannel) sk.channel();
                    
                    
                    
                    ByteBuffer buffer = ByteBuffer.allocate(maxLen);
                           
                    int count = sc.read(buffer);
                                                                                                                  
                    buffer.flip();
                                
                    sk.interestOps(SelectionKey.OP_READ);
                    selector.selectedKeys().remove(sk);
                    if (count > 0 ) {

                    byte[] resultByte = new byte[count];

                    buffer.get(resultByte);
                    
                    logln("接收数据:" +count);
                    for (int i = 0; i < count; i++) {
                        System.out.print(String.format("%02x ", resultByte[i] & 0xff));
                    }
                    logln("");

                           
                      
  
  
  
  
                    
                                                                                           
                    return resultByte;
                    }
                    return EMPTY;
                } else {
                    Log.d(TAG,getClass().getName()+ " recvBytes(maxLen,timeout)... 网络不可读!");
                }
                                      
                selector.selectedKeys().remove(sk);
            }
        } else {
            Log.d(TAG,getClass().getName()+ " recvBytes(maxLen,timeout)... 无返回数据!");
        }
        return null;
    }

       
  
  
  
  
  
                                                               
         
                                                                        
                                                               
                                                     
                                        
                                           
                                                                      
                                                                    
                                      
                                    
                                  
                                                           
                                                                   
                                             
                             
                     
                                                         
                                                                                      
                                        
                          
                                                                 
                   
                                        
                                                     
               
              
                  
                                                         
           
                      
            
                                      
       
    
    public Frame recvFrame(byte mcmd,int subcmd,int timeout) throws IOException {
    	Frame f =  null;
    	long start = System.currentTimeMillis()/1000;
    	long end = start+timeout;
    	while (start < end) {
    		f = recvFrame((int)(end-start));
    		if(f == null) {
    			break;
    		}
    		if (f.subCmd == subcmd && f.mainCmd == mcmd) {
    			break;
    		}
            onOtherFrame(f);
    		start = System.currentTimeMillis()/1000;
    	}
    	return f;
    }
    public Frame recvFrame(int timeout) throws IOException {
                    
        byte[] alldata = new byte[4096];
        int pos = 0;
        int dataLen = -1;
        byte[] data = null;
        data = recvBytes(12, timeout);
        if (data == null) {
            Log.d(TAG, getClass().getName()+" recvFrame() ...  未读到数据！");
            return null;
        }
                            
          
  
  
        System.arraycopy(data, 0, alldata, pos, data.length);
        if (dataLen == -1) {
            dataLen = FrameTools.HighLowToInt(alldata[9], alldata[10]);
                                                                                               
        } else {
            return null;
        }

        pos += data.length;
                                                   
        if (pos < dataLen + 12) {
            data = recvBytesAll(dataLen + 12 - pos, timeout);
            if (data != null && data.length < 4096) {
                System.arraycopy(data, 0, alldata, pos, data.length);
                pos += data.length;
            }
            else {
            	logln("再次读取数据是失败！");
            }
        }
        if (pos >= dataLen + 12) {
            logln("接收数据: 接到长度:" + pos);
            for (int i = 0; i < pos; i++) {
                System.out.print(String.format("%02x ", alldata[i] & 0xff));
            }
            logln("");

                                                  
            return new Frame(alldata);
        } else {
        	logln("接收数据: 接到长度:" + pos+" ,dataLen=" + dataLen);
            for (int i = 0; i < pos; i++) {
                System.out.print(String.format("%02x ", alldata[i] & 0xff));
            }
            logln("");

            return null;
        }
    }

    public synchronized Command sendFirstToServer() throws IOException {
        Command cmd = null;
        if (this.queueSend.size() > 0) {
            cmd = queueSend.poll();
            sendToServer(cmd);
            return cmd;
        }
        return cmd;
    }

    synchronized void sendToServer(byte[] data) throws IOException {
        logln(getClass().getName()+ "sendToServer(byte[] data) 上传数据:");
        for (int i = 0; i < data.length; i++) {
            log(String.format("%02x ", data[i] & 0xff));
        }
        logln("");
        ByteBuffer writeBuffer = ByteBuffer.wrap(data);
        socketChannel.write(writeBuffer);
    }

    synchronized void sendToServer(Command cmd) throws IOException {
        logln(getClass().getName()+" sendToServer(Command cmd) 上传数据:");
        for (int i = 0; i < cmd.data.length; i++) {
            log(String.format("%02x ", cmd.data[i] & 0xff));
        }
        logln("");
        ByteBuffer writeBuffer = ByteBuffer.wrap(cmd.data);
        Log.i("TCPServiceClientV2", "sendToServer()  writeBuffer = "+writeBuffer+" ; socketChannel = "+socketChannel);
        if (null == socketChannel) {
        	return;
        }
        socketChannel.write(writeBuffer);
    }

    public Frame createLoginFrame(String user, String psw) {
        Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x01;
        f.subCmd = 0x01;
        f.strData = user + "\t" + psw;
        return f;
    }

    public Command createLogin(String user, String psw) {
        Frame f = createLoginFrame(user, psw);
        return new Command(f, null, 1);
    }

    public static void logln(String str) {
        System.out.println(str);
    }

    public static void log(String str) {
        System.out.print(str);
    }
}
