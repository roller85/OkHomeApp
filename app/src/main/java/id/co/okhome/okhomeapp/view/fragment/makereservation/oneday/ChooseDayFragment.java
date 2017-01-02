package id.co.okhome.okhomeapp.view.fragment.makereservation.oneday;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.CalendarController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.model.CleaningScheduleModel;
import id.co.okhome.okhomeapp.view.customview.calendar_old.CalendarMonthView;
import id.co.okhome.okhomeapp.view.customview.calendar_old.dayview.CalendarDayView;
import id.co.okhome.okhomeapp.view.customview.calendar_old.dayview.CalendarDayViewType1;
import id.co.okhome.okhomeapp.view.customview.calendar_old.model.CalendarDayType1Model;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTimeDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChooseDayFragment extends Fragment implements CalendarMonthView.OnDayClickListener<CalendarDayType1Model>, CalendarController.OnCalendarChangeListener, MakeReservationFlow{

    @BindView(R.id.layerCalendar_pbLoading)
    View vLoading;

    CalendarDayView dayViewPrev = null;
    String datetime = "";
    CalendarController calendarController;
    boolean calendarLoaded = false;

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

    public void loadSchedule(int year, int month){
//        http://192.168.0.104:3111/cleaning_request/26?homeId=2&yyyymm=201609
//        vLoading.setVisibility(View.VISIBLE);

        calendarController.showLoading();
        String sYear = Util.fillupWithZero(year, "0000");
        String sMonth = Util.fillupWithZero(month, "00");

        String userId = CurrentUserInfo.getId(getContext());
        String homeId = CurrentUserInfo.getHomeId(getContext());

//        RestClient.getCleaningRequestClient().getMonthlyFilteredSchedule(userId, homeId, sYear+sMonth).enqueue(new RetrofitCallback<List<CleaningScheduleModel>>() {
//
//
//            @Override
//            public void onSuccess(final List<CleaningScheduleModel> listResult) {
//                new Handler(){
//                    @Override
//                    public void dispatchMessage(Message msg) {
//                        adaptCalendarWithSchedule(listResult);
//                    }
//                }.sendEmptyMessageDelayed(0, 200);
//
//
//            }
//
//            @Override
//            public void onJodevError(ErrorModel jodevErrorModel) {
//                calendarController.dismissLoading();
//                Util.showToast(getContext(), jodevErrorModel.message);
//            }
//        });
    }

    private void adaptCalendarWithSchedule(final List<CleaningScheduleModel> listResult){
//        new Thread(){
//            @Override
//            public void run() {
//                try{
//                    for(CleaningScheduleModel m : listResult){
//                        String yyyymmdd = m.datetime.substring(0, 10).replace("-", "");
//                        final CalendarDayViewType1 cDayView = (CalendarDayViewType1)calendarController.getCurrentMonthView().getDayView(yyyymmdd);
//
//                        cDayView.setCleaningReservationModel(m);
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                cDayView.refresh();
//                            }
//                        });
//
//                    }
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            calendarController.dismissLoading();
//                        }
//                    });
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {

    }

    @Override
    public boolean next(MakeReservationParam params) {
        if(datetime.equals("")){
            Util.showToast(getContext(), "Choose a day for cleaning");

            return false;
        }else{
//            params.datetime = datetime;
            return true;
        }

    }

    @Override
    public void onMonthChange(final int year, final int month) {
        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                if(calendarLoaded == true){
                    loadSchedule(year, month);
                }else{
                    sendEmptyMessageDelayed(0, 300);
                }
            }
        }.sendEmptyMessageDelayed(0, 0);

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


        DialogController.showBottomDialog(getContext(), new ChooseCleaningTimeDialog(year, month, day, dayModel.time).setCommonCallback(new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String time = (String)params.get("TIME");

                if(dayViewPrev != null){
                    if(!time.equals("")){
                        ((CalendarDayType1Model)dayViewPrev.getDayModel()).time = null;
                        dayViewPrev.refresh();
                    }
                }

                for(CalendarDayView dv : listCalendarDayView){
                    if(dv.getDayModel() == dayModel){
                        ((CalendarDayType1Model)dv.getDayModel()).time = time;
                        dayViewPrev = dv;
                        dv.refresh();
                        break;
                    }else{
//                        ((CalendarDayType1Model)dv.getDayModel()).time = null;
                    }
                }

                if(!time.equals("")){
                    datetime = year + "-" + month + "-" + day + " " + dayModel.time;
                }
                DialogController.dismissDialog(dialog);
            }
        }));


    }
}
