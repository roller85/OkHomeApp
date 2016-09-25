package id.co.okhome.okhomeapp.view.customview.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.view.customview.calendar.dayview.CalendarDayView;
import id.co.okhome.okhomeapp.view.customview.calendar.dayview.CalendarDayViewType1;
import id.co.okhome.okhomeapp.view.customview.calendar.dayview.CalendarDayViewType2;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayType1Model;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayType2Model;

public class CalendarMonthView extends LinearLayout implements View.OnClickListener {

    private static final String NAME = "OneMonthView";
    private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());

    private int mYear;
    private int mMonth;
    private ArrayList<LinearLayout> arrWeek = null;
    private ArrayList<CalendarDayView> arrDayView = null;
    private Map<String, CalendarDayView> mapDayView = new HashMap<String, CalendarDayView>();
    private CalendarType calendarType;
    private OnDayClickListener onDayClickListener = null;

    public CalendarMonthView(Context context) {
        this(context, null);
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarMonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    private CalendarDayModel initNewModel(){
        if(calendarType == CalendarType.TYPE1){
            return new CalendarDayType1Model();
        }else{
            return new CalendarDayType2Model();
        }
    }

    public void init(){
        setOrientation(LinearLayout.VERTICAL);

        //Prepare many day-views enough to prevent recreation.
        if(arrWeek == null) {

            arrWeek = new ArrayList<>(6); //Max 6 arrWeek in a month
            arrDayView = new ArrayList<>(42); // 7 days * 6 arrWeek = 42 days

            LinearLayout ll = null;
            for(int i=0; i<42; i++) {

                if(i % 7 == 0) {
                    //Create new week layout
                    ll = new LinearLayout(getContext());
                    LinearLayout.LayoutParams params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    params.weight = 1;
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setLayoutParams(params);
                    ll.setWeightSum(7);

                    arrWeek.add(ll);
                }

                LinearLayout.LayoutParams params
                        = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1;

                CalendarDayView ov = null;
                if(calendarType == CalendarType.TYPE1){
                    ov = new CalendarDayViewType1(getContext());
                }else if(calendarType == CalendarType.TYPE2){
                    ov = new CalendarDayViewType2(getContext());
                }

                ov.setLayoutParams(params);
                ov.setOnClickListener(this);

                ll.addView(ov);
                arrDayView.add(ov);

            }
        }

        //for Preview of Graphic editor
        if(isInEditMode()) {
            Calendar cal = Calendar.getInstance();
            make(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        }
    }

    public void setCalendarType(CalendarType calendarType){
        this.calendarType = calendarType;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void make(int year, int month){
        if(mYear == year && mMonth == month) {
            return;
        }

        long makeTime = System.currentTimeMillis();

        this.mYear = year;
        this.mMonth = month;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//Sunday is first day of week in this sample

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);//Get day of the week in first day of this month
        int maxOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);//Get max day number of this month
        ArrayList<CalendarDayModel> oneDayDataList = new ArrayList<>();

        cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);//Move to first day of first week
        //HLog.d(TAG, CLASS, "first day : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));

        /* add previous month */
        int seekDay;
        //첫번째거 처리 색상다르게
        for(;;) {
            seekDay = cal.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == seekDay) break;

            CalendarDayModel one = initNewModel();
            one.isCurrentMonth = false;
            one.setDay(cal);
            oneDayDataList.add(one);
            //하루 증가
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        //HLog.d(TAG, CLASS, "this month : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));
        /* add this month */
        for(int i=0; i < maxOfMonth; i++) {
            CalendarDayModel one = initNewModel();
            one.setDay(cal);
            one.isCurrentMonth = true;
            oneDayDataList.add(one);
            //add one day
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        //나머지 처리. 색상다르게
        for(int i = oneDayDataList.size(); i < 42; i++){
            CalendarDayModel one = initNewModel();
            one.setDay(cal);
            one.isCurrentMonth = false;
            oneDayDataList.add(one);

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        if(oneDayDataList.size() == 0) return;

        //Remove all day-views
        this.removeAllViews();

        int count = 0;
        for(CalendarDayModel dayModel : oneDayDataList) {

            if(count % 7 == 0) {
                addView(arrWeek.get(count / 7));
            }
            CalendarDayView dayView = arrDayView.get(count);
            dayView.clear();
            dayView.setDay(dayModel);
            dayView.refresh();

            String key = dayModel.get(Calendar.YEAR)+""
                    + Util.fillupWithZero(dayModel.get(Calendar.MONTH)+1, "XX")
                    + Util.fillupWithZero(dayModel.get(Calendar.DAY_OF_MONTH), "XX");
            mapDayView.put(key, dayView);
            count++;
        }

        //Set the weight-sum of LinearLayout to week counts
        this.setWeightSum(getChildCount());
    }

    public CalendarDayView getDayView(String yyyymmdd){
        return mapDayView.get(yyyymmdd);
    }

    public CalendarDayView getDayViewSample(){
        return arrDayView.get(0);
    }

    @Override
    public void onClick(View v) {
        CalendarDayView ov = (CalendarDayView) v;
        if(onDayClickListener != null) {
            CalendarDayModel dayModel = ov.getDayModel();
            String year = dayModel.get(Calendar.YEAR) + "";
            String month  = Util.fillupWithZero(dayModel.get(Calendar.MONTH)+1, "XX");
            String day = Util.fillupWithZero(dayModel.get(Calendar.DAY_OF_MONTH), "XX");

            onDayClickListener.onDayClick(arrDayView, ov, dayModel, year, month, day);
        }

    }
    public enum CalendarType {TYPE1, TYPE2};

    public interface OnDayClickListener<T extends CalendarDayModel>{
        public void onDayClick(
                List<CalendarDayView> listCalendarDayView, CalendarDayView dayview, T dayModel, String year, String month, String day);
    }
}