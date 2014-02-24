package com.zgan.wifi;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiAdmin {
	// 定义WifiManager对象
	private WifiManager mWifiManager;
	// 定义WifiInfo对象
	private WifiInfo mWifiInfo;
	// 扫描出的网络连接列表,ScanResult主要用来描述已经检测出的接入点，包括介入点的地址，介入点的名称，身份认证，频率，信号强度等信息
	private List<ScanResult> mWifiList;
	// 网络连接列表
	private List<WifiConfiguration> mWifiConfiguration;
	// 定义一个WifiLock
	WifiLock mWifiLock;
	
	private Context context;

	// 构造器
	public WifiAdmin(Context context) {
		// 取得WifiManager对象
		this.context=context;
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);				
	}

	// 打开WIFI
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
			System.out.println("wifi打开成功!");
		}
		// if (mWifiManager.disconnect()) {
		// mWifiManager.setWifiEnabled(true);
		// System.out.println("wifi打开成功!!");
		// }
	}

	// 关闭WIFI
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// 检查当前WIFI状态
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	// 锁定WifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// 解锁WifiLock
	public void releaseWifiLock() {
		// 判断时候锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// 创建一个WifiLock
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// 得到配置好的网络
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	// 指定配置好的网络进行连接
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回
		if (index > mWifiConfiguration.size()) {
			System.out.println("连接失败!");
			return;
		}
		// 连接配置好的指定ID的网络
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
		System.out.println(index + "连接成功!");
	}

	public void startScan() {
		mWifiManager.startScan();
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	// 得到网络列表
	public List<ScanResult> getWifiList() {
	    return mWifiList;
	}
	
	//查看扫描结果
    public StringBuilder LookUpScan()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++)
        {
            stringBuilder.append("Index_"+new Integer(i + 1).toString() + ":");
            //将ScanResult信息转换成一个字符串包
            //其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }
    
    //得到MAC地址
    public String GetMacAddress()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }
    
    //得到接入点的BSSID
    public String GetBSSID()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }
    
    //得到IP地址
    public int GetIPAddress()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }
    
    //得到连接的ID
    public int GetNetworkId()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }
    
    //得到WifiInfo的所有信息包
    public WifiInfo GetWifiInfo()
    {
    	// 取得WifiInfo对象
    	return mWifiManager.getConnectionInfo();
        //return (WifiInfo) ((mWifiInfo == null) ? "NULL" : mWifiInfo);
    }
    
    //添加一个网络并连接
    public boolean AddNetwork(WifiConfiguration wcg)
    {
        int wcgID = mWifiManager.addNetwork(wcg);
        return mWifiManager.enableNetwork(wcgID, true);
    }
    
    //断开指定ID的网络
    public void DisconnectWifi(int netId)
    {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    // 1没有密码2用wep加密3用wpa加密
    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)  
    {  
          WifiConfiguration config = new WifiConfiguration();    
          config.allowedAuthAlgorithms.clear();  
          config.allowedGroupCiphers.clear();  
          config.allowedKeyManagement.clear();  
          config.allowedPairwiseCiphers.clear();  
          config.allowedProtocols.clear();  
          config.SSID = "\"" + SSID + "\"";    
           
          WifiConfiguration tempConfig = this.IsExsits(SSID);            
          if(tempConfig != null) {   
              mWifiManager.removeNetwork(tempConfig.networkId);   
          } 
          
          if(Type == 1) //WIFICIPHER_NOPASS 
          {                  
               config.hiddenSSID = true;  
//             config.wepKeys[0] = "";
               config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//             config.wepTxKeyIndex = 0;
          }  
          if(Type == 2) //WIFICIPHER_WEP 
          {  
              config.hiddenSSID = true; 
              config.wepKeys[0]= "\""+Password+"\"";  
              config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
              config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
              config.wepTxKeyIndex = 0;  
          }  
          if(Type == 3) //WIFICIPHER_WPA 
          {  
          config.preSharedKey = "\""+Password+"\"";  
          config.hiddenSSID = true;    
          config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);    
          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);                          
          config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                          
          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                     
          //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);   
          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP); 
          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP); 
          config.status = WifiConfiguration.Status.ENABLED;    
          } 
           return config;  
    }  
     
    public WifiConfiguration IsExsits(String SSID)   
    {   
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();   
           for (WifiConfiguration existingConfig : existingConfigs)    
           {   
             if (existingConfig.SSID.equals("\""+SSID+"\""))   
             {   
                 return existingConfig;   
             }   
           }   
        return null;    
    }  
    
    public boolean isWifiConnect() {   
    	ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
    	return mWifi.isConnected();  
    }
    
    public boolean IsWifiEnabled()
    {
    	return mWifiManager.isWifiEnabled();
    }

	public WifiManager getmWifiManager() {
		return mWifiManager;
	}

	public void setmWifiManager(WifiManager mWifiManager) {
		this.mWifiManager = mWifiManager;
	}
    
}
