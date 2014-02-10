package com.hiibox.houseshelter.net;

public class Frame {

       
  
  
    public int platform = 0;

       
  
  
    public int version = 1;

       
  
  
  
  
  
  
  
    public byte mainCmd = 0;

       
  
  
    public int subCmd = 0;

       
  
  
    public String strData = "";

       
  
  
    public byte[] aryData = null;

    public Frame() {}

    public Frame(byte[] Buff) {
        FrameTools.getByteToFrame(Buff, this);
    }
}
