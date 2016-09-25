package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipTextController;
import id.co.okhome.okhomeapp.lib.CalendarController;
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
import id.co.okhome.okhomeapp.view.activity.MainActivity;
import id.co.okhome.okhomeapp.view.customview.OkHomeViewPager;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarMonthView;
import id.co.okhome.okhomeapp.view.customview.calendar.dayview.CalendarDayView;
import id.co.okhome.okhomeapp.view.customview.calendar.dayview.CalendarDayViewType1;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayType1Model;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningGridDialog;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTypeDialog;
import id.co.okhome.okhomeapp.view.dialog.CleaningScheduleSettingDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MyCleaningCalendarFragment extends Fragment implements CalendarMonthView.OnDayClickListener<CalendarDayType1Model>, CalendarController.OnCalendarChangeListener{

    @BindView(R.id.fragmentMyCleaningCalendar_incCalendar)              View vCalendar;
    @BindView(R.id.layerCalendar_rlDays)                                View vDays;
    @BindView(R.id.fragmentMyCleaningCalendar_rlPopup)                  View vPopup;
    @BindView(R.id.layerCalendar_vp)                                    OkHomeViewPager vpCalendar;
    @BindView(R.id.fragmentMyCleaningCalendar_tvToolTip)                TextView tvToolTip;

    //팝업용
    @BindView(R.id.fragmentMyCleaningCalendar_tvName)                   TextView tvName;
    @BindView(R.id.fragmentMyCleaningCalendar_tvReservationType)        TextView tvCleaningType;
    @BindView(R.id.fragmentMyCleaningCalendar_tvPrice)                  TextView tvPrice;
    @BindView(R.id.fragmentMyCleaningCalendar_tvDuration)               TextView tvDuration;
    @BindView(R.id.fragmentMyCleaningCalendar_vgExtraCleaningItems)     ViewGroup vgCleaningItems;

    @BindView(R.id.fragmentMyCleaningCalendar_vPreventClick)            View vPreventClick;


    CalendarController calendarController;
    ChooseCleaningGridDialog chooseCleaningGridDialog;

    int calendarDayHeight = 0, calendarHeaderHeight = 0;
    int calendarTop = 0;
    int popupHeight = 0;
    int popupTop = 0;
    final int animationDuration = 300;
    int currentYear = 0;
    int currentMonth = 0;
    String prevYYYYMMDD = ""; //현재 달에 해당하지 않는 날짜 클릭했을때 버퍼용
    boolean calendarLoaded;

    CleaningScheduleModel currentScheduleModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_cleaning_calendar, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        ButterKnife.bind(this, getView());

        //캘린더
        calendarController = CalendarController.with(this)
                .setCalendarType(CalendarMonthView.CalendarType.TYPE1)
                .setOnDayClickListner(this)
                .setOnCalendarChangeListener(this)
                .initCalendar();

        //하단 데이팝업은 안보이는상태로
        vPopup.setVisibility(View.INVISIBLE);

        //툴팁 변경 핸들러
        AnimatedTooltipTextController.with(tvToolTip).setArrTooltips(new String[]{"For changing schedule, Click setting icon", "Click day for making reservation"}).start();

        //세팅 클릭시처리. 메인화면 설정 버튼임.
        if(getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setSettingBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //주기청소 아니면
//                    CurrentUserInfo.get(getContext()).

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
                                        loadSchedule(currentYear, currentMonth);
                                    }
                                })).show();
                            }else{
                                DialogController.showCenterDialog(getContext(), new CommonTextDialog("You are a not periodic cleaning user",
                                        "You need to make periodic cleaning reservation"));
                            }
                        }

                    });

                }
            });
        }

        vPreventClick.setVisibility(View.GONE);
    }

    private void clearCalendar(final List<CleaningScheduleModel> listResultForSkip){

        Map<String, String> mapDateSkip = new HashMap<>();
        for(CleaningScheduleModel m : listResultForSkip){
            String yyyymmdd = m.datetime.substring(0, 10).replace("-", "");
            mapDateSkip.put(yyyymmdd, yyyymmdd);
        }

        Calendar cMonthCursor = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            cMonthCursor.setTime(dateFormat.parse(currentYear + "-" + currentMonth + "-" + "01"));
        }catch(ParseException e){
            return;
        }

        for(int i = 0; i < 42; i++){

            String scheduledTime = "";
            int dayOfWeek = cMonthCursor.get(Calendar.DAY_OF_WEEK)- 1;
            String yyyyMMdd = cMonthCursor.get(Calendar.YEAR) + "-" + Util.getFull2Decimal(cMonthCursor.get(Calendar.MONTH) + 1) + "-" + Util.getFull2Decimal(cMonthCursor.get(Calendar.DAY_OF_MONTH));
            //1주짜리일경우

            try{
                String yyyymmddReduced = yyyyMMdd.substring(0, 10).replace("-", "");
                final CalendarDayViewType1 cDayView = (CalendarDayViewType1)calendarController.getCurrentMonthView().getDayView(yyyymmddReduced);
                if(cDayView.getCleaningReservationModel() != null){
                    if(mapDateSkip.get(yyyymmddReduced) == null){
                        cDayView.setCleaningReservationModel(null);
                        cDayView.refresh();
                    }

                }
            }catch(Exception e){
                ;
                //Util.showToast(getContext(), yyyyMMdd);
            }

            cMonthCursor.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    public void loadSchedule(int year, int month){
//        http://192.168.0.104:3111/cleaning_request/26?homeId=2&yyyymm=201609
//        vLoading.setVisibility(View.VISIBLE);

        calendarController.showLoading();
        String sYear = Util.fillupWithZero(year, "0000");
        String sMonth = Util.fillupWithZero(month, "00");

        String userId = CurrentUserInfo.getId(getContext());
        String homeId = CurrentUserInfo.getHomeId(getContext());

        RestClient.getCleaningRequestClient().getMonthlyFilteredSchedule(userId, homeId, sYear+sMonth).enqueue(new RetrofitCallback<List<CleaningScheduleModel>>() {


            @Override
            public void onSuccess(final List<CleaningScheduleModel> listResult) {
                new Handler(){
                    @Override
                    public void dispatchMessage(Message msg) {
                        clearCalendar(listResult);
                        adaptCalendarWithSchedule(listResult);
                    }
                }.sendEmptyMessageDelayed(0, 200);

            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                calendarController.dismissLoading();
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });
    }

    private void adaptCalendarWithSchedule(final List<CleaningScheduleModel> listResult){
        new Thread(){
            @Override
            public void run() {
                try{
                    for(CleaningScheduleModel m : listResult){
                        String yyyymmdd = m.datetime.substring(0, 10).replace("-", "");
                        final CalendarDayViewType1 cDayView = (CalendarDayViewType1)calendarController.getCurrentMonthView().getDayView(yyyymmdd);

                        cDayView.setCleaningReservationModel(m);

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
    public void onStop() {
        super.onStop();
        AnimatedTooltipTextController.with(tvToolTip).stop();
    }


    @Override
    public void onMonthChange(final int year, final int month) {
        this.currentYear = year;
        this.currentMonth = month;
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
        calendarLoaded = true;

        if(getView() == null) return;

        //달력 로드됨
        getView().post(new Runnable() {
            @Override
            public void run() {
                calendarHeaderHeight = getView().findViewById(R.id.layerCalendar_llTop).getHeight() + getView().findViewById(R.id.layerCalendar_llWeeks).getHeight();
                calendarTop = vCalendar.getTop();
                popupHeight = vPopup.getHeight();
                calendarDayHeight = calendarController.getDayViewSample().getHeight();
                popupTop = vPopup.getTop();
                Log.d("JO", "calendarHeaderHeight : " + calendarHeaderHeight);
                Log.d("JO", "calendarTop : " + calendarTop);
                Log.d("JO", "popupHeight : " + popupHeight);
                Log.d("JO", "calendarDayHeight : " + calendarDayHeight);
                Log.d("JO", "popupTop : " + popupTop);

                moveViewPosition(false, null);
            }
        });
    }


    //팝업 내용 업데이트
    private void updateScheduleDetailPopup(CleaningScheduleModel cleaningScheduleModel){
        //이름
        currentScheduleModel = cleaningScheduleModel;
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
                vgCleaningItems.getChildAt(i).setAlpha(1f);
            }else{
                vgCleaningItems.getChildAt(i).setAlpha(0.5f);
            }
        }


    }

    //팝업 전환. 현재 날짜의 주 위치가 팝업top보다 높으면 캘린더를 위로 땡겨야댐
    private void moveViewPosition(boolean popupShow, Calendar dayCalendar){

        //보기
        if(popupShow){
            vPreventClick.setVisibility(View.VISIBLE);
            int clickedMonth = dayCalendar.get(Calendar.MONTH) + 1;
            int currentTop = 0;

            Util.Log("clickedMonth : " + clickedMonth + ", currentMonth : " + currentMonth + ", dayCalendar.get(Calendar.WEEK_OF_MONTH) : " + dayCalendar.get(Calendar.WEEK_OF_MONTH));
            if(clickedMonth > currentMonth){
                currentTop = ((dayCalendar.get(Calendar.WEEK_OF_MONTH) + 4) * calendarDayHeight ) + calendarTop + calendarHeaderHeight;
            }else{
                currentTop = (dayCalendar.get(Calendar.WEEK_OF_MONTH) * calendarDayHeight) + calendarTop + calendarHeaderHeight;
            }

            if(currentTop > popupTop){
                int diffTop = currentTop - popupTop + 10;
                vDays.animate().translationY(-diffTop).setDuration(animationDuration).start();
            }
            vPopup.animate().translationY(0).setDuration(animationDuration).start();
            vpCalendar.setPagingEnabled(false);
            vPopup.setVisibility(View.VISIBLE);

        }
        //닫기
        else{
            vPreventClick.setVisibility(View.GONE);
            vDays.animate().translationY(0).setDuration(animationDuration).start();
            vPopup.animate().translationY(popupHeight).setDuration(animationDuration).start();
            vpCalendar.setPagingEnabled(true);
        }
    }


    @OnClick(R.id.fragmentMyCleaningCalendar_btnClose)
    public void onBtnPopupClose(View v){
        moveViewPosition(false, null);
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_btnSeeAllSpecialCleaning)
    public void onSeeAllSpecialCleaning(View v){
        List<CleaningModel> listExtraCleaning =  JoSharedPreference.with(getContext()).get("ExtraCleaningList");
//
//        chooseCleaningGridDialog = new ChooseCleaningGridDialog(listExtraCleaning, new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
//            @Override
//            public void onChoosed(List<CleaningModel> list) {
//                String ids = "";
//                for(CleaningModel m : list){
//                    ids += "," + m.id;
//                }
//                ids = ids.substring(1);
//
//                //서버에 날릴거
//                specialCleaningIds = ids;
//                adaptList(list);
//            }
//        });

//        DialogController.showTopDialog(getContext(),
//                new ChooseCleaningDialog()
//                        .setCommonCallback(new ViewDialog.DialogCommonCallback() {
//                            @Override
//                            public void onCallback(Object dialog, Map<String, Object> params) {
//                                boolean isChked = (boolean)params.get("isChked");
//                                int pos = (int)params.get("pos");
//                                CleaningItemModel model = (CleaningItemModel)params.get("CleaningItemModel");
//
//                                if(isChked){
//                                    //리스트에 추가
//                                }else{
//                                }
//                            }
//                        }));

//        DialogController.showBottomDialog(getContext(), new ChooseCleaningGridDialog(new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
//            @Override
//            public void onChoosed(List<CleaningModel> list) {
//
//            }
//        }));
    }

    //예약 취소하기
    private void cancelReservation(){
        if(currentScheduleModel == null){
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
                moveViewPosition(false, null);
                //달력에서 지우기

                final CalendarDayViewType1 cDayView = (CalendarDayViewType1)calendarController.getCurrentMonthView().getDayView(currentScheduleModel.yyyymmdd());
                cDayView.setCleaningReservationModel(null);
                cDayView.refresh();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);

            }
        };

        if(currentScheduleModel.periodicYN.equals("Y")){
            //이그노어
            RestClient.getCleaningRequestClient().ignore(
                    CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext()), currentScheduleModel.yyyymmdd()).enqueue(callback);
        }else{
            //캔슬
            RestClient.getCleaningRequestClient().cancel(currentScheduleModel.id).enqueue(callback);
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
        moveViewPosition(false, null);
        ;
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_llPopupContent)
    public void onContentClick(View v){
        ;
    }

    @Override
    public void onDayClick(List<CalendarDayView> listCalendarDayView, CalendarDayView calendarDayView, CalendarDayType1Model dayModel, String year, String month, String day) {

        int clickedMonth = dayModel.get(Calendar.MONTH) + 1;

        CleaningScheduleModel cleaningScheduleModel = ((CalendarDayViewType1)calendarDayView).getCleaningReservationModel();
        if(cleaningScheduleModel != null){
            moveViewPosition(true, dayModel.cal);
            updateScheduleDetailPopup(cleaningScheduleModel);

        }else{
            final String yyyymmdd = year + month + day;
            //신청화면띄우기..
            DialogController.showBottomDialog(getContext(), new ChooseCleaningTypeDialog(this, false, yyyymmdd));
        }




    }
}
