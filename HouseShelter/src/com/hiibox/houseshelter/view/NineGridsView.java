package com.hiibox.houseshelter.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.entity.Point;
import com.hiibox.houseshelter.util.BitmapUtil;
import com.hiibox.houseshelter.util.MathUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.RoundUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class NineGridsView extends View {

    private float screenWidth = 0;
    private float screenHeight = 0;

    private boolean isCache = false;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Point[][] mPoints = new Point[3][3];
    private float r = 0;         
    private List<Point> sPoints = new ArrayList<Point>();         
    private boolean checking = false;
    private Bitmap locus_round_original;              
    private Bitmap locus_round_click;            
    private Bitmap locus_round_click_error;            
    private Bitmap locus_line;             
    private Bitmap locus_line_semicircle;
    private Bitmap locus_line_semicircle_error;
    private Bitmap locus_arrow;          
    private Bitmap locus_line_error;              
    private long CLEAR_TIME = 0;           
    private int passwordMinLength = 5;          
    private boolean isTouch = true;          
    private Matrix mMatrix = new Matrix();
    private int lineAlpha = 50;          

    private int trackTimes = 0;
    private String firstTrack = null;                  
    private String secondTrack = null;                  
    private OnCompleteListener mCompleteListener = null;
    private boolean isUnlock = false;                                  

    public NineGridsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NineGridsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NineGridsView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isCache) {
            initCache();
        }
        drawToCanvas(canvas);
    }

    private void drawToCanvas(Canvas canvas) {
                
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                Point p = mPoints[i][j];
                if (p.state == Point.STATE_CHECK) {
                    canvas.drawBitmap(locus_round_click, p.x - r, p.y - r, mPaint);
                } else if (p.state == Point.STATE_CHECK_ERROR) {
                    canvas.drawBitmap(locus_round_click_error, p.x - r, p.y - r, mPaint);
                } else {
                    canvas.drawBitmap(locus_round_original, p.x - r, p.y - r, mPaint);
                }
            }
        }
               
        if (sPoints.size() > 0) {
            int tmpAlpha = mPaint.getAlpha();
            mPaint.setAlpha(lineAlpha);
            Point tp = sPoints.get(0);
            for (int i = 1; i < sPoints.size(); i++) {
                Point p = sPoints.get(i);
                drawLine(canvas, tp, p);
                tp = p;
            }
            if (this.movingNoPoint) {
                drawLine(canvas, tp, new Point((int) moveingX, (int) moveingY));
            }
            mPaint.setAlpha(tmpAlpha);
            lineAlpha = mPaint.getAlpha();
        }
    }

        
  
  
    private void initCache() {
        screenWidth = this.getWidth();
        screenHeight = this.getHeight();
        float x = 0;
        float y = 0;

                  
        if (screenWidth > screenHeight) {       
            x = (screenWidth - screenHeight) / 2;
            screenWidth = screenHeight;
        }
        else {       
            y = (screenHeight - screenWidth) / 2;
            screenHeight = screenWidth;
        }

        locus_round_original =
                BitmapFactory.decodeResource(this.getResources(), R.drawable.cycle_normal_iv);
        locus_round_click =
                BitmapFactory.decodeResource(this.getResources(), R.drawable.cycle_pressed_iv);
        locus_round_click_error =
                BitmapFactory.decodeResource(this.getResources(), R.drawable.cycle_normal_iv);

        locus_line = BitmapFactory.decodeResource(this.getResources(), R.drawable.locus_line);
        locus_line_semicircle =
                BitmapFactory.decodeResource(this.getResources(), R.drawable.locus_line_semicircle);

        locus_line_error =
                BitmapFactory.decodeResource(this.getResources(), R.drawable.locus_line_error);
        locus_line_semicircle_error =
                BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.locus_line_semicircle_error);

        locus_arrow = BitmapFactory.decodeResource(this.getResources(), R.drawable.locus_arrow);
        Log.d("Canvas w h :", "w:" + screenWidth + " h:" + screenHeight);

                     
        float canvasMinW = screenWidth;
        if (screenWidth > screenHeight) {
            canvasMinW = screenHeight;
        }
        float roundMinW = canvasMinW / 8.0f * 2;
        float roundW = roundMinW / 2.f;
           
        float deviation = canvasMinW % (8 * 2) / 2;
        x += deviation;
        x += deviation;

        if (locus_round_original.getWidth() > roundMinW) {
            float sf = roundMinW * 1.0f / locus_round_original.getWidth();                      
            locus_round_original = BitmapUtil.zoom(locus_round_original, sf);
            locus_round_click = BitmapUtil.zoom(locus_round_click, sf);
            locus_round_click_error = BitmapUtil.zoom(locus_round_click_error, sf);

            locus_line = BitmapUtil.zoom(locus_line, sf);
            locus_line_semicircle = BitmapUtil.zoom(locus_line_semicircle, sf);

            locus_line_error = BitmapUtil.zoom(locus_line_error, sf);
            locus_line_semicircle_error = BitmapUtil.zoom(locus_line_semicircle_error, sf);
            locus_arrow = BitmapUtil.zoom(locus_arrow, sf);
            roundW = locus_round_original.getWidth() / 2;
        }

        mPoints[0][0] = new Point(x + 0 + roundW, y + 0 + roundW);
        mPoints[0][1] = new Point(x + screenWidth / 2, y + 0 + roundW);
        mPoints[0][2] = new Point(x + screenWidth - roundW, y + 0 + roundW);
        mPoints[1][0] = new Point(x + 0 + roundW, y + screenHeight / 2);
        mPoints[1][1] = new Point(x + screenWidth / 2, y + screenHeight / 2);
        mPoints[1][2] = new Point(x + screenWidth - roundW, y + screenHeight / 2);
        mPoints[2][0] = new Point(x + 0 + roundW, y + screenHeight - roundW);
        mPoints[2][1] = new Point(x + screenWidth / 2, y + screenHeight - roundW);
        mPoints[2][2] = new Point(x + screenWidth - roundW, y + screenHeight - roundW);
        int k = 0;
        for (Point[] ps : mPoints) {
            for (Point p : ps) {
                p.index = k;
                k++;
            }
        }
        r = locus_round_original.getHeight() / 2;           
        isCache = true;
    }

        
  
  
  
  
  
  
    private void drawLine(Canvas canvas, Point a, Point b) {
        float ah = (float) MathUtil.distance(a.x, a.y, b.x, b.y);
        float degrees = getDegrees(a, b);
        canvas.rotate(degrees, a.x, a.y);
        if (a.state == Point.STATE_CHECK_ERROR) {
            mMatrix.setScale(
                    (ah - locus_line_semicircle_error.getWidth()) / locus_line_error.getWidth(), 1);
            mMatrix.postTranslate(a.x, a.y - locus_line_error.getHeight() / 2.0f);
            canvas.drawBitmap(locus_line_error, mMatrix, mPaint);
            canvas.drawBitmap(locus_line_semicircle_error, a.x + locus_line_error.getWidth(), a.y
                    - locus_line_error.getHeight() / 2.0f, mPaint);
        } else {
            mMatrix.setScale((ah - locus_line_semicircle.getWidth()) / locus_line.getWidth(), 1);
            mMatrix.postTranslate(a.x, a.y - locus_line.getHeight() / 2.0f);
            canvas.drawBitmap(locus_line, mMatrix, mPaint);
            canvas.drawBitmap(locus_line_semicircle, a.x + ah - locus_line_semicircle.getWidth(),
                    a.y - locus_line.getHeight() / 2.0f, mPaint);
        }
        canvas.drawBitmap(locus_arrow, a.x, a.y - locus_arrow.getHeight() / 2.0f, mPaint);
        canvas.rotate(-degrees, a.x, a.y);
    }

    public float getDegrees(Point a, Point b) {
        float ax = a.x;                
        float ay = a.y;                
        float bx = b.x;                
        float by = b.y;                
        float degrees = 0;
        if (bx == ax) {                 
            if (by > ay) {              
                degrees = 90;
            } else if (by < ay) {               
                degrees = 270;
            }
        } else if (by == ay) {                
            if (bx > ax) {              
                degrees = 0;
            } else if (bx < ax) {               
                degrees = 180;
            }
        } else {
            if (bx > ax)                  
            {
                if (by > ay)                  
                {
                    degrees = 0;
                    degrees = degrees + switchDegrees(Math.abs(by - ay), Math.abs(bx - ax));
                } else if (by < ay)                 
                {
                    degrees = 360;
                    degrees = degrees - switchDegrees(Math.abs(by - ay), Math.abs(bx - ax));
                }

            } else if (bx < ax)                  
            {
                if (by > ay)                     
                {
                    degrees = 90;
                    degrees = degrees + switchDegrees(Math.abs(bx - ax), Math.abs(by - ay));
                } else if (by < ay)                    
                {
                    degrees = 270;
                    degrees = degrees - switchDegrees(Math.abs(bx - ax), Math.abs(by - ay));
                }
            }
        }
        return degrees;
    }

        
  
  
  
  
  
    private float switchDegrees(float x, float y) {
        return (float) MathUtil.pointTotoDegrees(x, y);
    }

        
  
  
    public int[] getArrayIndex(int index) {
        int[] ai = new int[2];
        ai[0] = index / 3;
        ai[1] = index % 3;
        return ai;
    }

        
  
  
    private Point checkSelectPoint(float x, float y) {
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                Point p = mPoints[i][j];
                if (RoundUtil.checkInRound(p.x, p.y, r, (int) x, (int) y)) {
                    return p;
                }
            }
        }
        return null;
    }

        
  
  
    private int crossPoint(Point p) {
                           
        if (sPoints.contains(p)) {
            if (sPoints.size() > 2) {
                            
                if (sPoints.get(sPoints.size() - 1).index != p.index) {
                    return 2;
                }
            }
            return 1;            
        } else {
            return 0;       
        }
    }

        
  
  
    private void addPoint(Point point) {
        this.sPoints.add(point);
    }

        
  
  
    private String toPointString() {
        if (sPoints.size() >= passwordMinLength) {
            StringBuffer sf = new StringBuffer();
            for (Point p : sPoints) {
                sf.append(",");
                sf.append(p.index);
            }
            return sf.deleteCharAt(0).toString();
        } else {
            return null;
        }
    }

    boolean movingNoPoint = false;
    float moveingX, moveingY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
                
        if (!isTouch) {
            return false;
        }
        movingNoPoint = false;

        float ex = event.getX();
        float ey = event.getY();
        boolean isFinish = false;
        boolean redraw = false;
        Point p = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:       
                                
                if (task != null) {
                    task.cancel();
                    task = null;
                    Log.d("task", "touch cancel()");
                }
                          
                reset();
                p = checkSelectPoint(ex, ey);
                if (p != null) {
                    checking = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:       
                if (checking) {
                    p = checkSelectPoint(ex, ey);
                    if (p == null) {
                        movingNoPoint = true;
                        moveingX = ex;
                        moveingY = ey;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:       
                p = checkSelectPoint(ex, ey);
                checking = false;
                isFinish = true;
                break;
        }
        if (!isFinish && checking && p != null) {

            int rk = crossPoint(p);
            if (rk == 2) {            
                movingNoPoint = true;
                moveingX = ex;
                moveingY = ey;
                redraw = true;
            } else if (rk == 0) {         
                p.state = Point.STATE_CHECK;
                addPoint(p);
                redraw = true;
            }
                           
        }
                
        if (redraw) {}
        if (isFinish) {
            if (trackTimes == 0) {
                trackTimes++;
                if (this.sPoints.size() == 1) {
                    this.reset();
                    trackTimes = 0;
                } else if (this.sPoints.size() < passwordMinLength && this.sPoints.size() > 0) {
                    error();
                    clearPassword();
                    trackTimes = 0;
                    MessageUtil.alertMessage(this.getContext(), R.string.gesture_short);
                } else if (mCompleteListener != null) {
                    if (this.sPoints.size() >= passwordMinLength) {
                        this.disableTouch();
                        firstTrack = toPointString();
                        Log.i("LocusPassWordView", "firstTrack = " + firstTrack);
                        if (StringUtil.isEmpty(firstTrack)) {
                            trackTimes = 0;
                        } else {
                            if (isUnlock) {         
                                savePassword(firstTrack);
                                mCompleteListener.onComplete(firstTrack);
                            } else {
                                MessageUtil.alertMessage(this.getContext(), R.string.draw_gesture_again);
                            }
                        }
                        this.reset();
                    }
                }
            } else if (trackTimes == 1) {
                if (this.sPoints.size() == 1) {
                    this.reset();
                } else if (this.sPoints.size() < passwordMinLength && this.sPoints.size() > 0) {
                    error();
                    clearPassword();
                    MessageUtil.alertMessage(this.getContext(), R.string.gesture_short);
                } else if (mCompleteListener != null) {
                    if (this.sPoints.size() >= passwordMinLength) {
                        this.disableTouch();
                        secondTrack = toPointString();
                        Log.i("LocusPassWordView", "secondTrack = " + secondTrack);
                        if (secondTrack.equals(firstTrack)) {             
                            savePassword(secondTrack);
                            mCompleteListener.onComplete(secondTrack);
                        } else {              
                            clearPassword();
                            MessageUtil.alertMessage(this.getContext(), R.string.different_gestrue);
                        }
                    }
                }
            }

        }
        this.invalidate();
        return true;
    }

        
  
  
    private void error() {
        for (Point p : sPoints) {
            p.state = Point.STATE_CHECK_ERROR;
        }
    }

        
  
  
    public void markError() {
        markError(CLEAR_TIME);
    }

        
  
  
    public void markError(final long time) {
        for (Point p : sPoints) {
            p.state = Point.STATE_CHECK_ERROR;
        }
        this.clearPassword(time);
    }

        
  
  
    public void enableTouch() {
        isTouch = true;
    }

        
  
  
    public void disableTouch() {
        isTouch = false;
    }

    private Timer timer = new Timer();
    private TimerTask task = null;

        
  
  
    private void reset() {
        for (Point p : sPoints) {
            p.state = Point.STATE_NORMAL;
        }
        sPoints.clear();
        this.enableTouch();
    }

        
  
  
    public void clearPassword() {
        clearPassword(CLEAR_TIME);
    }

        
  
  
    public void clearPassword(final long time) {
        if (time > 1) {
            if (task != null) {
                task.cancel();
                Log.d("task", "clearPassword cancel()");
            }
            lineAlpha = 130;
            invalidate();
            task = new TimerTask() {
                public void run() {
                    reset();
                    invalidate();
                }
            };
            Log.d("task", "clearPassword schedule(" + time + ")");
            timer.schedule(task, time);
        } else {
            reset();
            invalidate();
        }

    }
    
    public void clearUnlockInfo() {
        trackTimes = 0;
        firstTrack = null;
        secondTrack = null;
    }

        
  
  
    private String getPassword() {
        return PreferenceUtil.getInstance(getContext()).getString("gestureTracks", null);
    }

        
  
  
    public boolean isPasswordEmpty() {
        return StringUtil.isEmpty(getPassword());
    }

    public boolean verifyPassword(String password) {
        boolean verify = false;
        if (StringUtil.isNotEmpty(password) && password.equals(getPassword())) {
            verify = true;
        }
        return verify;
    }

        
  
  
    public void savePassword(String password) {
        PreferenceUtil.getInstance(getContext()).saveString("gestureTracks", password);
    }

    public void setPasswordMinLength(int passwordMinLength) {
        this.passwordMinLength = passwordMinLength;
    }
    
    public void setIsUnlock(boolean isUnlock) {
        this.isUnlock = isUnlock;
    }
    
    public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

        
  
  
    public interface OnCompleteListener {
        public void onComplete(String password);
    }
}
