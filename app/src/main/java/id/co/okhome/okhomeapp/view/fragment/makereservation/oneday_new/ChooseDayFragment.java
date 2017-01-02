package id.co.okhome.okhomeapp.view.fragment.makereservation.oneday_new;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkhomeCleaningTimeChooser;
import id.co.okhome.okhomeapp.lib.OkhomeException;
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

public class ChooseDayFragment extends Fragment implements MakeCleaningReservationFlow, MonthViewListener {



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
    boolean isFirst = true;

    String type;
    MakeCleaningReservationParam params;

    Map<String, String> mapChoosedDay = new HashMap<>();

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

        type = getArguments().getString("type");
    }


    Handler handlerDelayed = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            Util.Log("------handlerDelayed");
            if(calendarView != null){
                loadScheduleWithServer(targetYear, targetMonth, calendarView.getCurrentMonthView());
            }
        }
    };

    @Override
    public void onCurrentPage(int pos, MakeCleaningReservationParam params) {
        Util.Log("------onCurrentPage");
        this.params = params;

        if(calendarView == null){
            handlerDelayed.removeMessages(0);
            handlerDelayed.sendEmptyMessageDelayed(0, 400);
        }
        if(calendarView != null){
            loadScheduleWithServer(targetYear, targetMonth, calendarView.getCurrentMonthView());
        }
    }

    @Override
    public boolean next(MakeCleaningReservationParam params) {
        try{
            if(mapChoosedDay.size() <= 0){
                throw new OkhomeException("청소일을 하루 이상 선택하세요");
            }
        }catch(OkhomeException e){
            Util.showToast(getContext(), e.getMessage());
            return false;
        }


        params.mapCleaningDateTime = mapChoosedDay;
        return true;

    }

    public void adaptCurrentMonthViewData(final MonthGridView monthGridView){
        for(DayModel dayModel : monthGridView.getListDayModels()){
            String startTime = mapChoosedDay.get(dayModel.yyyymmdd);
            if(startTime != null){
                dayModel.optBeginTime = startTime;
            }else{
                dayModel.optBeginTime = null;
            }
        }

        monthGridView.notifyDataSetChanged();
    }

    public void loadScheduleWithServer(int year, int month, final MonthGridView monthGridView){
        Util.Log("------loadScheduleWithServer");

        currentYear = year;
        currentMonth = month;
        currentMonthGridView = monthGridView;

        final String yearMonth = Util.fillupWithZero(year, "0000") + Util.fillupWithZero(month, "00");
        setLoadingVisibility(true);

        //서버호출. DayModel에서 3가지 데이터 조작. optBeginDay, cleaningScheduleModel, optAbleReservationForPeriod
        RestClient.getCleaningRequestClient().getMonthlyCleaningSchedule(CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext()), yearMonth).enqueue(new RetrofitCallback<List<CleaningScheduleModel>>() {
            @Override
            public void onSuccess(List<CleaningScheduleModel> listResult) {
                if (monthGridView == null) {
                    return;
                }

                //초기화
                monthGridView.clear("cleaningScheduleModel");

                //예약된 날짜보여주기
                for (CleaningScheduleModel m : listResult) {
                    //내용넣기
                    DayModel dayModel = monthGridView.getDayModel(m.yyyymmdd());
                    if (dayModel != null) {
                        dayModel.cleaningScheduleModel = m;
                    }
                }

                adaptCurrentMonthViewData(currentMonthGridView);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                setLoadingVisibility(false);
            }
        });
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

    public void mesuredViewSize(){
        initCalendar();
    }

    private void initCalendar() {
        final int height = flContent.getHeight();
        flContent.removeAllViews();
        new Thread() {
            @Override
            public void run() {
                calendarView = new CalendarView(flContent.getContext());
                calendarView.setGridAdapterType(CalendarView.GRIDTYPE_CHOOSE_ONEDAY_STARTDAY);
                calendarView.initCalendar(targetYear, targetMonth, height, ChooseDayFragment.this);
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

    private void onCalendarInit(final CalendarView calendarView) {
        Util.Log("------ onCalendarInit");
        this.calendarView = calendarView;
        calendarView.setMonthNavigator(vbtnMonthLeft, vbtnMonthRight, tvYearMonth);

        //이게 붙고난뒤 호출되니깐
        loadScheduleWithServer(currentYear, currentMonth, calendarView.getCurrentMonthView());
    }

    @Override
    public void onMonthSelected(int year, int month, MonthGridView monthGridView) {
        Util.Log("------ onMonthSelected");
        loadScheduleWithServer(year, month, monthGridView);
    }

    @Override
    public void onDayClick(int position, int week, final DayModel dayModel) {
        if(!dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())){
            return;
        }

        if(dayModel.cleaningScheduleModel != null && dayModel.cleaningScheduleModel.status.equals("OK")){
            return;
        }

        //기존에 클릭해놓은거 없애기1
//        calendarView.getCurrentMonthView().clear("optBeginDay");

        //이사청소면 하루만 선택가능
        if(type.equals("MOVEIN")){
            new OkhomeCleaningTimeChooser(getActivity(), new OkhomeCleaningTimeChooser.TimeListener() {
                @Override
                public void onTimeChoosed(String time, String timeValue) {
                    mapChoosedDay.clear();
                    mapChoosedDay.put(dayModel.yyyymmdd, timeValue);
                    //여러개 선택모드
                    adaptCurrentMonthViewData(calendarView.getCurrentMonthView());
                }
            }).show();

        }else{
            if(mapChoosedDay.get(dayModel.yyyymmdd) != null){
                mapChoosedDay.remove(dayModel.yyyymmdd);
                adaptCurrentMonthViewData(calendarView.getCurrentMonthView());
            }else{

                new OkhomeCleaningTimeChooser(getActivity(), new OkhomeCleaningTimeChooser.TimeListener() {
                    @Override
                    public void onTimeChoosed(String time, String timeValue) {
                        mapChoosedDay.put(dayModel.yyyymmdd, timeValue);
                        //여러개 선택모드
                        adaptCurrentMonthViewData(calendarView.getCurrentMonthView());
                    }
                }).show();

            }
        }



//        params.periodStartDate = dayModel.yyyymmdd;

    }
}
