package id.co.okhome.okhomeapp.view.customview.calendar.dayview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayType1Model;

/**
 * View to display a day
 * @author Brownsoo
 *
 */
public class CalendarDayViewType1 extends CalendarDayView {
 
    TextView tvDay;
    ViewGroup vgBg;
    View vCleaning;
    View vParent;

    public CalendarDayViewType1(Context context) {
        super(context);
    }

    public CalendarDayViewType1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context) {
        View v = inflate(context, R.layout.item_calendar_day, this);
        vParent = v;
        tvDay = (TextView) v.findViewById(R.id.itemCalendarDay_tvDay);
        vgBg = (ViewGroup)v.findViewById(R.id.itemCalendarDay_vBg);
    }

    @Override
    public CalendarDayType1Model getDayModel() {
        return (CalendarDayType1Model)super.getDayModel();
    }

    @Override
    public void refresh() {
        tvDay.setText(String.valueOf(getDayModel().get(Calendar.DAY_OF_MONTH)));

        if(getDayModel().isCurrentMonth){
            vgBg.setBackgroundColor(Color.parseColor("#05000000"));
            tvDay.setAlpha(1f);
        }else{
            vgBg.setBackgroundColor(Color.parseColor("#02000000"));
            tvDay.setAlpha(0.5f);
        }

        if(getDayModel().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tvDay.setTextColor(Color.parseColor("#e53434"));
        }
        else if(getDayModel().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tvDay.setTextColor(Color.parseColor("#3494e5"));
        }
        else {
            tvDay.setTextColor(getResources().getColor(R.color.okHomeGrayDeep));
        }

    }
}