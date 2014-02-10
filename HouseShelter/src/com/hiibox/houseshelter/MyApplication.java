package com.hiibox.houseshelter;

import java.io.IOException;

import android.app.Application;

import com.hiibox.houseshelter.net.TCPFileClient;
import com.hiibox.houseshelter.net.TCPMainClient;
import com.hiibox.houseshelter.net.TCPManager;
import com.hiibox.houseshelter.net.TCPMsgClient;
import com.hiibox.houseshelter.net.TCPServiceClientV2.ClientListener;

public class MyApplication extends Application {

                                                    
    public static boolean showedAds = false;                    
    public static String phone = null;                 
    public static String password = null;         
    public static int userLevel = -1;         
    public static TCPManager tcpManager = null;
    public static TCPMainClient mainClient = null;            
    public static TCPFileClient fileClient = null;          
    public static TCPMsgClient msgClient = null;          
    public static ClientListener listener = null;
                                                                          
    public static String deviceCode = null; 
                                                    
    public static boolean isFirstTimeEntry = true;

    @Override
    public void onCreate() {
        super.onCreate();
                                                                       
        tcpManager = new TCPManager();
                                                            
        listener = new ClientListener() {

            @Override
            public void onClientStop() {
                System.out.println("BaseApplication    Client Stop................");
            }

            @Override
            public void onClientStart() {
                System.out.println("BaseApplication    Client start................");
            }

            @Override
            public void onClientClose() {
                System.out.println("BaseApplication    Client Close................");
            }

            @Override
            public void onClientException(IOException ex) {
                ex.printStackTrace();
                System.out.println("BaseApplication    onClientException................"+ex.toString());
                                                                                             
            }

            @Override
            public void onLoginFail() {
                System.out.println("BaseApplication    Login Fail................");
            }

        };
    }
    
    public static void initTcpManager() {
        if (null != tcpManager) {
            return;
        }
        tcpManager = new TCPManager();
    }
    
}
