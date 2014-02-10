package com.hiibox.houseshelter.net;

import java.io.IOException;


public class TCPLoginClient extends TCPServiceClientV2 {

    public TCPLoginClient(String h, int p) {
        super(h, p, true);

    }

    public TCPLoginClient(String h, int p, boolean needlogin) {
        super(h, p, needlogin);

    }

       
  
  
  
  
  
  
    public void login(String phone, String password, String imei, TCPServiceClientV2.CommandListener listener)
            throws IOException {
                                                    
        Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x01;
        f.subCmd = 1;
        f.strData = phone + "\t" + password + "\t" + imei;
        this.sendToQueue(f, listener, 10);
    }

       
  
  
  
  
  
  
  
  
    public void regUser(String phone, String passwaord, String deviceCode, String authCode, String address, String nickname,
            TCPServiceClientV2.CommandListener listener) throws IOException {
        Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x01;
        f.version = VERSION_1;
        f.subCmd = 3;
        f.strData = phone + "\t" + passwaord + "\t" + deviceCode + "\t" + authCode+"\t"+address+"\t"+nickname;
        this.sendToQueue(f, listener);
    }

       
  
  
  
  
  
    public void bindUser(String phone, String deviceCode, String authCode, String address, TCPServiceClientV2.CommandListener l) {
        Frame f = new Frame();
        f.platform = 0x04;
        f.mainCmd = 0x01;
        f.version = 0x01;
        f.subCmd = 2;
        f.strData = phone+"\t"+deviceCode+"\t"+authCode+"\t"+address;
        this.sendToQueue(f, l);
    }

                
                          
                                 
                                       
           
                             
  
                                      
                                                 
               
                                  
  
                                  
                                         
                                  
                                  
                                                    
                                                    
                                                           
                          
                                   
                   
  
                                          
                                   
                                                     
                                                     
                   
  
               
                                   
                                  
                                     
                                                
               
                              
           
  
                              
                                     
                                         
               
                              
           
                                 
                                      
           
                            
                          
                  
       

    public Frame createLoginFrame(String user, String pwd) {
        Frame f = new Frame();
        f.platform = 4;
        f.mainCmd = 0x01;
        f.subCmd = 0x01;
        f.strData = user + "\t" + pwd;
        return f;
    }

    public Command createLogin(String user, String pwd) {
        Frame f = createLoginFrame(user, pwd);
        return new Command(f, null, 1);
    }

}
