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

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipTextController;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CleaningScheduleModel;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarView;
import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthGridView;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthViewListener;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningGridDialog;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTypeDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;
import id.co.okhome.okhomeapp.view.dialog.PaySpecialCleaningDialog;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;

import static org.joda.time.format.DateTimeFormat.forPattern;


/**
 * Created by josongmin on 2016-07-28.
 */

public class MyCleaningCalendarFragment extends Fragment implements TabFragmentFlow, MonthViewListener, CurrentUserInfo.OnUserInfoChangeListener {

    @BindView(R.id.layerCalendar_rlDays)                                View vDays;

    @BindView(R.id.layerCalendar_vbtnLeft)                              View vbtnMonthLeft;
    @BindView(R.id.layerCalendar_vbtnRight)                             View vbtnMonthRight;
    @BindView(R.id.layerCalendar_tvYearMonth)                           TextView tvYearMonthvbtnMonthRight;
    @BindView(R.id.fragmentMyCalendar_flContents)                       FrameLayout flContent;
    @BindView(R.id.fragmentMyCleaningCalendar_rlPopup)                  View vPopup;
    @BindView(R.id.fragmentMyCleaningCalendar_tvToolTip)                TextView tvToolTip;
    @BindView(R.id.layerCalendar_pbLoading)                             View vLoading;

    //팝업용
    @BindView(R.id.fragmentMyCleaningCalendar_tvReservationType)        TextView tvCleaningType;
    @BindView(R.id.fragmentMyCleaningCalendar_tvPrice)                  TextView tvPrice;
    @BindView(R.id.fragmentMyCleaningCalendar_tvDuration)               TextView tvDuration;
    @BindView(R.id.fragmentMyCleaningCalendar_tvDate)                   TextView tvDate;
    @BindView(R.id.fragmentMyCleaningCalendar_vgExtraCleaningItems)     ViewGroup vgCleaningItems;
    @BindView(R.id.fragmentMyCleaningCalendar_vPreventClick)            View vPreventClick;

    //상단
    @BindView(R.id.fragmentMyCleaningCalendar_tvCleaningTicket)         TextView tvCleaningTicket;
    @BindView(R.id.fragmentMyCleaningCalendar_tvCredit)                 TextView tvCredit;

    //청소이동
    @BindView(R.id.fragmentMyCleaningCalendar_vMoveCleaningInfo)        View vMoveCleaningInfo;

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

    DayModel currentDayModel;
    CleaningScheduleModel cleaningScheduleModel;
    View contentView;
    boolean isMoveMode = false;
    long targetExpiryMill = 0;

    boolean isFirst = true;


    @Override
    public String getTitle() {
        return "캘린더";
    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Util.Log("달력 : " + " onCreateView");
        if(contentView == null){
            contentView = inflater.inflate(R.layout.fragment_mycalendar, container, false);
        }
        return contentView;
    }

    @Override
    public void onUserInfoChanged(UserModel userModel) {
        UserModel um = CurrentUserInfo.get(getActivity());

        tvCleaningTicket.setText("-");
        tvCredit.setText(Util.getMoneyString(um.credit, '.'));
        RestClient.getCleaningClient().getCleaningCountSummary(um.id).enqueue(new RetrofitCallback<Map<String, String>>() {
            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);
                super.onFinish();
            }

            @Override
            public void onSuccess(Map<String, String> result) {
//                String totalCnt = result.get("allTicketCnt").toString();
//                String usedTicketlCnt = result.get("usedTicketCnt").toString();
                String notUsedTicketCnt = result.get("notUsedTicketCnt").toString();

                tvCleaningTicket.setText(notUsedTicketCnt + "개");

            }
        });
    }

    @Override
    public void onStart() {
        Util.Log("달력 : " + " onStart");
        super.onStart();

        if(isFirst){
            ButterKnife.bind(this, getView());

            targetYear = Util.getCurrentYear();
            targetMonth = Util.getCurrentMonth();

            //하단 데이팝업은 안보이는상태로
            vPopup.setVisibility(View.INVISIBLE);
            setLoadingVisibility(false);

            //툴팁 변경 핸들러
            AnimatedTooltipTextController.with(tvToolTip).setArrTooltips(new String[]{"청소권으로 기간내에 자유롭게 일정변경이 가능합니다.", "포인트로 스페셜 청소 신청이 가능합니다"}).start();

            getView().post(new Runnable() {
                @Override
                public void run() {
                    mesuredViewSize();
                }
            });

            isFirst = false;
        }

        CurrentUserInfo.registerUserInfoChangeListener(this);
        getUserInfo();

        initViews();
    }

    private void initViews(){
        vMoveCleaningInfo.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        CurrentUserInfo.unregisterUserInfoChangeListener(this);
    }

    private void getUserInfo(){
        vLoading.setVisibility(View.VISIBLE);
        RestClient.getUserRestClient().getUser(CurrentUserInfo.get(getContext()).id).enqueue(new RetrofitCallback<UserModel>() {

            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);

            }

            @Override
            public void onSuccess(UserModel result) {
                CurrentUserInfo.set(getContext(), result); //invoke함
                onUserInfoChanged(result);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        calendarView = null;
    }

    //서버에서 스케쥴 불러오기

    public void reloadSchedule() {
        loadSchedule(currentYear, currentMonth, currentMonthGridView);
    }

    public void loadSchedule(int year, int month, final MonthGridView monthGridView) {
        currentYear = year;
        currentMonth = month;
        currentMonthGridView = monthGridView;

        final String yearMonth = Util.fillupWithZero(year, "0000") + Util.fillupWithZero(month, "00");
        setLoadingVisibility(true);

        RestClient.getCleaningRequestClient().getMonthlyCleaningSchedule(CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext()), yearMonth).enqueue(new RetrofitCallback<List<CleaningScheduleModel>>() {
            @Override
            public void onSuccess(List<CleaningScheduleModel> listResult) {
                if (monthGridView == null) {
                    return;
                }

                //초기화
                monthGridView.clear();

                //예약된 날짜보여주기
                for (CleaningScheduleModel m : listResult) {
                    //내용넣기
                    DayModel dayModel = monthGridView.getDayModel(m.yyyymmdd());
                    if (dayModel != null) {
                        dayModel.cleaningScheduleModel = m;
                    }
                }

                if(isMoveMode){
                    //순회하면서 expiryDate 아래로는 다 체크함.
                    LocalDateTime localDateTime = forPattern("yyyy-MM-dd").parseLocalDateTime(currentDayModel.cleaningScheduleModel.expiryDate);


                }else{

                }

                monthGridView.notifyDataSetChanged();
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

    //달력로드완료
    private void onCalendarInit(final CalendarView calendarView) {
        calendarView.setMonthNavigator(vbtnMonthLeft, vbtnMonthRight, tvYearMonthvbtnMonthRight);
        new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                calendarView.post(new Runnable() {
                    @Override
                    public void run() {

                        try{
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
                        }catch(Exception e){
                            ;
                        }

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

    //일정변경모드에서 날짜클릭
    public void onDayClickInMoveMode(int position, int week, DayModel dayModel) {
        if(!dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())){
            return;
        }

        if(dayModel.timemill > targetExpiryMill){
            //변경불가능
            return;
        }

        if(dayModel.cleaningScheduleModel != null && dayModel.cleaningScheduleModel == cleaningScheduleModel){
            endMoveCleaning();
            return;
        }

        if(dayModel.cleaningScheduleModel != null && dayModel.cleaningScheduleModel.status.equals("OK") ){
            //변경불가능
            return;
        }



        //변경날짜 yyyy-MM-dd HH:mm:ss

        String time = currentDayModel.cleaningScheduleModel.when.substring(11);
        //변경하기 서버 호출 후 다시 로딩
        String moveDateTime = DateTimeFormat.forPattern("yyyy-MM-dd").print(dayModel.timemill);
        moveDateTime += " " + time;

        final int pId = ProgressDialogController.show(getActivity());
        RestClient.getCleaningClient().moveCleaning(CurrentUserInfo.getId(getContext()), currentDayModel.cleaningScheduleModel.id, moveDateTime).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                currentMonthGridView.notifyDataSetChanged();
                reloadSchedule();
                endMoveCleaning();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogController.dismiss(pId);
            }
        });



    }

    //일반모드에서 날짜클릭
    public void onDayClickInPlainMode(int position, int week, DayModel dayModel) {
        //예약가능날짜에만 팝업띄운다
        if(!dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())){
            return;
        }

        if(dayModel.cleaningScheduleModel != null && dayModel.cleaningScheduleModel.status.equals("OK")){
            moveViewPosition(true, week, dayModel);
            this.cleaningScheduleModel = dayModel.cleaningScheduleModel;
            currentDayModel = dayModel;
            updateScheduleDetailPopup();
        }else{
            DialogController.showBottomDialog(getContext(), new ChooseCleaningTypeDialog(this, false, dayModel.yyyymmdd));
        }
    }

    @Override
    public void onDayClick(int position, int week, DayModel dayModel) {
        //week은 달력의 첫째주면 0임. +1씩..

        if(isMoveMode){
            onDayClickInMoveMode(position, week, dayModel);
            return;
        }else{
            onDayClickInPlainMode(position, week, dayModel);
        }


    }

    //spc변경
    private void requestChangeSpcCleaning(String userId, String cleaningReqId, String spcCleaningIds, final List<SpcCleaningModel> list){

        final int pId = ProgressDialogController.show(getContext());
        RestClient.getCleaningClient().changeSpcCleaning(userId, cleaningReqId, spcCleaningIds).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                cleaningScheduleModel.listExtraCleaning = list;
                updateScheduleDetailPopup();

                getUserInfo();

                Util.showToast(getContext(), "변경되었습니다.");
            }

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }
        });

    }

    //팝업 내용 업데이트
    private void updateScheduleDetailPopup(){

        Map<Integer, View> mapSpcViews = new HashMap<>();
        mapSpcViews.put(SpcCleaningModel.ID_CELLING, vgCleaningItems.getChildAt(0));
        mapSpcViews.put(SpcCleaningModel.ID_WINDOW, vgCleaningItems.getChildAt(1));
        mapSpcViews.put(SpcCleaningModel.ID_BALCONY, vgCleaningItems.getChildAt(2));
        mapSpcViews.put(SpcCleaningModel.ID_PILL, vgCleaningItems.getChildAt(3));
        mapSpcViews.put(SpcCleaningModel.ID_REFRIGERATOR, vgCleaningItems.getChildAt(4));
        mapSpcViews.put(SpcCleaningModel.ID_TIDYUP, vgCleaningItems.getChildAt(5));
        mapSpcViews.put(SpcCleaningModel.ID_BATHROOM, vgCleaningItems.getChildAt(6));
        mapSpcViews.put(SpcCleaningModel.ID_KITCHEN, vgCleaningItems.getChildAt(7));
        mapSpcViews.put(SpcCleaningModel.ID_WALL, vgCleaningItems.getChildAt(8));
        mapSpcViews.put(SpcCleaningModel.ID_VENTILATOR, vgCleaningItems.getChildAt(9));
        Set mapSpcKeySet = mapSpcViews.keySet();


        //시간
        LocalDateTime datetime = forPattern("yyyy-MM-dd HH:mm:ss").parseLocalDateTime(cleaningScheduleModel.when.substring(0, 19));
        String date = forPattern("yyyy.MM.dd(E)").print(datetime);
        String beginTime = forPattern("aa hh:mm").withLocale(Locale.KOREAN).print(datetime);
        int totalPrice = Integer.parseInt(cleaningScheduleModel.basicCleaningPrice);
        float duration = Float.parseFloat(cleaningScheduleModel.duration);

        //스페셜처리
        List<SpcCleaningModel> listExtraCleaningModels =  cleaningScheduleModel.listExtraCleaning;
        if(listExtraCleaningModels != null){
            Map<Integer, SpcCleaningModel> mapSpcTarget = new HashMap<>();
            for(SpcCleaningModel em : listExtraCleaningModels){
                totalPrice += em.price;
                duration += em.hour;
                mapSpcTarget.put(Integer.parseInt(em.id), em);
            }

            //뷰 리스트 갖고오기
            for (Iterator<Integer> iterator = mapSpcKeySet.iterator(); iterator.hasNext();) {
                int spcId = iterator.next();
                View vItem = mapSpcViews.get(spcId);

                if(mapSpcTarget.get(spcId) != null){
                    vItem.setAlpha(1f);
                }else{
                    vItem.setAlpha(0.4f);
                }
            }

        }else{
            //활성화된거 없으니까 전부다 알파
            for (Iterator<Integer> iterator = mapSpcKeySet.iterator(); iterator.hasNext();) {
                int spcKey = iterator.next();
                View vItem = mapSpcViews.get(spcKey);
                vItem.setAlpha(0.4f);
            }
        }
        int iDuration = (int)duration;
        float minute = iDuration % 1 * 60;
        String endTime = forPattern("aa hh:mm").withLocale(Locale.KOREAN).print(datetime.plusHours(iDuration).plusMinutes((int)minute));
        String cleaningType = cleaningScheduleModel.cleaningType.equals("NORMAL") ? "일반청소" : "이사청소";
        //넣기
        tvDate.setText(date);
        tvDuration.setText(beginTime + " ~ " + endTime);
        tvCleaningType.setText(cleaningType + "입니다.");




        tvPrice.setText(Util.getMoneyString(totalPrice, '.'));
    }

    //캘린더 이동 활성화
    private void startMoveCleaning(){

        isMoveMode = true;
//        calendarView.animate().scaleX(1f).scaleY(0.9f).translationYBy(0.9f).setDuration(400).start();
        vMoveCleaningInfo.setVisibility(View.VISIBLE);
        vMoveCleaningInfo.setAlpha(0);
        vMoveCleaningInfo.animate().alpha(1f).setDuration(500).start();


        //하단 팝업 닫아주고
        moveViewPosition(false, 0, null);
        currentDayModel.startMove = true;
        calendarView.putParam("moveMode", true);
        long expiryMill = DateTimeFormat.forPattern("yyyy-MM-dd").parseMillis(currentDayModel.cleaningScheduleModel.expiryDate);
        targetExpiryMill = expiryMill;
        calendarView.putParam("expiryMill", expiryMill);
        calendarView.putParam("expiryDate", currentDayModel.cleaningScheduleModel.expiryDate);
        currentMonthGridView.notifyDataSetChanged();

    }

    //캘린더 이동 끝
    private void endMoveCleaning(){
        isMoveMode = false;
        calendarView.putParam("moveMode", false);
        calendarView.putParam("expiryDate", null);
        calendarView.putParam("expiryMill", null);

        currentDayModel.startMove = false;
        vMoveCleaningInfo.animate().alpha(0f).setDuration(500).start();

        currentMonthGridView.notifyDataSetChanged();
//        calendarView.animate().scaleX(1f).scaleY(1f).translationYBy(1f).setDuration(400).start();
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

                getUserInfo();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);

            }
        };

        RestClient.getCleaningClient().cancelCleaning(CurrentUserInfo.getId(getContext()), cleaningScheduleModel.id).enqueue(callback);

//        if(cleaningScheduleModel.periodicYN.equals("Y")){
//            //이그노어
//            RestClient.getCleaningRequestClient().ignore(
//                    CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext()), cleaningScheduleModel.yyyymmdd()).enqueue(callback);
//        }else{
//            //캔슬
//            RestClient.getCleaningRequestClient().cancel(cleaningScheduleModel.id).enqueue(callback);
//        }
    }


    @OnClick(R.id.fragmentMyCleaningCalendar_vbtnChangeSchedule)
    public void onMoveSchedule(View v){


        if(v.getTag() == null){
            v.setTag(true);
        }

        boolean is = (boolean)v.getTag();
        if(is){
            startMoveCleaning();

        }else{
            endMoveCleaning();
        }

    }

    @OnClick(R.id.fragmentMyCleaningCalendar_vbtnCancelMoveCleaning)
    public void onCancelMoveCleaning(View v){
        endMoveCleaning();
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_vbtnCancel)
    public void onCancelClick(View v){
        //캔슬
        if(cleaningScheduleModel == null){
            Util.showToast(getContext(), "?");
            return;
        }

        String text = "";
        if(cleaningScheduleModel.spcCleaningIds.length() > 0){
            text = "예약을 취소하시겠습니까?\n1)청소권은 반환되어 기간내에 언제든지 재예약이 가능합니다.\n2) 신청하신 스페셜청소는 포인트로 환급됩니다.";
        }else{
            text = "예약을 취소하겠습니까?\n청소권은 반환되어 기간내에 언제든지 재예약이 가능합니다.";
        }

        DialogController.showCenterDialog(getContext(), new CommonTextDialog("Okhome", text, new ViewDialog.DialogCommonCallback() {
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

        DialogController.showChooseExtraCleaningList(getActivity(), 0, cleaningScheduleModel.listExtraCleaning, new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
            @Override
            public void onChoosed(final List<SpcCleaningModel> list) {

                //구매 팝업띄우고 성공하면
                int price = 0;
                int hour = 0;
                String desc  ="";
                String spcCleaningIds = "";
                for(SpcCleaningModel m : list){
                    hour += m.hour;
                    price += m.price;
                    spcCleaningIds += "," + m.id;
                }

                if(spcCleaningIds.length() > 0){
                    spcCleaningIds = spcCleaningIds.substring(1);
                }

                final int fPrice = price;
                final int fHour = hour;
                final String fdesc = "스페셜청소 " + hour + "시간";
                final String fSpcCleaningIds = spcCleaningIds;
                final int pId = ProgressDialogController.show(getContext());

                //가능확인
                RestClient.getCleaningClient().chkAbleToChangeSpcCleaning(CurrentUserInfo.getId(getActivity()), cleaningScheduleModel.id, spcCleaningIds).enqueue(new RetrofitCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer result) {

                        if(result > 0){
                            //
                            DialogController.showCenterDialog(getActivity(), new PaySpecialCleaningDialog(fPrice, fHour, result, fdesc, new PaySpecialCleaningDialog.StatusListener() {
                                @Override
                                public void onClickBuy() {
                                    requestChangeSpcCleaning(CurrentUserInfo.getId(getActivity()), cleaningScheduleModel.id, fSpcCleaningIds, list);
                                }
                            }));

                        }
                        else if(result == 0){
                            if(fSpcCleaningIds.length() <= 0){
                                ;
                            }else{
                                requestChangeSpcCleaning(CurrentUserInfo.getId(getActivity()), cleaningScheduleModel.id, fSpcCleaningIds, list);
                            }
                        }
                        else{
                            //0보다 작으면 환급됨..
                            DialogController.showAlertDialog(getContext(), "Okhome", "스페셜청소 시간이 단축됩니다.\n" + Util.getMoneyString(Math.abs(result), '.') + " 포인트를 환급해드립니다.", true, new ViewDialog.DialogCommonCallback() {
                                @Override
                                public void onCallback(Object dialog, Map<String, Object> params) {
                                    requestChangeSpcCleaning(CurrentUserInfo.getId(getActivity()), cleaningScheduleModel.id, fSpcCleaningIds, list);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFinish() {
                        ProgressDialogController.dismiss(pId);
                    }
                });

                //뷰업데이트
                //수수수수

            }
        });

//        DialogController.showBottomDialog(getContext(), new ChooseCleaningGridDialog(0, listExtraCleaning, new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
//            @Override
//            public void onChoosed(List<SpcCleaningModel> list) {
//
//            }
//        }));

    }

    //캘린더 초기화
    private void initCalendar() {
        final int height = flContent.getHeight();
        flContent.removeAllViews();
        new Thread() {
            @Override
            public void run() {
                calendarView = new CalendarView(flContent.getContext());
                calendarView.setGridAdapterType(CalendarView.GRIDTYPE_NORMAL);
                calendarView.initCalendar(targetYear, targetMonth, height, MyCleaningCalendarFragment.this);
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