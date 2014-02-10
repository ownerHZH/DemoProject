package com.hiibox.houseshelter.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hiibox.houseshelter.R;

public class CaptureView extends View {

	           
	public static final int GROW_NONE = (1 << 0);       
	public static final int GROW_LEFT_EDGE = (1 << 1);        
	public static final int GROW_RIGHT_EDGE = (1 << 2);        
	public static final int GROW_TOP_EDGE = (1 << 3);        
	public static final int GROW_BOTTOM_EDGE = (1 << 4);        
	public static final int GROW_MOVE = (1 << 5);       

	private Paint outsideCapturePaint = new Paint();             
	private Paint lineCapturePaint = new Paint();         

	private Rect viewRect;         
	private Rect captureRect;         

	private int mMotionEdge;          
	private float mLastX, mLastY;            

	private Drawable horStretchArrows;           
	private Drawable verStretchArrows;           
	private int horStretchArrowsHalfWidth;             
	private int horStretchArrowsHalfHeigth;            
	private int verStretchArrowsHalfWidth;            
	private int verStretchArrowsHalfHeigth;            

	private CaptureView mCaptureView;

	private enum ActionMode {                   
		None, Move, Grow
	}

	private ActionMode mMode = ActionMode.None;

	public CaptureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		lineCapturePaint.setStrokeWidth(3F);              
		lineCapturePaint.setStyle(Paint.Style.STROKE);           
		lineCapturePaint.setAntiAlias(true);        
		lineCapturePaint.setColor(Color.BLACK);         
		Resources resources = context.getResources();
		horStretchArrows = resources.getDrawable(R.drawable.hor_stretch_arrows);
		verStretchArrows = resources.getDrawable(R.drawable.ver_stretch_arrows);

		horStretchArrowsHalfWidth = horStretchArrows.getIntrinsicWidth() / 2;
		horStretchArrowsHalfHeigth = horStretchArrows.getIntrinsicHeight() / 2;
		verStretchArrowsHalfWidth = verStretchArrows.getIntrinsicWidth() / 2;
		verStretchArrowsHalfHeigth = verStretchArrows.getIntrinsicHeight() / 2;
		setFullScreen(true);            
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		                
		viewRect = new Rect(left, top, right, bottom);
		int viewWidth = right - left;
		int viewHeight = bottom - top;
		int captureWidth = Math.min(viewWidth, viewHeight) * 3 / 5;
		int captureHeight = viewHeight * 2 / 5;
		                  
		int captureX = (viewWidth - captureWidth) / 2;
		int captureY = (viewHeight - captureHeight) / 2;
		captureRect = new Rect(captureX, captureY, captureX + captureWidth,
				captureY + captureHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		                                   
		super.onDraw(canvas);
		canvas.save();
		Path path = new Path();
		path.addRect(new RectF(captureRect), Path.Direction.CW);           
		canvas.clipPath(path, Region.Op.DIFFERENCE);
		canvas.drawRect(viewRect, outsideCapturePaint);             

		canvas.drawPath(path, lineCapturePaint);         
		canvas.restore();
		if (mMode == ActionMode.Grow) {                 

			int xMiddle = captureRect.left + captureRect.width() / 2;            
			int yMiddle = captureRect.top + captureRect.height() / 2;            

			           
			horStretchArrows.setBounds(captureRect.left
					- horStretchArrowsHalfWidth, yMiddle
					- horStretchArrowsHalfHeigth, captureRect.left
					+ horStretchArrowsHalfWidth, yMiddle
					+ horStretchArrowsHalfHeigth);
			horStretchArrows.draw(canvas);

			           
			horStretchArrows.setBounds(captureRect.right
					- horStretchArrowsHalfWidth, yMiddle
					- horStretchArrowsHalfHeigth, captureRect.right
					+ horStretchArrowsHalfWidth, yMiddle
					+ horStretchArrowsHalfHeigth);
			horStretchArrows.draw(canvas);

			           
			verStretchArrows.setBounds(xMiddle - verStretchArrowsHalfWidth,
					captureRect.top - verStretchArrowsHalfHeigth, xMiddle
							+ verStretchArrowsHalfWidth, captureRect.top
							+ verStretchArrowsHalfHeigth);
			verStretchArrows.draw(canvas);

			           
			verStretchArrows.setBounds(xMiddle - verStretchArrowsHalfWidth,
					captureRect.bottom - verStretchArrowsHalfHeigth, xMiddle
							+ verStretchArrowsHalfWidth, captureRect.bottom
							+ verStretchArrowsHalfHeigth);
			verStretchArrows.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int grow = getGrow(event.getX(), event.getY());
			if (grow != GROW_NONE) {
				                                                  
				mCaptureView = CaptureView.this;
				mMotionEdge = grow;
				mLastX = event.getX();
				mLastY = event.getY();
				mCaptureView.setMode((grow == GROW_MOVE) ? ActionMode.Move
						: ActionMode.Grow);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mCaptureView != null) {
				setMode(ActionMode.None);
				mCaptureView = null;                
			}

			break;
		case MotionEvent.ACTION_MOVE:         
			if (mCaptureView != null) {
				handleMotion(mMotionEdge, event.getX() - mLastX, event.getY()
						- mLastY);
				mLastX = event.getX();
				mLastY = event.getY();
			}
			break;
		}
		return true;
	}

	public void setFullScreen(boolean full) {
		if (full) {                    
			outsideCapturePaint.setARGB(100, 50, 50, 50);
		} else {                    
			outsideCapturePaint.setARGB(255, 0, 0, 0);
		}
	}

	private void setMode(ActionMode mode) {
		if (mode != mMode) {
			mMode = mode;
			invalidate();
		}
	}

	                                          
	private int getGrow(float x, float y) {
		final float effectiveRange = 20F;              
		int grow = GROW_NONE;
		int left = captureRect.left;
		int top = captureRect.top;
		int right = captureRect.right;
		int bottom = captureRect.bottom;
		boolean verticalCheck = (y >= top - effectiveRange)
				&& (y < bottom + effectiveRange);
		boolean horizCheck = (x >= left - effectiveRange)
				&& (x < right + effectiveRange);

		            
		if ((Math.abs(left - x) < effectiveRange) && verticalCheck) {
			grow |= GROW_LEFT_EDGE;
		}

		            
		if ((Math.abs(right - x) < effectiveRange) && verticalCheck) {
			grow |= GROW_RIGHT_EDGE;
		}

		            
		if ((Math.abs(top - y) < effectiveRange) && horizCheck) {
			grow |= GROW_TOP_EDGE;
		}

		            
		if ((Math.abs(bottom - y) < effectiveRange) && horizCheck) {
			grow |= GROW_BOTTOM_EDGE;
		}

		          
		if (grow == GROW_NONE && captureRect.contains((int) x, (int) y)) {
			grow = GROW_MOVE;
		}
		return grow;
	}

	                       
	private void handleMotion(int grow, float dx, float dy) {
		if (grow == GROW_NONE) {
			return;
		} else if (grow == GROW_MOVE) {
			moveBy(dx, dy);         
		} else {
			if (((GROW_LEFT_EDGE | GROW_RIGHT_EDGE) & grow) == 0) {
				dx = 0;          
			}

			if (((GROW_TOP_EDGE | GROW_BOTTOM_EDGE) & grow) == 0) {
				dy = 0;          
			}
			growBy((((grow & GROW_LEFT_EDGE) != 0) ? -1 : 1) * dx,
					(((grow & GROW_TOP_EDGE) != 0) ? -1 : 1) * dy);
		}
	}

	private void moveBy(float dx, float dy) {
		Rect invalRect = new Rect(captureRect);
		captureRect.offset((int) dx, (int) dy);
		captureRect.offset(Math.max(0, viewRect.left - captureRect.left),
				Math.max(0, viewRect.top - captureRect.top));
		captureRect.offset(Math.min(0, viewRect.right - captureRect.right),
				Math.min(0, viewRect.bottom - captureRect.bottom));

		            
		invalRect.union(captureRect);                  
		invalRect.inset(-100, -100);
		invalidate(invalRect);           
	}

	private void growBy(float dx, float dy) {
		float widthCap = 50F;		                  
		float heightCap = 50F;                        
		
		RectF r = new RectF(captureRect);
		
		                                              
		if (dx > 0F && r.width() + 2 * dx >= viewRect.width()) {
			dx = 0F;
		}
		     
		if (dy > 0F && r.height() + 2 * dy >= viewRect.height()) {
			dy = 0F;
		}

		r.inset(-dx, -dy);           

		
		                                
		if (r.width() <= widthCap) {
			r.inset(-(widthCap - r.width()) / 2F, 0F);
		}

		     
		if (r.height() <= heightCap) {
			r.inset(0F, -(heightCap - r.height()) / 2F);
		}

		if (r.left < viewRect.left) {
			r.offset(viewRect.left - r.left, 0F);
		} else if (r.right > viewRect.right) {
			r.offset(-(r.right - viewRect.right), 0);
		}
		if (r.top < viewRect.top) {
			r.offset(0F, viewRect.top - r.top);
		} else if (r.bottom > viewRect.bottom) {
			r.offset(0F, -(r.bottom - viewRect.bottom));
		}

		captureRect.set((int) r.left, (int) r.top, (int) r.right,
				(int) r.bottom);
		invalidate();
	}

	public Rect getCaptureRect() {
		return captureRect;
	}
}
