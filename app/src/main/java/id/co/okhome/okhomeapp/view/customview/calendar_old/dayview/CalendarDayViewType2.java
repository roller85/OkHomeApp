package id.co.okhome.okhomeapp.view.customview.calendar_old.dayview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.view.customview.calendar_old.model.CalendarDayType2Model;

/**
 * View to display a day
 * @author Brownsoo
 *
 */
public class CalendarDayViewType2 extends CalendarDayView {

    TextView tvDay;
    ViewGroup vgBg;
    View vCleaning;
    View vParent;
    View vTime;
    TextView tvTime;
    View vLine;

    public CalendarDayViewType2(Context context) {
        super(context);
    }

    public CalendarDayViewType2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context) {
        View v = inflate(context, R.layout.item_calendar_day, this);
        vParent = v;
        tvDay = (TextView) v.findViewById(R.id.itemCalendarDay_tvDay);
        vgBg = (ViewGroup)v.findViewById(R.id.itemCalendarDay_vBg);
        vTime = v.findViewById(R.id.itemCalendarDay_vTime);
        tvTime = (TextView)v.findViewById(R.id.itemCalendarDay_tvTime);
        vLine = v.findViewById(R.id.itemCalendarDay_vLine);
    }

    @Override
    public CalendarDayType2Model getDayModel() {
        return (CalendarDayType2Model)super.getDayModel();
    }

    @Override
    public void clear() {

    }

    @Override
    public void refresh() {
        tvDay.setText(String.valueOf(getDayModel().get(Calendar.DAY_OF_MONTH)));
        vTime.setVisibility(View.GONE);
        vLine.setVisibility(View.GONE);

        //일요일 처리
        if(getDayModel().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tvDay.setTextColor(Color.parseColor("#e53434"));
        }
        //토요일 처리
        else if(getDayModel().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tvDay.setTextColor(Color.parseColor("#3494e5"));
        }
        //일반 처리
        else {
            tvDay.setTextColor(getResources().getColor(R.color.okHomeGrayDeep));
        }

        if(getDayModel().isCurrentMonth){
            vgBg.setBackgroundColor(Color.parseColor("#06000000"));
            tvDay.setAlpha(1f);
        }else{
            vgBg.setBackgroundColor(Color.parseColor("#02000000"));
            tvDay.setAlpha(0.5f);
        }

    }

}