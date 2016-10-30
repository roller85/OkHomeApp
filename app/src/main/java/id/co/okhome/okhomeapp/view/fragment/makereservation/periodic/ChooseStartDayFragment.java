package id.co.okhome.okhomeapp.view.fragment.makereservation.periodic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarView;
import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthGridView;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthViewListener;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChooseStartDayFragment extends Fragment implements MakeReservationFlow, MonthViewListener {
    @BindView(R.id.fragmentMakeReservationChooseDay_flContents)           FrameLayout flContent;
    @BindView(R.id.fragmentMakeReservationChooseDay_tvYearMonth)          TextView tvYearMonth;
    @BindView(R.id.fragmentMakeReservationChooseDay_vbtnLeft)             View vbtnMonthLeft;
    @BindView(R.id.fragmentMakeReservationChooseDay_vbtnRight)            View vbtnMonthRight;
    @BindView(R.id.fragmentMakeReservationChooseDay_vLoading)             View vLoading;

    CalendarView calendarView;
    MonthGridView currentMonthGridView;
    DayModel prevDayModel = null;

    int targetYear, targetMonth;
    MakeReservationParam params;
    String startDate = "";
    int currentYear, currentMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_day2, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        targetYear = Util.getCurrentYear();
        targetMonth = Util.getCurrentMonth();

        getView().post(new Runnable() {
            @Override
            public void run() {
                mesuredViewSize();
            }
        });
    }

    //캘린더 초기화
    private void initCalendar() {
        final int height = flContent.getHeight();
        flContent.removeAllViews();
        new Thread() {
            @Override
            public void run() {
                calendarView = new CalendarView(flContent.getContext());
                calendarView.initCalendar(targetYear, targetMonth, height, ChooseStartDayFragment.this);
                handler.sendEmptyMessage(0);
            }

            Handler handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    flContent.addView(calendarView);
                    onCalendarInit(calendarView);
                }
            };
        }.start();

    }

    private void setLoadingVisibility(final boolean on){
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(on){
                        vLoading.setVisibility(View.VISIBLE);
                    }else{
                        vLoading.setVisibility(View.GONE);
                    }
                }
            });
        }catch(NullPointerException e){
            ;
        }

    }

    public void loadSchedule(){
        currentMonthGridView.clear();
        try{

            if(params == null){
                throw new OkhomeException("params are not setting yet");
            }

            String periodicPlan = params.period;
            String weekType = params.weekType;


            List<String> listScheduledDate = new ArrayList<>();

            //1-1이번달 시작일, 마지막을 먼저 알아내고..
            Map<String, String> mapYyyymm = Util.getMonthRange42(currentYear, currentMonth - 1);
            String beginDate = mapYyyymm.get("startDate");
            String endDate = mapYyyymm.get("endDate");

            Calendar cMonthCursor = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try{
                cMonthCursor.setTime(dateFormat.parse(beginDate));
//                cDatePivot.setTime(dateFormat.parse(startDate));
            }catch(ParseException e){
                throw new OkhomeException("Date err");
            }


            String[] arrWeekdays = periodicPlan.split(",");
            for(int i = 0; i < 42; i++){

//                if(cMonthCursor.getTimeInMillis() < cDatePivot.getTimeInMillis()){
//                    continue;
//                }

                String scheduledTime = "";
                int dayOfWeek = cMonthCursor.get(Calendar.DAY_OF_WEEK)- 1;
                String yyyyMMdd = cMonthCursor.get(Calendar.YEAR) + "-" + Util.getFull2Decimal(cMonthCursor.get(Calendar.MONTH) + 1) + "-" + Util.getFull2Decimal(cMonthCursor.get(Calendar.DAY_OF_MONTH));
                //1주짜리일경우


                if(weekType.equals("1W")){
                    scheduledTime = arrWeekdays[dayOfWeek];
                }

                //2주짜리일경우
                else if(weekType.equals("2W")){
                    try{
                        //몇번째 주인지 가져오자
                        int weekCount = Util.getWeekFromSpecificDate(beginDate, yyyyMMdd);

                        if(weekCount % 2 == 0){
                            scheduledTime = arrWeekdays[dayOfWeek];
                        }else{
                            scheduledTime = arrWeekdays[dayOfWeek + 7];
                        }

                    }catch(Exception e){
                        //예외발생시 다음 시작
                        e.printStackTrace();
                        cMonthCursor.add(Calendar.DAY_OF_MONTH, 1);
                        continue;
                    }
                }

                if(!scheduledTime.equals("X") && !scheduledTime.equals("")){
                    //일정있는거니깐 리스트에 보관
                    listScheduledDate.add(yyyyMMdd + " " + scheduledTime);
                }else{

//                    //다지우기
//                    try{
//                        String yyyymmddReduced = yyyyMMdd.substring(0, 10).replace("-", "");
//                        currentMonthGridView.getDayModel(yyyymmddReduced).optPlanned = false;
//                    }catch(Exception e){
//                        Util.showToast(getContext(), yyyyMMdd);
//                    }
                }

                cMonthCursor.add(Calendar.DAY_OF_MONTH, 1);
            }

            //달력에 처리

            adaptCalendarWithSchedule(listScheduledDate);
        }catch(OkhomeException e){
            e.printStackTrace();
            return;
        }
    }


    Handler handlerForNotifyCurrentMonthView = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            currentMonthGridView.notifyDataSetChanged();
        }
    };

    private void adaptCalendarWithSchedule(final List<String> listResult){

        new Thread(){
            @Override
            public void run() {
                try{
                    for(String date : listResult){
                        String yyyymmdd = date.substring(0, 10).replace("-", "");
                        currentMonthGridView.getDayModel(yyyymmdd).optPlanned = true;
                    }

                    handlerForNotifyCurrentMonthView.sendEmptyMessage(0);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //달력로드완료
    private void onCalendarInit(final CalendarView calendarView) {
        calendarView.setMonthNavigator(vbtnMonthLeft, vbtnMonthRight, tvYearMonth);
    }

    public void mesuredViewSize(){
        initCalendar();
    }

    @Override
    public void onMonthSelected(int year, int month, MonthGridView monthGridView) {
        currentYear = year;
        currentMonth = month;
        currentMonthGridView = monthGridView;
        loadSchedule();
    }

    @Override
    public void onDayClick(int position, int week, final DayModel dayModel) {
        if(!dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())){
            return;
        }

        if(dayModel.cleaningScheduleModel != null){
            Util.showToast(getContext(), "Already reserved");
            return;
        }

        if(prevDayModel != null){
            prevDayModel.optBeginDay = false;
        }

        dayModel.optBeginDay = true;

        prevDayModel = dayModel;

        currentMonthGridView.notifyDataSetChanged();
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {
        this.params = params;
        loadSchedule();
    }

    @Override
    public boolean next(MakeReservationParam params) {
        if(startDate.equals("")){
            Util.showToast(getContext(), "Choose a day for cleaning");
            return false;
        }else{
            params.startDate = startDate;
            return true;
        }

    }
}
