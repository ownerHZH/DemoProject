  
  
  
  

package com.hiibox.houseshelter.net;

import java.io.IOException;
import java.net.InetAddress;

import android.util.Log;

    
  
  
  
  
public class TCPMsgClient extends TCPServiceClientV2 {

    public static int MAIN_CMD = 0x0d;
    CommandListener cmdlistener = null;

    public TCPMsgClient(InetAddress h, int p, CommandListener l) {
        super(h, p, true);
        cmdlistener = l;
    }
    
    public TCPMsgClient(InetAddress h, int p) {
    	super(h, p, true);
    }
    
                                                                     
                             
                         
                             
                                      
     
    
        
  
  
  
  
    public void replyMsg(int subCmd, int msgId) {
    	Frame f = createFrame();
    	f.subCmd = subCmd;
    	f.strData = msgId + "\t" + 0;
    	Log.e("TCPMsgClient", "向服务器反馈接收到的信息  subCmd = "+subCmd+" ; strData = "+f.strData);
    	try {
			this.sendToServer(FrameTools.getFrameBuffData(f));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public Frame createLoginFrame(String user, String psw) {
        Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x0d;
        f.subCmd = 21;
        f.strData = user;
        return f;
    }

    @SuppressWarnings("unused")
	public void run() {
        connectInner();
        if (listener != null) {
            listener.onClientStart();
        }
        Command cmd = null;
        if (0 == logined) {
            loginReal();
        }

        if (-1 == logined) {
            if (listener != null) {
                listener.onLoginFail();
            }
            running = false;
        }
        int count = 0;
        while (running) {
            try {
                           
                Frame f = recvFrame(10);
                if (f != null ) {
                                                                                                                                       
                	if( cmdlistener != null) {
                			int rc = cmdlistener.onReceive(null, f);
                }
                } else {
                	count ++;
                    try {
                        Thread.sleep(50);
                        if (count == 3) {                       
                            count = 0;
                            createLinkTestFrame();
                            Frame f1 = recvFrame(45);
                                                                                                                                                     
                            if (null == f1) {                        
                                break;
                            } else {
                            	count = 0;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("TCPMsgClient", "InterruptedException ................."+e.toString());
                    }
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

    static Frame createFrame() {
        Frame f = new Frame();
        f.platform = PLATFORM_APP;
        f.mainCmd = (byte) (MAIN_CMD & 0xff);
        f.version = VERSION_1;
        return f;
    }
}
