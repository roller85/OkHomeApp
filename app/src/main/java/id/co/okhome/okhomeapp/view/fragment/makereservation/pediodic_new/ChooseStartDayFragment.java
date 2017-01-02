package id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.model.CleaningScheduleModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarView;
import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthGridView;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthViewListener;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChooseStartDayFragment extends Fragment implements MakeCleaningReservationFlow, MonthViewListener {
    @BindView(R.id.fragmentMakeReservationChooseDay_flContents)           FrameLayout flContent;
    @BindView(R.id.fragmentMakeReservationChooseDay_tvYearMonth)          TextView tvYearMonth;
    @BindView(R.id.fragmentMakeReservationChooseDay_vbtnLeft)             View vbtnMonthLeft;
    @BindView(R.id.fragmentMakeReservationChooseDay_vbtnRight)            View vbtnMonthRight;
    @BindView(R.id.fragmentMakeReservationChooseDay_vLoading)             View vLoading;


    CalendarView calendarView;
    MonthGridView currentMonthGridView;
    DayModel prevDayModel = null;

    int targetYear, targetMonth;
    int currentYear, currentMonth;

    MakeCleaningReservationParam params;
    boolean isFirst = true;

    boolean hasReservationReadyFlag = false;
    boolean hasReservationOkFlag = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_day2, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        if(isFirst){
            targetYear = Util.getCurrentYear();
            targetMonth = Util.getCurrentMonth();
            getView().post(new Runnable() {
                @Override
                public void run() {
                    mesuredViewSize();
                }
            });
        }else{
            ;
        }



    }

    //캘린더 초기화
    private void initCalendar() {
        final int height = flContent.getHeight();
        flContent.removeAllViews();
        new Thread() {
            @Override
            public void run() {
                calendarView = new CalendarView(flContent.getContext());
                calendarView.setGridAdapterType(CalendarView.GRIDTYPE_CHOOSE_STARTDAY);
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


    public void loadScheduleWithServer(int year, int month, final MonthGridView monthGridView){
        currentYear = year;
        currentMonth = month;
        currentMonthGridView = monthGridView;

        final String yearMonth = Util.fillupWithZero(year, "0000") + Util.fillupWithZero(month, "00");
        setLoadingVisibility(true);

        hasReservationOkFlag = false;
        hasReservationReadyFlag = false;
        //서버호출. DayModel에서 3가지 데이터 조작. optBeginDay, cleaningScheduleModel, optAbleReservationForPeriod
        RestClient.getCleaningRequestClient().getMonthlyCleaningSchedule(CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext()), yearMonth).enqueue(new RetrofitCallback<List<CleaningScheduleModel>>() {
            @Override
            public void onSuccess(List<CleaningScheduleModel> listResult) {
                if (monthGridView == null) {
                    return;
                }

                //초기화
                monthGridView.clear();
                for(DayModel dayModel : monthGridView.getListDayModels()){
                    dayModel.cleaningScheduleModel = null;
                }

                //예약된 날짜보여주기
                for (CleaningScheduleModel m : listResult) {
                    //내용넣기
                    DayModel dayModel = monthGridView.getDayModel(m.yyyymmdd());
                    if (dayModel != null) {
                        dayModel.cleaningScheduleModel = m;
                        if(m.status.equals("OK")){
                            hasReservationOkFlag = true;
                        }
                        else if(m.status.equals("READY")){
                            hasReservationReadyFlag = true;
                        }
                    }else{

                    }
                }

                //주기맞춰서 달력에 예약가능일자 표시해주기
                String arrDays[] = params.periodValue.split(",");
                //일단 일주일동안 청소 몇번있는지 확인
                Map<Integer, Boolean> mapDay = new HashMap<>();
                for(int i = 0; i < arrDays.length; i++){
                    String day = arrDays[i];
                    if(!day.equals("X")){
                        mapDay.put(i, true);
                    }
                }

                for(DayModel m : monthGridView.getListDayModels()){
                    LocalDate targetDate  = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(m.yyyymmdd);
                    if(mapDay.get(((targetDate.getDayOfWeek()) % 7) ) != null){
                        m.optAbleReservationForPeriod = true;
                    }
                }

                ///////////////선핵한 날짜 표시되게
                DayModel dayModel = monthGridView.getDayModel(params.periodStartDate);
                if (dayModel != null) {
                    dayModel.optBeginDay = true;
                }
                //리스트 변경
                monthGridView.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                setLoadingVisibility(false);
            }
        });
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
        loadScheduleWithServer(year, month, monthGridView);
    }

    @Override
    public void onDayClick(int position, int week, final DayModel dayModel) {

        //없으면 꺼졍
        if(!dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())){
            return;
        }

        if(!dayModel.optAbleReservationForPeriod){
            return;
        }

        if(dayModel.cleaningScheduleModel != null && dayModel.cleaningScheduleModel.status.equals("OK")){
            return;
        }

        //기존에 클릭해놓은거 없애기1
        calendarView.getCurrentMonthView().clear("optBeginDay");

        //기존에 클릭해놓은거 없애기 2
        if(prevDayModel != null){
            prevDayModel.optBeginDay = false;
        }

        dayModel.optBeginDay = true;
        currentMonthGridView.notifyDataSetChanged();
        prevDayModel = dayModel;

        params.periodStartDate = dayModel.yyyymmdd;

        Util.Log(dayModel.yyyymmdd);

        //이번달의 첫번째 셀 가져오기

//        currentMonthGridView.get



    }

//    @Override
//    public void onDayClick(int position, int week, final DayModel dayModel) {
//
//        //없으면 꺼졍
//        if(!dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())){
//            return;
//        }
//
////        if(dayModel.cleaningScheduleModel != null){
////            Util.showToast(getContext(), "Already reserved");
////            return;
////        }
//
//        //기존에 클릭해놓은거 없애기1
//        calendarView.getCurrentMonthView().clear();
//
//        //기존에 클릭해놓은거 없애기 2
//        if(prevDayModel != null){
//            prevDayModel.optBeginDay = false;
//        }
//
//        dayModel.optBeginDay = true;
//        currentMonthGridView.notifyDataSetChanged();
//        prevDayModel = dayModel;
//
//        params.periodStartDate = dayModel.yyyymmdd;
//
//
//
////        dayModel.
////        dayModel.yyyymmdd;
//
////        new OkhomeCleaningTimeChooser(getActivity(), dayModel.year +"-" + dayModel.month + "-" + dayModel.day, new OkhomeCleaningTimeChooser.TimeListener() {
////            @Override
////            public void onTimeChoosed(String time, String timeValue) {
////                if(prevDayModel != null){
////                    if(!time.equals("")){
////                        prevDayModel.optBeginTime = null;
////                    }
////                }
////
////                if(!time.equals("")){
////                    dayModel.optBeginTime = time;
////                    datetime = Util.fillupWithZero(dayModel.year, "XXXX") + "-" + Util.fillupWithZero(dayModel.month, "XX") + "-" + Util.fillupWithZero(dayModel.day, "XX")
////                            + " " + dayModel.optBeginTime;
////                }
////
////                currentMonthGridView.notifyDataSetChanged();
////                prevDayModel = dayModel;
////            }
////        }).show();
//
//    }

    @Override
    public boolean next(MakeCleaningReservationParam params) {
        if(params.periodStartDate.equals("")){
            Util.showToast(getContext(), "청소 시작일을 선택하세요!");
            return false;
        }
        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeCleaningReservationParam params) {
        //좌우로
        this.params = params;
        loadScheduleWithServer(targetYear, targetMonth, calendarView.getCurrentMonthView());
    }


}
