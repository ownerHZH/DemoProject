package com.hiibox.houseshelter.net;

import java.net.InetAddress;
import java.util.ArrayList;

import android.util.Log;
                                                                             
                                                                          

   
  
  
  
  
  
  
  
public class TCPMainClient extends TCPServiceClientV2 {
    
    public final static int MAIN_CMD = 0x0e;
    String terminal = null;
    String tpwd = null;

       
  
  
  
  
  
  
    public TCPMainClient(String h, int p, String t, String tpass) {
        super(h, p, true);
        terminal = t;
        tpwd = tpass;
    }

    public TCPMainClient(InetAddress h, int p, String t, String tpass) {
        super(h, p, true);
        terminal = t;
        tpwd = tpass;
    }
    
    @Override
    public Frame createLoginFrame(String user, String psw) {
    	Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x0e;
        f.subCmd = 21;
        f.strData = user;
        return f;
    }
    
    public void userAuth(String phone, CommandListener listener) {
    	Frame f = createLoginFrame1(phone);
    	Log.d("TCPMainClient", "userAuth()  data = "+f.strData);
    	this.sendToQueue(f, listener, 10);
    }
    
    public Frame createLoginFrame1(String user) {
        Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x0E;
        f.subCmd = 21;
        f.strData = user;
        return f;
    }

       
  
  
  
    public void startDefine(String phone, String time, CommandListener l) {
        Frame f = createFrame();
                          
        f.subCmd = 1;
        f.strData = phone + "\t" + time;
        Log.e("TCPMainClient", "[布防] : "+f.platform+" ; "+f.version+" ; "+f.mainCmd+" ; "+f.subCmd+" ; "+f.strData);
        this.sendToQueue(f, l);
    }

       
  
  
  
    public void finishDefine(String phone, String time, CommandListener l) {
        Frame f = createFrame();
                          
        f.subCmd = 2;
        f.strData = phone + "\t" + time;
        Log.e("TCPMainClient", "[撤防] : "+f.platform+" ; "+f.version+" ; "+f.mainCmd+" ; "+f.subCmd+" ; "+f.strData);
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void capture(String userid, String time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 0x03;
        f.strData = userid + "\t" + time;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
    public void readCard(String userid, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 0x04;
        f.strData = userid;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
    public void regCard(int mode, String phone, String cardNum, String nickname, byte[] headPortrait, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 0x05;
        f.strData = mode + "\t" + phone+"\t"+cardNum+"\t"+nickname+"\t"+headPortrait;
        Log.e("TCPMainManager", "regCard()   data = "+f.strData);
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void unregCard(String phone, String card, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 0x06;
        f.strData = phone + "\t" + card;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void paramCaptureSet(String userid, int num, String time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 0x07;
        f.strData = user + "\t" + num + "\t" + time;
                                      
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void setCaptureNum(String userid, int num, String time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 7;
        f.strData = userid + "\t" + num + "\t" +time;
        Log.d("TCPMainClient", "[设置抓拍张数]  upload data : "+f.strData);
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void paramCaptureTimeSet(String userid, int time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 0x08;
        f.strData = user + "\t" + time;
                                      
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void paramVideoPasswordSet(String userid, String pwd, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 0x09;
        f.strData = user + "\t" + pwd;
                                      
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void checkVideoPassword(String userid, String pwd, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 10;
        f.strData = user + "\t" + pwd;
                                      
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
    public void paramCapturePixelSet(String userid, String pixel, String time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 11;
        f.strData = user + "\t" + pixel + "\t" + time;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
    public void setCaptureTime(String userid, String time, int order, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 12;
        f.strData = userid + "\t" + time + "\t" + order;
        Log.d("TCPMainClient", "[设置定时抓拍时间]  upload data :time = "+time+" ; order = "+order+" ; data = "+f.strData);
        this.sendToQueue(f, l);
    }

       
  
  
  
  
    public void beepOn(String userid, String time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 13;
        f.strData = userid + "\t1" + "\t" + time;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
    public void beepOff(String userid, String time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 13;
        f.strData = userid + "\t0" + "\t" + time;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
  
    public void paramPasswordSet(String phone, String oldpwd, String newpwd, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 20;
        f.strData = phone + "\t" + oldpwd + "\t" + newpwd;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
  
    public void queryFiles(String userid, int filetype, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 39;
        f.strData = userid + "\t" + filetype;
        this.sendToQueue(f, l);
    }

       
  
  
  
  
  
    public void queryOutRecords(String userid, CommandListener l) {
        this.queryOutRecordsInner(userid + "\t0", l);
    }

       
  
  
  
  
  
  
    public void queryOutRecords(String userid, String nickname, CommandListener l) {
    	this.queryOutRecordsInner(userid + "\t1\t" + nickname, l);
    }

       
  
  
  
  
  
  
    public void queryOutRecordsTime(String userid, String time, CommandListener l) {
        this.queryOutRecordsInner(userid + "\t2\t" + time, l);
    }

       
  
  
  
  
  
  
  
    public void queryOutRecords(String userid, String nickname, String time, CommandListener l) {
    	this.queryOutRecordsInner(userid + "\t3\t" + nickname + "\t" + time, l);
    }



    void queryOutRecordsInner(String data, CommandListener l) {
        query(68, data, new QueryWarnListener(l));
    }

       
  
  
  
  
    public void queryDefineRecords(String userid, CommandListener l) {
        this.query(69, userid + "\t0", new QueryWarnListener(l));
    }

       
  
  
  
  
  
  
    public void queryDefineRecords(String userid, String time, CommandListener l) {
        this.query(69, userid + "\t1\t" + time, new QueryWarnListener(l));
    }

       
  
  
  
  
  
    public void queryDefineRecords20(String userid, CommandListener l) {
        this.query(69, userid + "\t2\t1", new QueryWarnListener(l));
    }

       
  
  
  
  
  
    public void queryDefineRecords31(String userid, CommandListener l) {
        this.query(69, userid + "\t3\t2", new QueryWarnListener(l));
    }
    
       
  
  
  
  
    public void queryDefineRecords34(String phone, String time, CommandListener l) {
        this.query(69, phone + "\t4\t"+ time, new QueryWarnListener(l));
    }
    
       
  
  
  
  
    public void queryDefineRecords35(String phone, String time, CommandListener l) {
        this.query(69, phone + "\t5\t"+ time, new QueryWarnListener(l));
    }

       
  
  
  
  
  
    public void queryWarnRecord(String userid, CommandListener l) {
        this.query(70, userid+"\t0", new QueryWarnListener(l));
    }
    
       
  
  
  
  
  
    public void queryTemperatureWarnRecord(String userid, CommandListener l) {
        this.query(70, userid+"\t1", new QueryWarnListener(l));
    }

       
  
  
  
  
  
    public void queryInvadeWarnRecord(String userid, CommandListener l) {
        this.query(70, userid+"\t2", new QueryWarnListener(l));
    }

       
  
  
  
  
  
  
    public void queryWarnRecord(String userid, String time, CommandListener l) {
        this.query(70, userid+"\t3\t" + time, new QueryWarnListener(l));
    }

       
  
  
  
  
  
  
  
    public void switchTerminal(String userid, String tcode, String pwd, CommandListener l) {
                                                             
    	Frame f = createFrame();
        f.subCmd = 71;
        f.strData = userid + "\t" + tcode + "\t" + pwd;
        Log.e("TcpMainClient", "切换终端    tcode = "+tcode+"; pwd = "+pwd+";");
        this.sendToQueue(f, l);
    }

       
   
  
  
  
  
    public void queryDefineStatus(String tcode, CommandListener l) {
        this.query(72, tcode, l);
    }

       
  
  
  
  
  
    public void queryUserLevel(String userid, CommandListener l) {
        this.query(73, userid, l);
    }

       
  
  
  
  
  
    public void queryTemperature(String userid, CommandListener l) {
        this.query(74, userid, l);
    }

       
  
  
  
  
  
    public void queryDampness(String userid, CommandListener l) {
        this.query(75, userid, l);
    }

       
  
  
  
  
    public void queryUserPoints(String userid, CommandListener l) {
        this.query(76, userid, l);
    }

       
  
  
  
  
  
    public void queryUserInfo(String userid, CommandListener l) {
        this.query(77, userid, l);
    }

       
  
  
  
  
  
  
  
    public void modifyUserInfo(String userid, String nickname, String tel, CommandListener l) {
        this.query(78, userid + "\t" + nickname + "\t" + tel, l);
          







    
    }
    
       
  
  
  
  
    public void queryEmergencyTelephone(String userid, CommandListener l) {
        this.query(79, userid, l);
    }
    
       
  
  
  
  
    public void getDeviceCode(String phone, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 80;
        f.strData = phone;
        this.sendToQueue(f, l);
    }
    
       
  
  
  
  
  
    public void uploadFeedBack(String phone, String msg, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 81;
        f.strData = phone + "\t" + msg;
        this.sendToQueue(f, l);
    }
    
       
  
  
  
  
    public void getDeviceInfo(String phone, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 82;
        f.strData = phone;
        this.sendToQueue(f, l);
    }
    
       
  
  
  
  
    public void getAdsUrl(String phone, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 83;
        f.strData = phone;
        this.sendToQueue(f, l);
    }
    
       
  
  
  
  
  
    public void getTemperatureAry(String phone, String time, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 84;
        f.strData = phone + "\t" + time;
        Log.d("TCPMainClient", "getTemperatureAry() strData = "+f.strData);
        this.sendToQueue(f, l);
    }
    
       
  
  
  
  
    public void getAboutInfo(String phone, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 85;
        f.strData = phone;
        this.sendToQueue(f, l);
    }
    
       
  
  
  
  
  
  
  
  
  
  
    public void getAppUrl(String phone, int platform, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 86;
        f.strData = phone + "\t" + platform + "\t" + 0;
        this.sendToQueue(f, l);
    }
    
       
  
  
  
  
  
  
    public void deleteDevice(String phone, String deviceCode, String authCode, CommandListener l) {
    	Frame f = createFrame();
        f.subCmd = 87;
        f.strData = phone + "\t" + deviceCode + "\t" + authCode;
        this.sendToQueue(f, l);
    }
    
       
  
  
    public void getCaptureNumber(String deviceCode, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 88;
        f.strData = deviceCode;
        this.sendToQueue(f, l);
    }
    
       
  
  
    public void getCaptureTime(String deviceCode, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 89;
        f.strData = deviceCode;
        this.sendToQueue(f, l);
    }
    
       
  
  
    public void getCaptureParams(String phone, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = 90;
        f.strData = phone;
        this.sendToQueue(f, l);
    }
    
      


   

  

       
  
  
  
  
  
    void query(int subcmd, String data, CommandListener l) {
        Frame f = createFrame();
        f.subCmd = subcmd;
        f.strData = data;
                                                                                            
        this.sendToQueue(f, l, 90);
    }

    public static Frame createFrame() {
        Frame f = new Frame();
        f.platform = PLATFORM_APP;
        f.mainCmd = MAIN_CMD;
        f.version = VERSION_1;
        return f;
    }

       
  
  
    public static class QueryWarnListener2 implements CommandListener {
        CommandListener listener = null;

        public QueryWarnListener2(CommandListener l) {
            listener = l;
        }

        @Override
        public int onReceive(Frame src, Frame f) {
            ArrayList<byte[]> datas = FrameTools.split(f.aryData, '\t');
            if (listener != null) {
                listener.onReceive(src, f);
            }
            if (datas.size() < 3) {
                return 0;
            }
            return Integer.parseInt(FrameTools.getFrameData(datas.get(1))) - Integer.parseInt(FrameTools.getFrameData(datas.get(2))) - 1;
        }

        @Override
        public void onTimeout(Frame src, Frame f) {
            if (listener != null) {
                listener.onTimeout(src, f);
            }
        }
    }
    
    public static class QueryWarnListener implements CommandListener {
    	int total = -1;
    	int received = 0;
        CommandListener listener = null;
        public QueryWarnListener(CommandListener l) {
            listener = l;
        }
		@Override
		public int onReceive(Frame src, Frame f) {
			 ArrayList<byte[]> datas = FrameTools.split(f.aryData, '\t');
	            if (listener != null) {
	                listener.onReceive(src, f);
	            }
	            if (datas.size() < 3) {
	                return 0;
	            }
	            if (total == -1) {
	            	total = Integer.parseInt(FrameTools.getFrameData(datas.get(0)));
	            }
	            received +=  Integer.parseInt(FrameTools.getFrameData(datas.get(1)));
	            Log.e("TCPMainClient", "datas.get(0)="+FrameTools.getFrameData(datas.get(0))+" ; datas.get(1)="+FrameTools.getFrameData(datas.get(1))+" ; datas.get(2)="+FrameTools.getFrameData(datas.get(2)));
	            Log.e("TCPMainClient", "QueryWarnListener  total = "+total+" ; received = "+received+" ; release = "+(total-received));
	            return total - received;
		}
		@Override
		public void onTimeout(Frame src, Frame f) {
			if(listener != null) {
				listener.onTimeout(src, f);
			}
		}
    }
    
    public static class QueryWarnListener1 implements CommandListener {
        int total = -1;
        int received = 0;
        CommandListener listener = null;
        public QueryWarnListener1(CommandListener l) {
            listener = l;
        }
        @Override
        public int onReceive(Frame src, Frame f) {
            ArrayList<byte[]> datas = FrameTools.split(f.aryData, '\t');
            if (listener != null) {
                listener.onReceive(src, f);
            }
            if (datas.size() < 3) {
                return 0;
            }
            if (total == -1) {
                total = Integer.parseInt(FrameTools.getFrameData(datas.get(1)));
            }
            received +=  Integer.parseInt(FrameTools.getFrameData(datas.get(2)));
            Log.e("TCPMainClient", "datas.get(0)="+FrameTools.getFrameData(datas.get(0))+" ; datas.get(1)="+FrameTools.getFrameData(datas.get(1))+" ; datas.get(2)="+FrameTools.getFrameData(datas.get(2)));
            Log.e("TCPMainClient", "QueryWarnListener  total = "+total+" ; received = "+received+" ; release = "+(total-received));
            return total - received;
        }
        @Override
        public void onTimeout(Frame src, Frame f) {
            if(listener != null) {
                listener.onTimeout(src, f);
            }
        }
    }

}
