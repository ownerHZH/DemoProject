package com.hiibox.houseshelter.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

import com.hiibox.houseshelter.core.GlobalUtil;

    
  
  
  
  
  
  
  
public class FileUtil {

    public static File updateDir = null;
    public static File updateFile = null;
    
        
  
  
  
    public static boolean isSdCardMounted() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

        
  
  
  
  
    public static void createFile(String name) {
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState())) {
            updateDir = new File(GlobalUtil.GLOBAL_PATH + "/apk/");
            updateFile = new File(updateDir + "/" + name + ".apk");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

        
  
  
  
  
    public static boolean delFolder(String folderPath) {
        boolean isDelete = false;
        try {
            if (delAllFile(folderPath)) {              
                String filePath = folderPath;
                filePath = filePath.toString();
                File myFilePath = new File(filePath);
                myFilePath.delete();           
                isDelete = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDelete;
    }

        
  
  
  
  
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);               
                delFolder(path + "/" + tempList[i]);           
                flag = true;
            }
        }
        return flag;
    }
    
    public static String DecToHex(int dec) {
    	String str = Integer.toHexString(dec);
    	  


  
    	return str;
    }
    
}
