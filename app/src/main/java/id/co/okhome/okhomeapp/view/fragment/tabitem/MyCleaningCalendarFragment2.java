package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipTextController;
import id.co.okhome.okhomeapp.lib.JoSharedPreference;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CleaningModel;
import id.co.okhome.okhomeapp.model.CleaningScheduleModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarView;
import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthGridView;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthViewListener;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningGridDialog;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTypeDialog;
import id.co.okhome.okhomeapp.view.dialog.CleaningScheduleSettingDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;


/**
 * Created by josongmin on 2016-07-28.
 */

public class MyCleaningCalendarFragment2 extends Fragment implements TabFragmentFlow, MonthViewListener {

    @BindView(R.id.layerCalendar_rlDays)                                View vDays;

    @BindView(R.id.layerCalendar_vbtnLeft)                              View vbtnMonthLeft;
    @BindView(R.id.layerCalendar_vbtnRight)                             View vbtnMonthRight;
    @BindView(R.id.layerCalendar_tvYearMonth)                           TextView tvYearMonthvbtnMonthRight;
    @BindView(R.id.fragmentMyCalendar_flContents)                       FrameLayout flContent;
    @BindView(R.id.fragmentMyCleaningCalendar_rlPopup)                  View vPopup;
    @BindView(R.id.fragmentMyCleaningCalendar_tvToolTip)                TextView tvToolTip;
    @BindView(R.id.layerCalendar_pbLoading)                             View vLoading;

    //팝업용
    @BindView(R.id.fragmentMyCleaningCalendar_tvName)                   TextView tvName;
    @BindView(R.id.fragmentMyCleaningCalendar_tvReservationType)        TextView tvCleaningType;
    @BindView(R.id.fragmentMyCleaningCalendar_tvPrice)                  TextView tvPrice;
    @BindView(R.id.fragmentMyCleaningCalendar_tvDuration)               TextView tvDuration;
    @BindView(R.id.fragmentMyCleaningCalendar_vgExtraCleaningItems)     ViewGroup vgCleaningItems;
    @BindView(R.id.fragmentMyCleaningCalendar_vPreventClick)            View vPreventClick;

    CalendarView calendarView;
    int targetYear, targetMonth;

    //캘린더 높이
    int calendarDayHeight = 0, calendarHeaderHeight = 0;
    int calendarTop = 0;
    int popupHeight = 0;
    int popupTop = 0;
    int dayItemHeight = 0;
    final int animationDuration = 300;

    int currentYear, currentMonth;
    MonthGridView currentMonthGridView;

    CleaningScheduleModel cleaningScheduleModel;

    @Override
    public String getTitle() {
        return "Schedule";
    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {


        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final int pId = ProgressDialogController.show(getContext());
                RestClient.getUserRestClient().getUserFieldValue(CurrentUserInfo.getId(getContext()), "periodicCleaningYn").enqueue(new RetrofitCallback<String>() {

                    @Override
                    public void onFinish() {
                        ProgressDialogController.dismiss(pId);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if(result.equals("Y")){
                            DialogController.showCenterDialog(getContext(), new CleaningScheduleSettingDialog(new ViewDialog.DialogCommonCallback() {
                                @Override
                                public void onCallback(Object dialog, Map<String, Object> params) {
                                    //다시불러오기
                                    loadSchedule(currentYear, currentMonth, currentMonthGridView);
                                }
                            })).show();
                        }else{
                            DialogController.showCenterDialog(getContext(), new CommonTextDialog("You are a not periodic cleaning user",
                                    "You need to make periodic cleaning reservation"));
                        }
                    }

                });
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mycalendar_2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        targetYear = Util.getCurrentYear();
        targetMonth = Util.getCurrentMonth();

        //하단 데이팝업은 안보이는상태로
        vPopup.setVisibility(View.INVISIBLE);
        setLoadingVisibility(false);

        //툴팁 변경 핸들러
        AnimatedTooltipTextController.with(tvToolTip).setArrTooltips(new String[]{"For changing schedule, Click setting icon", "Click day for making reservation"}).start();

        getView().post(new Runnable() {
            @Override
            public void run() {
                mesuredViewSize();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        calendarView = null;
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

    //달력로드완료
    private void onCalendarInit(final CalendarView calendarView) {
        calendarView.setMonthNavigator(vbtnMonthLeft, vbtnMonthRight, tvYearMonthvbtnMonthRight);
        new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                calendarView.post(new Runnable() {
                    @Override
                    public void run() {
                        calendarView.getAdapter().onPageSelected(calendarView.getCurrentItem());

                        calendarHeaderHeight = getView().findViewById(R.id.layerCalendar_llTop).getHeight() + getView().findViewById(R.id.layerCalendar_llWeeks).getHeight();
                        calendarTop = getView().findViewById(R.id.layerCalendar_llTop).getTop();
                        popupHeight = vPopup.getHeight();
                        calendarDayHeight = flContent.getHeight() / 6;
                        popupTop = vPopup.getTop();
                        Log.d("JO", "calendarHeaderHeight : " + calendarHeaderHeight);
                        Log.d("JO", "calendarTop : " + calendarTop);
                        Log.d("JO", "popupHeight : " + popupHeight);
                        Log.d("JO", "calendarDayHeight : " + calendarDayHeight);
                        Log.d("JO", "popupTop : " + popupTop);

                        moveViewPosition(false, 0, null);
                    }
                });

            }
        }.sendEmptyMessageDelayed(0, 400);

    }

    //월 선택됨
    @Override
    public void onMonthSelected(int year, int month, MonthGridView monthGridView) {
        loadSchedule(year, month, monthGridView);
    }

    @Override
    public void onDayClick(int position, int week, DayModel dayModel) {
        //week은 달력의 첫째주면 0임. +1씩..


        //예약가능날짜에만 팝업띄운다
        if(!dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())){
            return;
        }

        if(dayModel.cleaningScheduleModel != null){
            moveViewPosition(true, week, dayModel);
            updateScheduleDetailPopup(dayModel.cleaningScheduleModel);
        }else{
            DialogController.showBottomDialog(getContext(), new ChooseCleaningTypeDialog(this, false, dayModel.yyyymmdd));
        }
    }

    //팝업 내용 업데이트
    private void updateScheduleDetailPopup(CleaningScheduleModel cleaningScheduleModel){
        //이름
        this.cleaningScheduleModel = cleaningScheduleModel;
        tvName.setText("Hello " + CurrentUserInfo.get(getContext()).name);

        //예약상태
        if(cleaningScheduleModel.periodicYN.equals("Y")){
            tvCleaningType.setText("Periodical Cleaning");
        }else{
            if(cleaningScheduleModel.cleaningType.equals("1")){
                //일반
                tvCleaningType.setText("Basic cleaning reservation");
            }else if(cleaningScheduleModel.cleaningType.equals("2")){
                //이사청소
                tvCleaningType.setText("Move in cleaning reservation");
            }
        }

        //시간
        tvDuration.setText(cleaningScheduleModel.datetime);
        tvPrice.setText(Util.getMoneyString(cleaningScheduleModel.price, '.'));

        //엑스트라
        String[] arrExtraIds = cleaningScheduleModel.extraCleaningIds.split(",");
        Map<String, String> mapExtraIds = new HashMap<>();
        for(String extraId : arrExtraIds){
            mapExtraIds.put(extraId, extraId);
        }
        for(int i = 0 ; i < vgCleaningItems.getChildCount(); i++){
            vgCleaningItems.getChildAt(i).setAlpha(1f);
            if(mapExtraIds.get((i+1)+"") != null){
//                vgCleaningItems.getChildAt(i).setAlpha(1f);
            }else{
//                vgCleaningItems.getChildAt(i).setAlpha(0.3f);
            }
        }
    }

    //예약 취소하기
    private void cancelReservation(){
        if(cleaningScheduleModel == null){
            Util.showToast(getContext(), "?");
            return;
        }

        final int pid = ProgressDialogController.show(getContext());
        //캔슬 콜백
        final RetrofitCallback callback = new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pid);
            }

            @Override
            public void onSuccess(String result) {
                moveViewPosition(false, 0, null);
                //달력에서 지우기

//                cleaningScheduleModel.
                calendarView.getCurrentMonthView().getDayModel(cleaningScheduleModel.yyyymmdd()).cleaningScheduleModel = null;
                calendarView.getCurrentMonthView().notifyDataSetChanged();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);

            }
        };

        if(cleaningScheduleModel.periodicYN.equals("Y")){
            //이그노어
            RestClient.getCleaningRequestClient().ignore(
                    CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext()), cleaningScheduleModel.yyyymmdd()).enqueue(callback);
        }else{
            //캔슬
            RestClient.getCleaningRequestClient().cancel(cleaningScheduleModel.id).enqueue(callback);
        }
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_vbtnCancel)
    public void onCancelClick(View v){
        //캔슬

        DialogController.showCenterDialog(getContext(), new CommonTextDialog("Really cancel?", "Your cleaning reservation will be canceled", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String onClick = (String)params.get("ONCLICK");

                if(onClick.equals("OK")){
                    cancelReservation();
                }
            }
        }));
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_vPreventClick)
    public void onPreventClick(View v){
        moveViewPosition(false, 0, null);
        ;
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_llPopupContent)
    public void onContentClick(View v){
        ;
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_btnSeeAllSpecialCleaning)
    public void onSeeAllSpecialCleaning(View v){
        List<CleaningModel> listExtraCleaning =  JoSharedPreference.with(getContext()).get("ExtraCleaningList");

        DialogController.showBottomDialog(getContext(), new ChooseCleaningGridDialog(0, listExtraCleaning, new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
            @Override
            public void onChoosed(List<CleaningModel> list) {

            }
        }));

    }

    //캘린더 초기화
    private void initCalendar() {
        final int height = flContent.getHeight();
        flContent.removeAllViews();
        new Thread() {
            @Override
            public void run() {
                calendarView = new CalendarView(flContent.getContext());
                calendarView.initCalendar(targetYear, targetMonth, height, MyCleaningCalendarFragment2.this);
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

    public void mesuredViewSize(){
        initCalendar();
    }

    //하단 팝업 보이기
    //팝업 전환. 현재 날짜의 주 위치가 팝업top보다 높으면 캘린더를 위로 땡겨야댐
    private void moveViewPosition(boolean popupShow, int week, DayModel dayModel){

        //보기
        if(popupShow){
            int currentTop = 0;
            vPreventClick.setVisibility(View.VISIBLE);

            currentTop = (week+1) * calendarDayHeight + calendarTop + calendarHeaderHeight;


            if(currentTop > popupTop){
                int diffTop = currentTop - popupTop + 10;
                vDays.animate().translationY(-diffTop).setDuration(animationDuration).start();
            }
            vPopup.animate().translationY(0).setDuration(animationDuration).start();
//            vpCalendar.setPagingEnabled(false);
            vPopup.setVisibility(View.VISIBLE);

        }
        //닫기
        else{
            vPreventClick.setVisibility(View.GONE);
            vDays.animate().translationY(0).setDuration(animationDuration).start();
            vPopup.animate().translationY(popupHeight).setDuration(animationDuration).start();
//            vpCalendar.setPagingEnabled(true);
        }
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_btnClose)
    public void onBtnPopupClose(View v){
        moveViewPosition(false, 0, null);
    }
}