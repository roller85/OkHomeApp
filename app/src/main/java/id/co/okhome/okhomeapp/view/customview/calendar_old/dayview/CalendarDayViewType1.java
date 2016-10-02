package id.co.okhome.okhomeapp.view.customview.calendar_old.dayview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.model.CleaningScheduleModel;
import id.co.okhome.okhomeapp.view.customview.calendar_old.model.CalendarDayType1Model;

/**
 * View to display a day
 * @author Brownsoo
 *
 */
public class CalendarDayViewType1 extends CalendarDayView {

    TextView tvDay;
    ViewGroup vgBg;
    View vParent;
    View vTime;
    TextView tvTime;
    View vLine;
    View vCleaning;
    TextView tvCleaning;

    CleaningScheduleModel cleaningScheduleModel = null;
    String scheduledDate = null;

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
        vTime = v.findViewById(R.id.itemCalendarDay_vTime);
        tvTime = (TextView)v.findViewById(R.id.itemCalendarDay_tvTime);
        vLine = v.findViewById(R.id.itemCalendarDay_vLine);
        vCleaning = v.findViewById(R.id.itemCalendarDay_vCleaning);
        tvCleaning = (TextView)v.findViewById(R.id.itemCalendarDay_tvCleaning);
    }

    @Override
    public CalendarDayType1Model getDayModel() {
        return (CalendarDayType1Model)super.getDayModel();
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setCleaningReservationModel(CleaningScheduleModel cleaningScheduleModel) {
        this.cleaningScheduleModel = cleaningScheduleModel;
    }

    public CleaningScheduleModel getCleaningReservationModel() {
        return cleaningScheduleModel;
    }

    @Override
    public void clear() {
        cleaningScheduleModel = null;
    }

    private void setTvDayColor(int sundayColor, int saturdayColor, int normalColor){
        //일요일 처리
        if(getDayModel().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tvDay.setTextColor(sundayColor);
        }
        //토요일 처리
        else if(getDayModel().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tvDay.setTextColor(saturdayColor);
        }
        //일반 처리
        else {
            tvDay.setTextColor(normalColor);
        }
    }

    @Override
    public void refresh() {
        tvDay.setText(String.valueOf(getDayModel().get(Calendar.DAY_OF_MONTH)));
        vTime.setVisibility(View.GONE);
        vLine.setVisibility(View.GONE);
        vCleaning.setVisibility(View.GONE);


        setTvDayColor(Color.parseColor("#e53434"), Color.parseColor("#3494e5"), Color.parseColor("#454849"));

        //예약가능여부 확인
        if(getDayModel().chkAbleReservation()){
            //가능
            if(cleaningScheduleModel != null ){
                vCleaning.setVisibility(View.VISIBLE);
            }else if(scheduledDate != null){
                vCleaning.setVisibility(View.VISIBLE);
                tvCleaning.setText("예정일");
            }

            if(getDayModel().isCurrentMonth){
                vgBg.setBackgroundColor(Color.parseColor("#06000000"));
                vCleaning.setBackgroundColor(Color.parseColor("#887f8a9e"));
            }else{
                vgBg.setBackgroundColor(Color.parseColor("#02000000"));
                setTvDayColor(Color.parseColor("#55e53434"), Color.parseColor("#553494e5"), Color.parseColor("#55454849"));
                vCleaning.setBackgroundColor(Color.parseColor("#337f8a9e"));
            }


            //시간선택되어있으면
            if(getDayModel().time != null && !getDayModel().time.equals("")){

                int timeLeft = Integer.parseInt(getDayModel().time.substring(0, getDayModel().time.indexOf(":")));
                if(timeLeft < 12){
                    tvTime.setText("AM" + timeLeft + getDayModel().time.substring(getDayModel().time.indexOf(":")));
                }else{
                    tvTime.setText("PM" + (timeLeft-12) + getDayModel().time.substring(getDayModel().time.indexOf(":")));
                }

                vTime.setVisibility(View.VISIBLE);

            }

            else if(getDayModel().isStartDate != false){
                vTime.setVisibility(View.VISIBLE);
                vLine.setVisibility(View.VISIBLE);
                tvTime.setText("시작일");
            }



        }else{
            //불가능
            vgBg.setBackgroundColor(Color.parseColor("#02000000"));
            setTvDayColor(Color.parseColor("#22e53434"), Color.parseColor("#223494e5"), Color.parseColor("#22454849"));
        }
    }

}