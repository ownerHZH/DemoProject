package com.hiibox.houseshelter.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

import com.hiibox.houseshelter.R;

public class AdsDialog extends Dialog {

	private GestureDetector detector = null;            
	private ViewPager pager = null;
	private final int FLING_MIN_DISTANCE = 150, FLING_MIN_VELOCITY = 200;
	private View splashView = null;
	
	public AdsDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public AdsDialog(Context context, int theme) {
		super(context, theme);
	}

	public AdsDialog(Context context, ViewPager pager, View splashView) {
		super(context, R.style.splashDialogStyle);
		this.pager = pager;
		this.splashView = splashView;
		init(context, splashView);
	}
	
	public void notifySlideDown() {
		show();
		TranslateAnimation animation = new TranslateAnimation(0f, 0f, -splashView.getHeight(), 0f);
		animation.setDuration(2000);
		animation.setFillAfter(true);
		splashView.startAnimation(animation);
	}

	private void init(Context context, View splashView) {
		this.setContentView(splashView);
		this.setCancelable(false);
		detector = new GestureDetector(context, new OnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
						&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
					translateAnim();
				}
				return false;
			}
			@Override
			public void onLongPress(MotionEvent e) {
			}
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			@Override
			public void onShowPress(MotionEvent e) {
			}
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
		});
		this.pager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return detector.onTouchEvent(event);
			}
		});
	}
	
	private void translateAnim() {
		TranslateAnimation animation = new TranslateAnimation(0f, 0f, 0f, -splashView.getHeight());
		animation.setDuration(2000);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				dismiss();
			}
		});
		splashView.startAnimation(animation);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}

}
