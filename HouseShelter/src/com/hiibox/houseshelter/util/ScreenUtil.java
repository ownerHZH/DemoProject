package com.hiibox.houseshelter.util;

import com.hiibox.houseshelter.core.GlobalUtil;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;

    
  
  
  
  
  
public class ScreenUtil {

	public static int screenHeight = 0;
	public static int screenWidth = 0;
	public static float screenDensity = 0;

	    
  
  
  
  
  

	public static int getScreenHeight(Activity context) {
		if (screenWidth == 0 || screenHeight == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			ScreenUtil.screenDensity = dm.density;
			ScreenUtil.screenHeight = dm.heightPixels;
			ScreenUtil.screenWidth = dm.widthPixels;
		}
		return screenHeight;
	}

	    
  
  
  
  
  
	public static int getScreenWidth(Activity activity) {
		if (screenWidth == 0 || screenHeight == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			ScreenUtil.screenDensity = dm.density;
			ScreenUtil.screenHeight = dm.heightPixels;
			ScreenUtil.screenWidth = dm.widthPixels;
		}
		return screenWidth;
	}

	    
  
  
  
  
  
	public static float getScreenHeightRatio(Activity context) {
		return (float) getScreenHeight(context) / GlobalUtil.mScreenHeight;
	}

	    
  
  
  
  
  
	public static float getScreenWidthRatio(Activity context) {
		return (float) getScreenWidth(context) / GlobalUtil.mScreenWidth;
	}

	    
  
  
  
  
  
	public static float getScreenDensity(Activity activity) {
		if (screenWidth == 0 || screenHeight == 0 || screenDensity == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			ScreenUtil.screenDensity = dm.density;
			ScreenUtil.screenHeight = dm.heightPixels;
			ScreenUtil.screenWidth = dm.widthPixels;
		}
		return screenDensity;
	}

	    
  
  
  
  
  
	public static double getScreenPhysicalSize(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2)
				+ Math.pow(dm.heightPixels, 2));
		return diagonalPixels / (160 * dm.density);
	}

	    
  
  
  
  
  
	public static int getScreenStatusBarHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}
}