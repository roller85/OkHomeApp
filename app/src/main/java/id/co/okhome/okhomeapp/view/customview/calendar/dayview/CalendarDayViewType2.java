package id.co.okhome.okhomeapp.view.customview.calendar.dayview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayType2Model;

/**
 * View to display a day
 * @author Brownsoo
 *
 */
public class CalendarDayViewType2 extends CalendarDayView {

    private TextView tvDay;
    private TextView tvTime;
    private ViewGroup vgBg;
    private View vgTime;

    public CalendarDayViewType2(Context context) {
        super(context);
    }
    public CalendarDayViewType2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context) {
        View v = inflate(context, R.layout.item_calendar_day_type2, this);
        tvDay = (TextView) v.findViewById(R.id.itemCalendarDay_tvDay);
        vgBg = (ViewGroup)v.findViewById(R.id.itemCalendarDay_vBg);
        tvTime = (TextView)v.findViewById(R.id.itemCalendarDay_tvTime);
        vgTime = v.findViewById(R.id.itemCalendarDay_llTime);
    }

    @Override
    public CalendarDayType2Model getDayModel() {
        return (CalendarDayType2Model)super.getDayModel();
    }

    @Override
    public void refresh() {
        tvDay.setText(String.valueOf(getDayModel().get(Calendar.DAY_OF_MONTH)));

        if(getDayModel().isCurrentMonth){
            tvDay.setAlpha(1f);
        }else{
            tvDay.setAlpha(0.3f);
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

        //
        if(getDayModel().time == null){
            vgTime.setVisibility(GONE);
        }else{
            vgTime.setVisibility(VISIBLE);
            tvTime.setText(getDayModel().time);
        }

    }
    
}