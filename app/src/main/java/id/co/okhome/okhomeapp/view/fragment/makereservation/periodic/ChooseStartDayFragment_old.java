package id.co.okhome.okhomeapp.view.fragment.makereservation.periodic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.CalendarController;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.view.customview.calendar_old.CalendarMonthView;
import id.co.okhome.okhomeapp.view.customview.calendar_old.dayview.CalendarDayView;
import id.co.okhome.okhomeapp.view.customview.calendar_old.dayview.CalendarDayViewType1;
import id.co.okhome.okhomeapp.view.customview.calendar_old.model.CalendarDayType1Model;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChooseStartDayFragment_old extends Fragment implements CalendarMonthView.OnDayClickListener<CalendarDayType1Model>, CalendarController.OnCalendarChangeListener, MakeReservationFlow{

    @BindView(R.id.layerCalendar_pbLoading)
    View vLoading;

    CalendarDayView dayViewPrev = null;
    CalendarController calendarController;
    boolean calendarLoaded = false;
    MakeReservationParam params;
    String startDate = "";
    int currentYear, currentMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_day, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        ButterKnife.bind(this, getView());

        calendarController = CalendarController.with(this)
                .setCalendarType(CalendarMonthView.CalendarType.TYPE1)
                .setOnDayClickListner(this)
                .setOnCalendarChangeListener(this);
        calendarController.initCalendar();
    }

    public void loadSchedule(){

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
//            Calendar cDatePivot = Calendar.getInstance();
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

                    //다지우기
                    try{
                        String yyyymmddReduced = yyyyMMdd.substring(0, 10).replace("-", "");
                        final CalendarDayViewType1 cDayView = (CalendarDayViewType1)calendarController.getCurrentMonthView().getDayView(yyyymmddReduced);
                        cDayView.setScheduledDate(null);
                        cDayView.refresh();
                    }catch(Exception e){
                        Util.showToast(getContext(), yyyyMMdd);
                    }


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

    //스케쥴 동기화
    private void adaptCalendarWithSchedule(final List<String> listResult){
        new Thread(){
            @Override
            public void run() {
                try{
                    for(String date : listResult){
                        String yyyymmdd = date.substring(0, 10).replace("-", "");
                        final CalendarDayViewType1 cDayView = (CalendarDayViewType1)calendarController.getCurrentMonthView().getDayView(yyyymmdd);

                        cDayView.setScheduledDate(date);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cDayView.refresh();
                            }
                        });

                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calendarController.dismissLoading();
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
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

    @Override
    public void onMonthChange(final int year, final int month) {
        currentYear = year;
        currentMonth = month;
        loadSchedule();
    }

    @Override
    public void onCalendarLoad() {
        //불름성공
        calendarLoaded = true;

    }

    @Override
    public void onDayClick(final List<CalendarDayView> listCalendarDayView, final CalendarDayView calendarDayView, final CalendarDayType1Model dayModel
            , final String year, final String month, final String day) {


        if(!dayModel.chkAbleReservation()){
            return;
        }

        CalendarDayViewType1 cdv = (CalendarDayViewType1)calendarDayView;
        if(cdv.getCleaningReservationModel() != null){
            Util.showToast(getContext(), "Already reserved");
            return;
        }


        if(dayViewPrev != null){
            ((CalendarDayType1Model)dayViewPrev.getDayModel()).isStartDate = false;
            dayViewPrev.refresh();
        }

        for(CalendarDayView dv : listCalendarDayView){
            if(dv.getDayModel() == dayModel){
                ((CalendarDayType1Model)dv.getDayModel()).isStartDate = true;
                dayViewPrev = dv;
                dv.refresh();
                startDate = year + "-" + month + "-" + day;
                break;
            }
        }

        if(!startDate.equals("")){
            loadSchedule();
        }
    }

}
