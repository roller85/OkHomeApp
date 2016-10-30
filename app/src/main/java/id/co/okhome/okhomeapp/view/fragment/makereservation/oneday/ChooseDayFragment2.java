package id.co.okhome.okhomeapp.view.fragment.makereservation.oneday;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CleaningScheduleModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarView;
import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthGridView;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthViewListener;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTimeDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChooseDayFragment2 extends Fragment implements MakeReservationFlow, MonthViewListener {
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

    String datetime = "";

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
                calendarView.initCalendar(targetYear, targetMonth, height, ChooseDayFragment2.this);
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

    //서버에서 스케쥴 불러오기
    public void loadSchedule(int year, int month, final MonthGridView monthGridView) {
        currentYear = year;
        currentMonth = month;
        currentMonthGridView = monthGridView;

        final String sYear = Util.fillupWithZero(year, "0000");
        final String sMonth = Util.fillupWithZero(month, "00");

        String userId = CurrentUserInfo.getId(getContext());
        String homeId = CurrentUserInfo.getHomeId(getContext());

        setLoadingVisibility(true);
        RestClient.getCleaningRequestClient().getMonthlyFilteredSchedule(userId, homeId, sYear + sMonth).enqueue(new RetrofitCallback<List<CleaningScheduleModel>>() {

            @Override
            public void onFinish() {
                super.onFinish();
                setLoadingVisibility(false);
            }

            @Override
            public void onSuccess(final List<CleaningScheduleModel> listResult) {
                if (monthGridView != null) {
                    monthGridView.clear();

                    for(DayModel m : monthGridView.getListDayModels()){
                        m.cleaningScheduleModel = null;
                    }

                    for (CleaningScheduleModel m : listResult) {

                        DayModel dayModel = monthGridView.getDayModel(m.yyyymmdd());

                        if (dayModel != null) {
                            dayModel.cleaningScheduleModel = m;
                        }else{
                        }
                    }
                    //리스트 변경
                    monthGridView.notifyDataSetChanged();
                }else{
                }
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
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
        loadSchedule(year, month, monthGridView);
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

        DialogController.showBottomDialog(getContext(), new ChooseCleaningTimeDialog(dayModel.year+"", dayModel.month+"", dayModel.day+"", dayModel.optBeginTime).setCommonCallback(new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String time = (String)params.get("TIME");


                if(prevDayModel != null){
                    if(!time.equals("")){
                        prevDayModel.optBeginTime = null;
                    }
                }

                if(!time.equals("")){
                    dayModel.optBeginTime = time;
                    datetime = Util.fillupWithZero(dayModel.year, "XXXX") + "-" + Util.fillupWithZero(dayModel.month, "XX") + "-" + Util.fillupWithZero(dayModel.day, "XX")
                            + " " + dayModel.optBeginTime;
                }

                currentMonthGridView.notifyDataSetChanged();
                prevDayModel = dayModel;
                DialogController.dismissDialog(dialog);
            }
        }));
    }

    @Override
    public boolean next(MakeReservationParam params) {
        if(datetime.equals("")){
            Util.showToast(getContext(), "Choose a day for cleaning");

            return false;
        }else{
            params.datetime = datetime;
            return true;
        }
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {

    }
}
