package com.hiibox.houseshelter;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.activity.CloudEyesActivity;
import com.hiibox.houseshelter.activity.CloudPhotoAlbumActivity;
import com.hiibox.houseshelter.activity.ImprintingActivity;
import com.hiibox.houseshelter.activity.SelfCenterActivity;
import com.hiibox.houseshelter.activity.SmartAppActivity;
import com.hiibox.houseshelter.view.MyLinearLayout;
import com.hiibox.houseshelter.view.MyLinearLayout.OnScrollListener;

public class SmothActivity extends ActivityGroup
        implements OnClickListener, OnScrollListener,
            OnTouchListener, OnGestureListener {

    public static final int SNAP_VELOCITY = 400;
    public static boolean isMenuOpen = false;
    public static boolean isFinish = true;
    private boolean hasMeasured = false;
    private GestureDetector mGestureDetector;
    public static boolean isScrolling = false;
    private float mScrollX; 
    private int window_width;
    private Intent[] intentView;
    private View activityView;
    private int MAX_WIDTH = 0;
    private final static int SPEED = 20;
    public static int activityFlag = 0;
    
    private LinearLayout behindLayout;
    private MyLinearLayout contentLayout;
    private TextView cloudEyesTV, imprintingTV, cloudPhotoAlbumTV, smartAppTV, selfCenterTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFlag = getIntent().getIntExtra("activityFlag", 0);
        setContentView(R.layout.activity_sliding_menu_layout);
        initViews();
    }

    private void initViews() {
        intentView = new Intent[] {new Intent(SmothActivity.this, CloudEyesActivity.class),
                        new Intent(SmothActivity.this, ImprintingActivity.class),
                        new Intent(SmothActivity.this, CloudPhotoAlbumActivity.class),
                        new Intent(SmothActivity.this, SmartAppActivity.class),
                        new Intent(SmothActivity.this, SelfCenterActivity.class)};
        intentView[0].putExtra("enter", "1");
        
        behindLayout = (LinearLayout) findViewById(R.id.behind_layout);
        contentLayout = (MyLinearLayout) findViewById(R.id.content_layout);
        cloudEyesTV = (TextView) findViewById(R.id.cloud_eyes_tv);
        imprintingTV = (TextView) findViewById(R.id.imprinting_tv);
        cloudPhotoAlbumTV = (TextView) findViewById(R.id.cloud_photo_album_tv);
        smartAppTV = (TextView) findViewById(R.id.smart_app_tv);
        selfCenterTV = (TextView) findViewById(R.id.self_center_tv);
        
        cloudEyesTV.setOnClickListener(this);
        imprintingTV.setOnClickListener(this);
        cloudPhotoAlbumTV.setOnClickListener(this);
        smartAppTV.setOnClickListener(this);
        selfCenterTV.setOnClickListener(this);
        
        switch (activityFlag) {
            case 0:
                activityView = getLocalActivityManager().startActivity("cloudEyesActivity", intentView[0]).getDecorView();
                break;
            case 1:
                activityView = getLocalActivityManager().startActivity("imprintingActivity", intentView[1]).getDecorView();
                break;
            case 2:
                activityView = getLocalActivityManager().startActivity("cloudPhotoAlbumActivity", intentView[2]).getDecorView();
                break;
            case 3:
                activityView = getLocalActivityManager().startActivity("smartAppActivity", intentView[3]).getDecorView();
                break;
            case 4:
                activityView = getLocalActivityManager().startActivity("selfCenterActivity", intentView[4]).getDecorView();
                break;
            default:
                break;
        }
        contentLayout.removeAllViews();
        contentLayout.addView(activityView, 0);
        
        contentLayout.setOnScrollListener(this);
        contentLayout.setOnTouchListener(this);
        
        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setIsLongpressEnabled(false);          
        getMAX_WIDTH();
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.cloud_eyes_tv:
                activityFlag = 0;
                activityView = getLocalActivityManager().startActivity("gestureSettingActivity", intentView[0]).getDecorView();
                break;
            case R.id.imprinting_tv:
                activityFlag = 1;
                activityView = getLocalActivityManager().startActivity("imprintingActivity", intentView[1]).getDecorView();
                break;
            case R.id.cloud_photo_album_tv:
                activityFlag = 2;
                activityView = getLocalActivityManager().startActivity("cloudPhotoAlbumActivity", intentView[2]).getDecorView();
                break;
            case R.id.smart_app_tv:
                activityFlag = 3;
                activityView = getLocalActivityManager().startActivity("smartAppActivity", intentView[3]).getDecorView();
                break;
            case R.id.self_center_tv:
                activityFlag = 4;
                activityView = getLocalActivityManager().startActivity("selfCenterActivity", intentView[4]).getDecorView();
                break;
            default:
                break;
        }
        contentLayout.removeAllViews();
        contentLayout.addView(activityView, 0);
        contentLayout.bringChildToFront(activityView);
        new AsynMove().execute(-SPEED);
    }
    
    @Override
    public void doScroll(float distanceX) {
        if (isFinish) {
            doScrolling(distanceX);
        }
    }

    @Override
    public void doLoosen(boolean suduEnough) {
        doCloseScroll(suduEnough);
    }
    
    public void doScrolling(float distanceX) {
        isScrolling = true;
        mScrollX += distanceX;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
                .getLayoutParams();
        layoutParams.leftMargin -= mScrollX;
        layoutParams.rightMargin += mScrollX;
        Log.d("scrolling...leftmargin", layoutParams.leftMargin + "");
        if (layoutParams.leftMargin <= 0) {
            isScrolling = false;
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
        } else if (layoutParams.leftMargin >= MAX_WIDTH) {
            isScrolling = false;
            layoutParams.leftMargin = MAX_WIDTH;
        }
        contentLayout.setLayoutParams(layoutParams);
        behindLayout.invalidate();
    }

    public void doCloseScroll(boolean suduEnough) {
        if (isFinish) {
            Log.d("doCloseScroll...", "doCloseScroll");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
                    .getLayoutParams();
            int tempSpeed = SPEED;
            if (isMenuOpen) {
                tempSpeed = -tempSpeed;
            }
            if (suduEnough
                    || (!isMenuOpen && (layoutParams.leftMargin > window_width / 2))
                    || (isMenuOpen && (layoutParams.leftMargin < window_width / 2))) {
                new AsynMove().execute(tempSpeed);
            } else {
                new AsynMove().execute(-tempSpeed);
            }
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
    
                
                                                        
                                                                                             
                                                                                                          
                                                                                                                                 
                               
                
            
                                                
        
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	Log.e("SlidingActivity", "onKeyDown()   keyCode = "+keyCode+" ; activityFlag = "+activityFlag);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                                                                                                                                 
                               
                
            new AsynMove().execute(-SPEED);
        }
        return super.onKeyDown(keyCode, event);
    }
    
         
  
    private void getMAX_WIDTH() {
        ViewTreeObserver viewTreeObserver = contentLayout.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    window_width = getWindowManager().getDefaultDisplay()
                            .getWidth();
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
                            .getLayoutParams();
                    layoutParams.width = window_width;
                    contentLayout.setLayoutParams(layoutParams);
                    MAX_WIDTH = behindLayout.getWidth();
                    Log.i("NewSlidingMenuActivity", "getMAX_WIDTH()   MAX_WIDTH = " + MAX_WIDTH);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }
    
    private boolean isInScope(float y) {
        return (y > 50f && y < 400f) ? true : false;
    }
    
    @Override
    public boolean onDown(MotionEvent e) {
        mScrollX = 0;
        isScrolling = false;
        if (isInScope(e.getY()) && activityFlag == 3) {
            Log.e("SlidingMenuActivity", "onDown()  activityFlag == 3");
            return false;
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                                        
                                      
                                                                                            
                                                                
                                                                               
                           
            
        int currentX = (int) e2.getX();
        int lastX = (int) e1.getX();
        if (isMenuOpen) {
            if (!isScrolling && currentX - lastX >= 0) {
                return false;
            }
        } else {
            if (!isScrolling && currentX - lastX <= 0) {
                return false;
            }
        }
        boolean suduEnough = false;
        if (velocityX > SNAP_VELOCITY || velocityX < -SNAP_VELOCITY) {
            suduEnough = true;
        } else {
            suduEnough = false;
        }
        doCloseScroll(suduEnough);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

        
  
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isFinish) {
            doScrolling(distanceX);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

        
  
  
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (isFinish) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
                    .getLayoutParams();
            if (layoutParams.leftMargin >= MAX_WIDTH) {        
                new AsynMove().execute(-SPEED);
            } else {        
                new AsynMove().execute(SPEED);
            }
        }
        return true;
    }
    
    class AsynMove extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            isFinish = false;
            int times = 0;
            if (MAX_WIDTH % Math.abs(params[0]) == 0) {      
                times = MAX_WIDTH / Math.abs(params[0]);
            } else {
                times = MAX_WIDTH / Math.abs(params[0]) + 1;       
            }
            for (int i = 0; i < times; i++) {
                publishProgress(params[0]);
                try {
                    Thread.sleep(Math.abs(params[0]));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            isFinish = true;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
                    .getLayoutParams();
            if (layoutParams.leftMargin >= MAX_WIDTH) {
                isMenuOpen = true;
            } else {
                isMenuOpen = false;
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
                    .getLayoutParams();
                   
            if (values[0] > 0) {
                layoutParams.leftMargin = Math.min(layoutParams.leftMargin
                        + values[0], MAX_WIDTH);
                layoutParams.rightMargin = Math.max(layoutParams.rightMargin
                        - values[0], -MAX_WIDTH);
            } else {
                       
                layoutParams.leftMargin = Math.max(layoutParams.leftMargin
                        + values[0], 0);
            }
            contentLayout.setLayoutParams(layoutParams);
            behindLayout.invalidate();
        }
    }

}
