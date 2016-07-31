package id.co.okhome.okhomeapp.view.customview.calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayModel;

/**
 * View to display a day
 * @author Brownsoo
 *
 */
public class CalendarDayView extends RelativeLayout {
 
    private static final String TAG = "OneDayView";
    private static final String NAME = "OneDayView";
    private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
    
    private TextView tvDay;
    private CalendarDayModel dayModel;
    private ViewGroup vgBg;
    private View vCleaning;

    public CalendarDayView(Context context) {
        super(context);
        init(context);
 
    }

    public CalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
 
    private void init(Context context){
        View v = View.inflate(context, R.layout.item_calendar_day, this);
        tvDay = (TextView) v.findViewById(R.id.itemCalendarDay_tvDay);
        vgBg = (ViewGroup)v.findViewById(R.id.itemCalendarDay_vBg);
        vCleaning = v.findViewById(R.id.itemCalendarDay_ivCleaning);
        vCleaning.setVisibility(View.INVISIBLE);
        dayModel = new CalendarDayModel();
    }

    public void setDay(int year, int month, int day) {
        this.dayModel.cal.set(year, month, day);
    }

    public void setDay(Calendar cal) {
        this.dayModel.setDay((Calendar) cal.clone());
    }

    public void setDay(CalendarDayModel dayModel) {
        this.dayModel = dayModel;
    }

    public CalendarDayModel getDayModel() {
        return dayModel;
    }

    public int get(int field) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return dayModel.get(field);
    }

    public void refresh() {
        tvDay.setText(String.valueOf(dayModel.get(Calendar.DAY_OF_MONTH)));

        if(dayModel.isCurrentMonth){
            vgBg.setBackgroundColor(Color.parseColor("#07000000"));
            tvDay.setAlpha(1f);
        }else{
            vgBg.setBackgroundColor(Color.parseColor("#02000000"));
            tvDay.setAlpha(0.5f);
        }

        if(dayModel.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tvDay.setTextColor(Color.parseColor("#e53434"));
        }
        else if(dayModel.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tvDay.setTextColor(Color.parseColor("#3494e5"));
        }
        else {
            tvDay.setTextColor(getResources().getColor(R.color.okHomeGrayDeep));
        }
    }
    
}