package com.hiibox.houseshelter.net;

                          

public class TCPPing implements Runnable {
	FrameTools ft;
	public TCPPing(FrameTools ft) {
		new Thread(this).start();
	}

	@Override
	public void run() {
		                                  
		while (ft.Thread_Ping) {
			try {
				Thread.sleep(100);

				ft.Thread_PingTime += 1;

				if (ft.Thread_PingTime == (600)) {
					ft.Thread_PingTime = 0;

					Frame f = new Frame();
					f.platform = 4;
					f.mainCmd = FrameTools.Frame_MainCmd_Ping;

					                      
					
					                                     
				}

			} catch (InterruptedException e) {
				                                  
				e.printStackTrace();
			}
		}
	}

}
