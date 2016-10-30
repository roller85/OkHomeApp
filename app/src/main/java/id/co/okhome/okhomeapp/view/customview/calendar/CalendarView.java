package id.co.okhome.okhomeapp.view.customview.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import id.co.okhome.okhomeapp.lib.Util;


/**
 * Created by josongmin on 2016-09-28.
 */

public class CalendarView extends ViewPager implements MonthViewListener{

    //layer_calendar파싱해서 넣자..ㅅㅂ

    MonthAdapter monthAdapter;
    View vLeftBtn, vRightBtn;
    TextView tvYearMonth;
    MonthViewListener monthViewListener;
    int currentYear, currentMonth;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setMonthNavigator(View vLeftBtn, View vRightBtn, TextView tvYearMonth){
        this.vLeftBtn = vLeftBtn;
        this.vRightBtn = vRightBtn;
        this.tvYearMonth = tvYearMonth;

        vRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //오르쪽
                setCurrentItem(getCurrentItem()+1);
            }
        });

        vLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //오르쪽
                setCurrentItem(getCurrentItem()-1);
            }
        });

        tvYearMonth.setText(currentYear + "." + Util.getFull2Decimal(currentMonth));
    }

    private void init(){
        //inflate
    }

    public void initCalendar(int year, int month, int height, MonthViewListener monthViewListener){
        this.monthViewListener = monthViewListener;
        monthAdapter = new MonthAdapter(this, year, month, height, this);
        addOnPageChangeListener(monthAdapter);
        setAdapter(monthAdapter);
        setOffscreenPageLimit(1);
        setCurrentItem(monthAdapter.getCount() / 2);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public MonthAdapter getAdapter() {
        return (MonthAdapter)super.getAdapter();
    }

    public MonthGridView getCurrentMonthView(){
        return getAdapter().getCurrentMonthView();
    }

    @Override
    public void onDayClick(int position, int week, DayModel dayModel) {
        if(monthViewListener != null){
            monthViewListener.onDayClick(position, week, dayModel);
        }
    }

    @Override
    public void onMonthSelected(int year, int month, MonthGridView monthGridView) {
        currentYear = year;
        currentMonth = month;


        if(monthViewListener != null){
            monthViewListener.onMonthSelected(year, month, monthGridView);
        }

        if(tvYearMonth != null){
            tvYearMonth.setText(year + "." + Util.getFull2Decimal(month));
        }

    }



}
