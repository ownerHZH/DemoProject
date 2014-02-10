package com.hiibox.houseshelter.core;

import java.util.Stack;

import android.app.Activity;
    
  
  
  
  
public class MianActivity {
	private static Stack<Activity> activityStack;
	private static MianActivity instance;

	private MianActivity() {}

	public static MianActivity getScreenManager() {
		if (instance == null) {
			instance = new MianActivity();
		}
		return instance;
	}

	                
	public void exitActivity(Activity activity) {
		if (activity != null) {
			                                           
			activity.finish();
			                                                                                 
			activityStack.remove(activity);
			activity = null;
		}
	}

	                  
	public Activity currentActivity() {
		Activity activity = null;
		if (!activityStack.empty())
			activity = activityStack.lastElement();
		return activity;
	}

	                   
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	                  
	public void exitAllActivityExceptOne( ) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			  


  
			exitActivity(activity);
		}
	}
}