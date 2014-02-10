  
  
  
  
  

package com.hiibox.houseshelter.net;

import java.io.IOException;
import java.net.InetAddress;

import android.util.Log;

import com.hiibox.houseshelter.net.TCPMainClient.QueryWarnListener;
import com.hiibox.houseshelter.net.TCPMainClient.QueryWarnListener1;
import com.hiibox.houseshelter.util.FileUtil;

    
  
  
  
public class TCPFileClient extends TCPServiceClientV2 {

	public TCPFileClient(InetAddress h, int p) {
		super(h, p, true);
	}

	public void userAuth(String phone, CommandListener listener) {
		Frame f = createFrame();
        f.subCmd = 21;
        f.strData = phone;
    	this.sendToQueue(f, listener);
	}
	
	private Frame createFrame() {
		Frame f = new Frame();
		f.platform = 4;
        f.mainCmd = 0x0F;
        f.version = 1;
        return f;
	}
	
	@Override
    public Frame createLoginFrame(String user, String psw) {
        Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x0f;
        f.subCmd = 21;
        f.strData = user;
        return f;
    }
	
	    
  
  
  
  
  
  
	public void queryPhotos(String phone, String time, int type, CommandListener listener) {
		Frame f = createFrame();
		f.subCmd = 39;
		f.strData = phone+"\t"+time+"\t"+type;
		Log.d("TCPFileClient", "queryPhotos()  f.strData = "+f.strData+" ; subCmd = "+f.subCmd+" ; type = "+type);
		this.sendToQueue(f, new QueryWarnListener1(listener));
	}
	
	    
  
  
  
  
  
  
	public void queryInvadePhotos(String phone, String fileId, CommandListener listener) {
	    Frame f = createFrame();
	    f.subCmd = 39;
	    f.strData = phone+"\t"+fileId+"\t"+6;
	    Log.d("TCPFileClient", "queryPhotos()  f.strData = "+f.strData+" ; subCmd = "+f.subCmd);
	    this.sendToQueue(f, new QueryWarnListener(listener));
	}
	
	    
  
  
  
  
  
	public void deletePhoto(String phone, String fileId, CommandListener listener) {
		Frame f = createFrame();
		f.subCmd = 41;
		f.strData = phone + "\t" + fileId;
		this.sendToQueue(f, listener);
	}
	
                                                 
	
	    
  
  
  
  
  
  
	public void saveFileRequest(String phone, int fileSize, String time, int type, CommandListener listener) {
		Frame f = createFrame();
		f.subCmd = 18;
		f.strData = phone+"\t"+fileSize+"\t"+time+"\t"+type+"\t0";
		Log.d("TCPFileClient", "saveFileRequest()  f.strData = "+f.strData);
		this.sendToQueue(f, listener);
	}
	
	    
  
  
  
  
  
	public void saveFile(String start, String end, byte[] stream, CommandListener listener) {
		Frame f = createFrame();
		f.subCmd = 19;
		f.strData = start+"\t"+end+"\t"+stream;
		Log.i("TCPFileManager", "saveFile()  f.strData = "+f.strData);
		this.sendToQueue(f, listener);
	}
	
	public void uploadFile(int fileSize, byte[] file, CommandListener listener) {
		Log.e("TCPFileManager", "uploadFile()    fileSize = "+fileSize+" ; file.length = "+file.length);
		int pages = fileSize / 2048;        
		int realPages = (fileSize % 2048 == 0) ? pages : (pages+1);       
		                                   
		byte[] bytesBuff = new byte[2066];
		Frame response=null;
		if (realPages == 1) {
			String startHex = subcontract(0);
			String endHex = subcontract(fileSize);
			String temp = startHex+"\t"+endHex+"\t";   
			System.arraycopy(temp.getBytes(), 0, bytesBuff, 0, 18);
			System.arraycopy(file,0	, bytesBuff, 18, file.length);
			try {
				createUploadFile(bytesBuff,file.length+18, listener);
				response = this.recvFrame(30);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			int errored = 0;
			for (int i = 0; i < realPages; i ++) {
				int startPosition = i * 2048;         
				int endPosition = 0;         
				if (i == realPages - 1) {
					endPosition = fileSize - 1;
				} else {
					endPosition = (i + 1) * 2048 - 1;
				}
				String startPositionHex = subcontract(startPosition);             
				String endPositionHex = subcontract(endPosition);             
				String temp = startPositionHex+"\t"+endPositionHex+"\t";   
				Log.e("TCPFlieManager", ".... startPositionHex = "+startPositionHex+"  ;  endPositionHex = "+endPositionHex+" ; temp = "+temp);
				System.arraycopy(temp.getBytes(), 0, bytesBuff, 0, 18);
				System.arraycopy(file,startPosition	, bytesBuff, 18,  (endPosition-startPosition+1));
				                                                                  
				try {
					createUploadFile(bytesBuff, (endPosition-startPosition+1)+18, listener);
				} catch (IOException e) {
					e.printStackTrace();
					errored = 1;
					break;
				}
			}
			if (errored ==0) {
				try {
					response = this.recvFrame(30);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (listener != null) {
		if ( response == null) {
			listener.onTimeout(null, null);
		}
		else {
			listener.onReceive(null, response);
		}
		}
	}
	
	    
  
  
  
  
	private String subcontract(int decimal) {
		String hex = FileUtil.DecToHex(decimal);              
		int len = hex.length();
		if (len < 8) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < (8-len); i ++) {
				sb.append("0");
			}
			hex = sb.toString() + hex;
		}
		return hex;
	}
	
	private void createUploadFile(byte[] data,int len, CommandListener listener) throws IOException {
		Frame f = createFrame();
		f.subCmd = 19;
		f.aryData = new byte[len];
		System.arraycopy(data, 0, f.aryData, 0, len);
		                    
		Log.i("TCPFileManager", "createUploadFile()  f.strData = "+data);
		this.sendToServer(FrameTools.getFrameBuffData(f));
		                       
		                                 
	}
	
}
