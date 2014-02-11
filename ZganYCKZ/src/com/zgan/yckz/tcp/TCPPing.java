package com.zgan.yckz.tcp;

import android.util.Log;

public class TCPPing implements Runnable {

	public TCPPing() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (FrameTools.Thread_Ping) {
			try {
				Thread.sleep(100);

				FrameTools.Thread_PingTime += 1;

				if (FrameTools.Thread_PingTime == (600)) {
					FrameTools.Thread_PingTime = 0;

					Frame f = new Frame();
					f.Platform = 4;
					f.MainCmd = FrameTools.Frame_MainCmd_Ping;

					FrameTools.toSendTcpData(f);
					
					Log.i("TCPPing", "·¢ËÍÐÄÌø......");
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
