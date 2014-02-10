package com.hiibox.houseshelter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;

import com.hiibox.houseshelter.SmothActivity;
import com.hiibox.houseshelter.core.GlobalUtil;

public class MyLinearLayout extends LinearLayout {
	
	private VelocityTracker velocityTracker;
	private int velocityX;
	private float lastX = 0, currentX = 0;
	private float lastY = 0, currentY = 0;
	
	private GestureDetector mGestureDetector;
	
	private boolean isLock = false;          
	private boolean isNeedOpen = false;          

	public OnScrollListener onScrollListener;           

	private boolean b;             

	public MyLinearLayout(Context context) {
		super(context);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new MySimpleGesture());
	}
	
	public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

	    
  
  
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		b = mGestureDetector.onTouchEvent(ev);            
		if(ev.getAction() == MotionEvent.ACTION_DOWN ){
			lastX = ev.getX();
			lastY = ev.getY();
			Log.d("MyLinearLayout", "dispatchTouchEvent()  action down : lastX = "+lastX+" ; lastY = "+lastY);
		}
		if(ev.getAction() == MotionEvent.ACTION_MOVE ){
			if (velocityTracker == null) {
				velocityTracker = VelocityTracker.obtain();
			}
			velocityTracker.addMovement(ev);
		}
		    
  
  
		if (ev.getAction() == MotionEvent.ACTION_UP && isNeedOpen) {
			currentX = ev.getX();
			currentY = ev.getY();
			Log.d("MyLinearLayout", "dispatchTouchEvent()  action up : currentX = "+currentX);
			Log.d("MyLinearLayout", "dispatchTouchEvent()  isMenuOpen = "+SmothActivity.isMenuOpen);
			if(SmothActivity.isMenuOpen){
				Log.d("MyLinearLayout", "dispatchTouchEvent()  currentX-lastX = "+(currentX-lastX)+"  ����������,С��������     isScrolling "+SmothActivity.isScrolling );
				if (SmothActivity.activityFlag == 3 && isInScope(lastY) && isInScope(currentY)) {
				    Log.e("MyLinearLayout", "dispatchTouchEvent()  activityFlag == 3"+"  return : "+super.dispatchTouchEvent(ev));
				    return super.dispatchTouchEvent(ev);
				}
				if(!SmothActivity.isScrolling && currentX-lastX>=0 ){
					return super.dispatchTouchEvent(ev);
				}
			}else {
			    Log.d("MyLinearLayout", "dispatchTouchEvent()  currentX-lastX = "+(currentX-lastX)+"  ����������,С��������     isScrolling "+SmothActivity.isScrolling );
			    if (SmothActivity.activityFlag == 3 && isInScope(lastY) && isInScope(currentY)) {
                    Log.e("MyLinearLayout", "dispatchTouchEvent()  activityFlag == 3"+"  return : "+super.dispatchTouchEvent(ev));
                    return super.dispatchTouchEvent(ev);
                }
			    if(!SmothActivity.isScrolling && currentX-lastX<=0){
					return super.dispatchTouchEvent(ev);
				}
			}
			boolean suduEnough = false;
			VelocityTracker tempVelocityTracker = velocityTracker;
			if(tempVelocityTracker==null){
				tempVelocityTracker=VelocityTracker.obtain();
			}
			tempVelocityTracker.computeCurrentVelocity(1000);
			velocityX = (int) tempVelocityTracker.getXVelocity();
			Log.d("MyLinearLayout", "dispatchTouchEvent()  velocityX=" + velocityX);
			if (velocityX > SmothActivity.SNAP_VELOCITY || velocityX < -SmothActivity.SNAP_VELOCITY) {
				suduEnough = true;
			} else {
				suduEnough = false;
			}
			if (velocityTracker != null) {
				velocityTracker.recycle();
				velocityTracker = null;
			}
			onScrollListener.doLoosen(suduEnough);
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private boolean isInScope(float y) {
	    if (GlobalUtil.mScreenHeight > 854 && GlobalUtil.mScreenHeight <= 1280) {
	        return (y > 50f && y < 550f) ? true : false;
	    }
	    return (y > 50f && y < 400f) ? true : false;
	}

	    
  
  
  
  
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return b;
	}

	    
  
  
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		isLock = false;
		return super.onTouchEvent(event);
	}

	    
  
  
	class MySimpleGesture extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			isLock = true;
			return super.onDown(e);
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.d("onScroll", distanceX+"");
			float y1 = e1.getY();
			float y2 = e2.getY();
			if (SmothActivity.activityFlag == 3 && isInScope(y1) && isInScope(y2)) {
                Log.e("MyLinearLayout", "onScroll()  activityFlag == 3");
                return false;
            }
			if (!isLock)
				onScrollListener.doScroll(distanceX);
			          
			if (Math.abs(distanceY) > Math.abs(distanceX) && !SmothActivity.isScrolling && !SmothActivity.isMenuOpen) {
				isNeedOpen = false;
				return false;
			} else {
				isNeedOpen = true;
				return true;
			}
		}
	}

	    
  
  
	public interface OnScrollListener {
		void doScroll(float distanceX);         
		void doLoosen(boolean suduEnough);              
	}

}
