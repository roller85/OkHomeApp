package id.co.okhome.okhomeapp.view.dialog;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.PeriodicCleaningPlanModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.adapter.DayTimeRepeatorCallback;

/**
 * Created by josongmin on 2016-08-09.
 */

public class CleaningScheduleSettingDialog extends ViewDialog{

    @BindView(R.id.dialogCalendarSetting_vLoading)                              View vLoading;
    @BindView(R.id.dialogCalendarSetting_vgNoPeriodParent)                      ViewGroup vgNoPeriod;
    @BindView(R.id.dialogCalendarSetting_vgPeriodParent)                        ViewGroup vgPeriodParent;

    @BindView(R.id.dialogCelendarSetting_vgDays)                                ViewGroup vgDays;
    @BindView(R.id.dialogCelendarSetting_vgDays2Week)                           ViewGroup vgDays2Week;

    @BindView(R.id.dialogCelendarSetting_vgDays2WeekParent)                     ViewGroup vgDays2WeekParent;
    @BindView(R.id.dialogCelendarSetting_vgDaysAWeekParent)                     ViewGroup vgDaysAWeekParent;
    @BindView(R.id.dialogCalendarSetting_vgHoldingOff)                          ViewGroup vgHoldingOff;

    @BindView(R.id.dialogCalendarSetting_ivEveryWeekChk)                        ImageView ivEveryWeekChk;
    @BindView(R.id.dialogCalendarSetting_ivEvery2WeekChk)                       ImageView ivEvery2WeekChk;

    @BindView(R.id.dialogCalendarSetting_vgHoldingTerm)                         ViewGroup vgHoldingTerm;
    @BindView(R.id.dialogCalendarSetting_ivEnableHoldingOffChk)                 ImageView ivEnableHoldingOffChk;
    @BindView(R.id.dialogCalendarSetting_tvHoldingOffEndDate)                   TextView tvHoldingOffEndDate;
    @BindView(R.id.dialogCalendarSetting_tvHoldingOffBeginDate)                 TextView tvHoldingOffBeginDate;
    @BindView(R.id.dialogCalendarSetting_tvPeriodicCleaningStartDate)           TextView tvCleaningStartDate;

    JoViewRepeator repeatorDay, repeatorDay2Week;


    boolean isChkEnableHoldingTerm = false;
    boolean isEveryWeek = false;

    String tobePeriod = null, tobeBeginDate = null, tobeWeekType = null;
    String tobeHoldingOffFrom = null, tobeHoldingOffUntil = null;

    DialogCommonCallback callback;

    public CleaningScheduleSettingDialog(DialogCommonCallback callback) {
        this.callback = callback;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_calendar_setting, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        repeatorDay = new JoViewRepeator<String>(getContext())
                .setContainer(vgDays)
                .setSpanSize(7)
                .setItemLayoutId(R.layout.item_day_time)
                .setCallBack(new DayTimeRepeatorCallback(true));

        List<DayTimeRepeatorCallback.TimeModel> listTimeModel = new ArrayList<>();
        for(int  i = 0; i < 7; i ++){
            listTimeModel.add(new DayTimeRepeatorCallback.TimeModel(""));
        }

        repeatorDay.setList(listTimeModel);
        repeatorDay.notifyDataSetChanged();
        repeatorDay.enableSquare();

        //
        repeatorDay2Week = new JoViewRepeator<String>(getContext())
                .setContainer(vgDays2Week)
                .setSpanSize(7)
                .setItemLayoutId(R.layout.item_day_time)
                .setCallBack(new DayTimeRepeatorCallback(true));

        listTimeModel = new ArrayList<>();
        for(int  i = 0; i < 14; i ++){
            listTimeModel.add(new DayTimeRepeatorCallback.TimeModel(""));
        }

        repeatorDay2Week.setList(listTimeModel);
        repeatorDay2Week.notifyDataSetChanged();
        repeatorDay2Week.enableSquare();

        getMyPeriod();
    }

    private void setDayweeksVisibility(boolean everyweek){
        ivEveryWeekChk.setImageResource(R.drawable.ic_check_not_deep);
        ivEvery2WeekChk.setImageResource(R.drawable.ic_check_not_deep);

        vgDays2WeekParent.setVisibility(View.GONE);
        vgDaysAWeekParent.setVisibility(View.GONE);

        if(everyweek){
            isEveryWeek = true;
            ivEveryWeekChk.setImageResource(R.drawable.ic_checked);
            vgDaysAWeekParent.setVisibility(View.VISIBLE);
        }else{
            isEveryWeek = false;
            ivEvery2WeekChk.setImageResource(R.drawable.ic_checked);
            vgDays2WeekParent.setVisibility(View.VISIBLE);
        }

    }

    //주기일정 가져왔을때
    private void onGettingPeriodSettingSuccess(PeriodicCleaningPlanModel periodModel){

        tobeHoldingOffFrom = periodModel.holdingBeginDate;
        tobeHoldingOffUntil = periodModel.holdingEndDate;
        tobeBeginDate = periodModel.beginDate;
        tobePeriod = periodModel.weekdays;
        tobeWeekType = periodModel.weekType;

        //일정관련 뷰 정리
        List<DayTimeRepeatorCallback.TimeModel> listTimeModel = new ArrayList<>();
        String[] arrDays = periodModel.weekdays.split(",");
        for(String day : arrDays){
            listTimeModel.add(new DayTimeRepeatorCallback.TimeModel(day));
        }

        JoViewRepeator viewRepeator = null;

        if(periodModel.weekType.equals("1W")){
            setDayweeksVisibility(true);
            viewRepeator = repeatorDay;
        }else{
            setDayweeksVisibility(false);
            viewRepeator = repeatorDay2Week;
        }

        tvCleaningStartDate.setText("주기청소 시작일 : " + periodModel.beginDate);

        viewRepeator.setList(listTimeModel);
        viewRepeator.notifyDataSetChanged();

        //홀딩기간 정리.
        //기간이 설정되어있는경우
        if(periodModel.holdingEndDate != null && !periodModel.holdingEndDate.equals("")){
            //텍스트 변경
            setHoldingTermChkItemVisiblity(true);

            tvHoldingOffBeginDate.setText(periodModel.holdingBeginDate);
            tvHoldingOffEndDate.setText(periodModel.holdingEndDate);

        }else{
            //기간설정안되어있음
            tvHoldingOffBeginDate.setText("Today");
            tvHoldingOffEndDate.setText("");
            tvHoldingOffEndDate.setHint("Choose");

            tobeHoldingOffFrom = Util.getCurrentDateSimpleType();


            setHoldingTermChkItemVisiblity(false);
        }
    }

    //주기일정 콜백. 가시상태변경은
    private void onGettingPerioidSetting(boolean success, PeriodicCleaningPlanModel periodModel){
        vgNoPeriod.setVisibility(View.GONE);
        vgPeriodParent.setVisibility(View.GONE);
        if(success){
            vgPeriodParent.setVisibility(View.VISIBLE);
            onGettingPeriodSettingSuccess(periodModel);

        }else{
            vgNoPeriod.setVisibility(View.VISIBLE);
            vgHoldingOff.setVisibility(View.GONE);
        }
    }

    private void getMyPeriod(){
        vLoading.setVisibility(View.VISIBLE);
        RestClient.getCleaningRequestClient().getPeriodicCleaningPlan(CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext())).enqueue(new RetrofitCallback<PeriodicCleaningPlanModel>() {

            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(PeriodicCleaningPlanModel result) {
                onGettingPerioidSetting(true, result);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                onGettingPerioidSetting(false, null);
            }
        });
    }

    //홀딩텀 앙템 가시상태 변경
    private void setHoldingTermChkItemVisiblity(boolean enableHoldingTerm){
        ivEnableHoldingOffChk.setImageResource(R.drawable.ic_check_not_deep);
        vgHoldingTerm.setAlpha(1f);
        if(enableHoldingTerm){

            isChkEnableHoldingTerm = true;
            ivEnableHoldingOffChk.setImageResource(R.drawable.ic_checked);
        }else{
            isChkEnableHoldingTerm = false;
            vgHoldingTerm.setAlpha(0.3f);
        }

    }

    @OnClick({R.id.dialogCalendarSetting_vbtnEveryWeek, R.id.dialogCalendarSetting_vbtnEvery2Week})
    public void onChkClick(View v){
        if(v.getId() == R.id.dialogCalendarSetting_vbtnEveryWeek){
            setDayweeksVisibility(true);
        }else{
            setDayweeksVisibility(false);
        }
    }

    @OnClick({R.id.dialogCalendarSetting_vbtnEnableHoldingOffChk})
    public void onHoldingTermChkClick(View v){
        ivEnableHoldingOffChk.setImageResource(R.drawable.ic_check_not_deep);

        setHoldingTermChkItemVisiblity(!isChkEnableHoldingTerm);

    }

    //홀딩 시작일자
    @OnClick({R.id.dialogCalendarSetting_vbtnHoldingOffBeginDate})
    public void onHoldingOffBeginDate(View v){
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String date = year + "." + Util.fillupWithZero(monthOfYear+1, "00") + "." + Util.fillupWithZero(dayOfMonth, "00");
                tvHoldingOffBeginDate.setText(date);

                tobeHoldingOffFrom = date.replace(".", "-");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    @OnClick({R.id.dialogCalendarSetting_vbtnHoldingOffEndDate})
    public void onHoldingOffEndDate(View v){
        //한달뒤로 기본설정
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "." + Util.fillupWithZero(monthOfYear+1, "00") + "." + Util.fillupWithZero(dayOfMonth, "00");
                tvHoldingOffEndDate.setText(date);

                tobeHoldingOffUntil = date.replace(".", "-");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    @OnClick(R.id.dialogCalendarSetting_vbtnPeriodicCleaningStartDate)
    public void onCleaningStartDate(View v){
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "." + Util.fillupWithZero(monthOfYear+1, "00") + "." + Util.fillupWithZero(dayOfMonth, "00");
                tvCleaningStartDate.setText("주기청소 시작일 : " + date);
                tobeBeginDate = date.replace(".", "-");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onBtnCancel(View v){
        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirm(View v){
        //confirm

        //일정정보
        boolean hasSchedule = false;
        String[] arrTimes = null;
        JoViewRepeator<DayTimeRepeatorCallback.TimeModel> repeator = null;
        if(isEveryWeek){
            arrTimes = new String[7];
            repeator = repeatorDay;
        }else{
            arrTimes = new String[14];
            repeator = repeatorDay2Week;
        }

        for(int i = 0; i < repeator.getList().size(); i++){
            DayTimeRepeatorCallback.TimeModel m = repeator.getList().get(i);
            if(!m.time.equals("")){
                arrTimes[i] = m.time;
                hasSchedule = true;
            }else{
                arrTimes[i] = "X";
            }
        }

        String period = "";
        for(String s : arrTimes){
            period += "," + s;
        }
        period = period.substring(1);

        //여기까지 주기청소부분
        try{
            if(!hasSchedule){
                throw new OkhomeException("Check schedule");
            }

            if(isChkEnableHoldingTerm){
                if(tobeHoldingOffUntil == null || tobeHoldingOffFrom == null){
                    throw new OkhomeException("Check holding off");
                }
            }

        }catch(OkhomeException e){
            Util.showToast(getContext(), e.getMessage());
            return;
        }


        tobePeriod = period;
        tobeWeekType = isEveryWeek ? "1W" : "2W";
        tobeBeginDate = tobeBeginDate;

        //period사용

        String holdingOffBeginDate = isChkEnableHoldingTerm ? tobeHoldingOffFrom : "";
        String holdingOffEndDate = isChkEnableHoldingTerm ? tobeHoldingOffUntil : "";


        final int pId = ProgressDialogController.show(getContext());

        RestClient.getCleaningRequestClient().updatePeriodicCleaning(CurrentUserInfo.getId(getContext()), CurrentUserInfo.getHomeId(getContext())
                , tobeWeekType, tobePeriod, tobeBeginDate, holdingOffBeginDate, holdingOffEndDate).enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
                super.onFinish();
            }

            @Override
            public void onSuccess(String result) {
                Util.showToast(getContext(), "변경완료");
                callback.onCallback(getDialog(), null);
                dismiss();
            }
        });



    }


}
