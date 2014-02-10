package com.hiibox.houseshelter.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;


public class TCPManager {

    private final static String LOGIN_HOST = "61.186.245.252";
    private final static int LOGIN_PORT = 21000;
    public final TCPLoginClient loginClient = new TCPLoginClient(LOGIN_HOST, LOGIN_PORT, false);

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, HostInfo> allHosts = new HashMap<Integer, HostInfo>();



    public void login(String user, String pass, String imei, final Handler handler)
            throws IOException {
        if (!loginClient.isConnected()) {
            loginClient.connect(user, pass);
        }
        Log.d("============",
                "===========  loginClient.isConnected() = " + loginClient.isConnected());
        loginClient.login(user, pass, imei, new TCPServiceClientV2.CommandListener() {
            @Override
            public int onReceive(Frame src, Frame f2) {


                Log.e("TCPManager", "[��¼] onReceive()  f2.str = " + f2.strData);
                if (f2 != null) {



                    Frame f;
                    try {
                        f = loginClient.recvFrame(20);
                        if (f != null) {
                            byte[] datas = f.aryData;
                            if (null != datas) {
                                parserServerInfo(datas);
                            }
                            handler.sendEmptyMessage(2);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = SpliteUtil.getResult(f2.strData);
                            handler.sendMessage(msg);

                        }
                    } catch (IOException e) {
                        handler.sendEmptyMessage(4);
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(5);
                }
                loginClient.stop();
                return 0;
            }

            @Override
            public void onTimeout(Frame src, Frame f) {
                handler.sendEmptyMessage(6);
                loginClient.stop();
            }

        });
    }



    public void login1(String user, String pass, String imei, final Handler handler)
            throws IOException {
        loginClient.connect();
        if (!loginClient.isConnected()) {
            loginClient.connect(user, pass);
        }
        Log.d("============",
                "===========  loginClient.isConnected() = " + loginClient.isConnected());
        loginClient.login(user, pass, imei, new TCPServiceClientV2.CommandListener() {
            @Override
            public int onReceive(Frame src, Frame f2) {


                Log.e("TCPManager", "[��¼] onReceive()  f2.str = " + f2.strData);
                if (f2 != null) {



                    Frame f;
                    try {
                        f = loginClient.recvFrame(20);
                        if (f != null) {
                            byte[] datas = f.aryData;
                            if (null != datas) {
                                parserServerInfo(datas);
                            }
                            handler.sendEmptyMessage(2);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = SpliteUtil.getResult(f2.strData);
                            handler.sendMessage(msg);

                        }
                    } catch (IOException e) {
                        handler.sendEmptyMessage(4);
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(5);
                }
                loginClient.stop();
                return 0;
            }

            @Override
            public void onTimeout(Frame src, Frame f) {
                handler.sendEmptyMessage(6);
                loginClient.stop();
            }

        });
    }



    public void register(String phone, String password, String deviceCode, String authCode,
            String address, String nickname, final Handler handler) throws IOException {
        loginClient.connect();
        if (!loginClient.isConnected()) {
            loginClient.connect();


        }
        loginClient.regUser(phone, password, deviceCode, authCode, address, nickname,
                new CommandListener() {
                    @Override
                    public void onTimeout(Frame src, Frame f) {
                        handler.sendEmptyMessage(3);
                        Log.e("TCPManager", "[�û�ע��] onTimeout()");
                    }

                    @Override
                    public int onReceive(Frame src, Frame f) {
                        if (null != f) {
                            String status = f.strData;
                            Log.e("TCPManager", "[�û�ע��] onReceive()  f.strData = " + status);
                            byte[] b;
                            try {
                                b = status.getBytes("UTF-8");
                                String s = new String(b);
                                Log.i("TCPManager", "onReceive()  utf-8 s = " + s);
                                if (status.equals("0")) {
                                    handler.sendEmptyMessage(0);
                                } else {
                                    String[] str = status.split("\t");
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = str[1];
                                    handler.sendMessage(msg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                        return 0;
                    }
                });
    }



    public void bindTerminal(String phone, String deviceCode, String authCode, String address,
            CommandListener listener) {
        loginClient.connect();
        if (!loginClient.isConnected()) {
            loginClient.connect(phone);

        }
        Log.i("TCPManager", "bindTerminal()  onReceive()  loginClient.connected = "
                + loginClient.connected);
        loginClient.bindUser(phone, deviceCode, authCode, address, listener);
    }



    public TCPMainClient getMainClient(String phone, String pass, String tcode, String tpass) {
        HostInfo info = allHosts.get(6);
        if (info == null) {

            return null;
        }
        try {
            TCPMainClient mainClient = null;
            InetAddress addr = java.net.Inet4Address.getByAddress(info.ip);
            mainClient = new TCPMainClient(addr, info.port, tcode, tpass);
            mainClient.connect(phone, pass);
            return mainClient;
        } catch (UnknownHostException ex) {
            Log.i("TCPManager", "getMainClient()   UnknownHostException  ex = " + ex.toString());
            return null;
        }
    }



    public TCPMsgClient getMsgClient(String user, String psw, CommandListener commandListener) {
        HostInfo info = allHosts.get(7);
        if (info == null) {

            return null;
        }
        try {
            TCPMsgClient msgClient = null;
            InetAddress addr = java.net.Inet4Address.getByAddress(info.ip);
            msgClient = new TCPMsgClient(addr, info.port, commandListener);
            msgClient.connect(user, psw);
            return msgClient;
        } catch (UnknownHostException ex) {
            return null;
        }
    }



    public TCPFileClient getFileClient(String phone) {
        HostInfo info = allHosts.get(8);
        if (info == null) {

            return null;
        }
        TCPFileClient fileClient = null;
        try {
            InetAddress addr = java.net.Inet4Address.getByAddress(info.ip);
            fileClient = new TCPFileClient(addr, info.port);
            fileClient.connect(phone);
            return fileClient;
        } catch (UnknownHostException ex) {
            return null;
        }
    }

    static void logln(String str) {
        System.out.println(str);
    }

    static void log(String str) {
        System.out.print(str);
    }

    static void output(ArrayList<byte[]> datas) {
        System.out.println(datas.size());
        for (byte[] b : datas) {
            System.out.println(new String(b));
        }
        System.out.println("-----");
    }

    private void parserServerInfo(byte[] datas) {
        ArrayList<byte[]> all = FrameTools.split(datas, '\t');
        if (all.size() > 2) {
            int tc = Integer.parseInt(FrameTools.decodeFrameData(all.get(0)));
            ArrayList<byte[]> temp = null;
            for (int i = 0; i < tc; i++) {
                temp = FrameTools.split(all.get(i + 1), ':');
                if (temp.size() == 3) {
                    HostInfo info = new HostInfo();
                    info.ip = new byte[4];
                    int ivalue = FrameTools.parseInt(temp.get(0));
                    info.ip[3] = (byte) (0xff & (ivalue >>> 24));
                    info.ip[2] = (byte) (0xff & (ivalue >>> 16));
                    info.ip[1] = (byte) (0xff & (ivalue >>> 8));
                    info.ip[0] = (byte) (0xff & (ivalue));
                    Log.i("====", "=== ip = " + info.ip + " ; len = " + info.ip.length
                            + " ; temp.get(0) = " + temp.get(0));
                    info.port = Integer.parseInt(FrameTools.decodeFrameData(temp.get(1)));
                    info.platform = Integer.parseInt(FrameTools.decodeFrameData(temp.get(2)));
                    allHosts.put(info.platform, info);
                    Log.i("TCPManager", "parserServerInfo()  ip = " + info.ip + " ; port = "
                            + info.port + " ; platform = " + info.platform);
                } else {
                    logln("��ַ��Ϣ��ʽ����" + FrameTools.decodeFrameData(all.get(i + 1)));
                    FrameTools.hexoutput(all.get(i + 1));
                    logln("");
                }
            }
        }
    }

    public static class HostInfo {
        public byte[] ip;
        public int port;
        public int platform;
    }



}
