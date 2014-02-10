package com.hiibox.houseshelter.view;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.NumericWheelAdapter;
import com.hiibox.houseshelter.listener.OnWheelChangedListener;

public class MyDatepicker extends PopupWindow implements OnWheelChangedListener, OnClickListener {

    private Activity mContext;
    private View mScrollDatePicker;
    private ViewFlipper viewfipper;
    private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter;
    private WheelView yearView, monthView, dayView;
    private String selectedDate = null;
    private Handler handler = null;
    
    public MyDatepicker(Activity context, int year, int month, int day, Handler handler) {
        super(context);
        this.mContext = context;
        this.handler = handler;
        init(year, month, day);
    }

    public MyDatepicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(int year, int month, int day) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mScrollDatePicker = inflater.inflate(R.layout.my_date_picker_layout, null);
        mScrollDatePicker.findViewById(R.id.cancel_tv).setOnClickListener(this);
        mScrollDatePicker.findViewById(R.id.confirm_tv).setOnClickListener(this);
        
        viewfipper = new ViewFlipper(mContext);
        viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        yearView = (WheelView) mScrollDatePicker.findViewById(R.id.year);
        monthView = (WheelView) mScrollDatePicker.findViewById(R.id.month);
        dayView = (WheelView) mScrollDatePicker.findViewById(R.id.day);
        
                
        yearAdapter = new DateNumericAdapter(mContext, year-100, year+100, 100 - 20);
        yearView.setViewAdapter(yearAdapter);
        yearView.setCurrentItem(100);
        yearView.addChangingListener(this);
                 
        monthAdapter = new DateNumericAdapter(mContext, 1, 12, 5);
        monthView.setViewAdapter(monthAdapter);
        monthView.setCurrentItem(month);
        monthView.addChangingListener(this);
               
        updateDays(yearView, monthView, dayView);
        dayView.setCurrentItem(day);
        updateDays(yearView, monthView, dayView);
        dayView.addChangingListener(this);

        viewfipper.addView(mScrollDatePicker);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.update();
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_tv) {
            handler.sendEmptyMessage(0x102);
            dismiss();
        } else if (v.getId() == R.id.confirm_tv) {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("selectedDate", selectedDate);
            msg.setData(data);
            msg.what = 0x101;
            handler.sendMessage(msg);
            dismiss();
        }
    }
    
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        updateDays(yearView, monthView, dayView);
    }

    private void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,
                calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateNumericAdapter(mContext, 1, maxDays,
                calendar.get(Calendar.DAY_OF_MONTH) - 1);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        int years = calendar.get(Calendar.YEAR) - 100;
        selectedDate = years + "/" + (month.getCurrentItem() + 1) + "/"
                + (day.getCurrentItem() + 1);
    }
    
        
  
  
    private class DateNumericAdapter extends NumericWheelAdapter {
                                 
        @SuppressWarnings("unused")
        int currentItem;
                                           
        @SuppressWarnings("unused")
        int currentValue;

            
  
  
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(24);
        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public CharSequence getItemText(int index) {
            currentItem = index;
            return super.getItemText(index);
        }

    }

}
