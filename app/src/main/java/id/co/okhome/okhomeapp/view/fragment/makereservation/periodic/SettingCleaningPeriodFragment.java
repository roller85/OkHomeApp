package id.co.okhome.okhomeapp.view.fragment.makereservation.periodic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoRepeatorCallback;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTimeDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class SettingCleaningPeriodFragment extends Fragment implements MakeReservationFlow{


    @BindView(R.id.fragmentMakeReservationChoosePeriod_vgEvery2Week)               ViewGroup vgEvery2Week;
    @BindView(R.id.fragmentMakeReservationChoosePeriod_vgEveryWeek)                LinearLayout vgEveryWeek;

    @BindView(R.id.fragmentMakeReservationChoosePeriod_vgDaysInEvery2Week)         LinearLayout llEvery2Week;
    @BindView(R.id.fragmentMakeReservationChoosePeriod_vgDaysInEveryWeek)          LinearLayout llEveryWeek;

    @BindView(R.id.fragmentMakeReservationChoosePeriod_ivEvery2WeekChk)            ImageView ivChkEvery2Week;
    @BindView(R.id.fragmentMakeReservationChoosePeriod_ivEveryWeekChk)             ImageView ivChkEveryWeek;

    boolean isEveryWeekChk = true;
    JoViewRepeator<TimeModel> repeatorEveryweek, repeatorEvery2week;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_period, null);
    }

    @Override
    public boolean next(MakeReservationParam params) {

        boolean hasSchedule = false;
        String[] arrTimes = null;
        JoViewRepeator<TimeModel> repeator = null;
        if(isEveryWeekChk){
            arrTimes = new String[7];
            repeator = repeatorEveryweek;
        }else{
            arrTimes = new String[14];
            repeator = repeatorEvery2week;
        }

        for(int i = 0; i < repeator.getList().size(); i++){
            TimeModel m = repeator.getList().get(i);
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

        try{
            if(!hasSchedule){
                throw new OkhomeException("Check schedule");
            }
        }catch(OkhomeException e){
            Util.showToast(getContext(), e.getMessage());
            return false;
        }


        params.type = MakeReservationParam.TYPE_PERIODIC_CLEANING;
        params.period = period;
        params.weekType = isEveryWeekChk ? "1W" : "2W";

        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {
        ;
    }



    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        setChkItems();
        isEveryWeekChk = true;

        vgEvery2Week.setVisibility(View.INVISIBLE);
        vgEveryWeek.setVisibility(View.INVISIBLE);

        calcViewSize();

    }

    private void setChkItems(){
        repeatorEveryweek = new JoViewRepeator<TimeModel>(getContext())
                .setContainer(llEveryWeek)
                .setItemLayoutId(R.layout.item_day_time)
                .setSpanSize(7)
                .setCallBack(new DayTimeRepeatorCallback());

        repeatorEvery2week = new JoViewRepeator<TimeModel>(getContext())
                .setContainer(llEvery2Week)
                .setItemLayoutId(R.layout.item_day_time)
                .setSpanSize(7)
                .setCallBack(new DayTimeRepeatorCallback());

        //1번
        List<TimeModel> listTimeModel = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            listTimeModel.add(new TimeModel(""));
        }
        repeatorEveryweek.setList(listTimeModel);
        repeatorEveryweek.notifyDataSetChanged();
        repeatorEveryweek.enableSquare();

        //2번
        List<TimeModel> listTimeModel2 = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            listTimeModel2.add(new TimeModel(""));
        }
        repeatorEvery2week.setList(listTimeModel2);
        repeatorEvery2week.notifyDataSetChanged();
        repeatorEvery2week.enableSquare();

    }

    private void calcViewSize(){
        getView().post(new Runnable() {
            @Override
            public void run() {
                onFinishCalcViewSize();
            }
        });
    }

    private void onFinishCalcViewSize(){
        adaptDayCheck();
    }

    //체크아이템들 변경적용
    private void adaptDayCheck(){
        ivChkEveryWeek.setImageResource(R.drawable.ic_check_not_deep);
        ivChkEvery2Week.setImageResource(R.drawable.ic_check_not_deep);

        vgEveryWeek.setVisibility(View.GONE);
        vgEvery2Week.setVisibility(View.GONE);

        if(isEveryWeekChk) {
            ivChkEveryWeek.setImageResource(R.drawable.ic_checked);
            vgEveryWeek.setVisibility(View.VISIBLE);
        }else {
            ivChkEvery2Week.setImageResource(R.drawable.ic_checked);
            vgEvery2Week.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.fragmentMakeReservationChoosePeriod_vbtnEvery2Week, R.id.fragmentMakeReservationChoosePeriod_vbtnEveryWeek})
    public void onClick(View v){

        switch(v.getId()){
            case R.id.fragmentMakeReservationChoosePeriod_vbtnEveryWeek:
                isEveryWeekChk = true;
                break;

            case R.id.fragmentMakeReservationChoosePeriod_vbtnEvery2Week:
                isEveryWeekChk = false;
                break;
        }
        adaptDayCheck();

    }

    class DayTimeRepeatorCallback extends JoRepeatorCallback<TimeModel>{

        @BindView(R.id.itemDayTime_tvTime)      TextView tvTime;
        @BindView(R.id.itemDayTime_vgFrame)     ViewGroup vgFrame;

        @Override
        public void onBind(View v, final TimeModel model) {
            ButterKnife.bind(this, v);

            tvTime.setText(model.time);
            if(model.time.equals("")){
                vgFrame.setBackgroundResource(R.drawable.bg_inputbox);
            }else{
                vgFrame.setBackgroundResource(R.drawable.bg_inputbox_focus);
            }

            //클릭시 팝업
            vgFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogController.showBottomDialog(getContext(), new ChooseCleaningTimeDialog("", "", "", model.time).setCommonCallback(new ViewDialog.DialogCommonCallback() {
                        @Override
                        public void onCallback(Object dialog, Map<String, Object> params) {
                            String time = (String) params.get("TIME");
                            model.time = time;


                            getViewRepeator().notifyDataChange(model);
                            DialogController.dismissDialog(dialog);

                        }
                    }), new OnCancelListener() {
                        @Override
                        public void onCancel(DialogPlus dialog) {
                            if(model.time.equals("")){

                            }
                        }
                    });
                }
            });
        }

    }


//
//
//    class DayChooser extends JoChoiceViewController<TimeModel> {
//
//        int lastChoosedItemPos = 0;
//
//        public DayChooser(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
//            super(context, vgContent, multiChoice, spanSize);
//        }
//
//        class ViewHolder{
//            @BindView(R.id.itemDayTime_tvTime)  TextView tvTime;
//            @BindView(R.id.itemDayTime_vgFrame) ViewGroup vgFrame;
//
//            public ViewHolder(View v) {
//                ButterKnife.bind(this, v);
//            }
//        }
//
//        public int getLastChoosedItemPos() {
//            return lastChoosedItemPos;
//        }
//
//        @Override
//        public View getItemView(LayoutInflater inflater, TimeModel model, int pos) {
//            View vItem = inflater.inflate(R.layout.item_day_time, null);
//            ViewHolder h = (ViewHolder)vItem.getTag();
//            if(h == null){
//                h = new ViewHolder(vItem);
//                vItem.setTag(h);
//            }
//
//            onItemCheckChanged(vItem, model, false, pos);
//            return vItem;
//        }
//
//        @Override
//        public void onItemCheckChanged(View vItem, final TimeModel model, final boolean checked, final int pos) {
//            ViewHolder h = (ViewHolder)vItem.getTag();
//
//            if(checked){
//                h.vgFrame.setBackgroundResource(R.drawable.bg_inputbox_focus);
//                h.tvTime.setText(model.time);
//
//                lastChoosedItemPos = pos;
//            }else{
//                h.vgFrame.setBackgroundResource(R.drawable.bg_inputbox);
//                h.tvTime.setText("");
//            }
//
//            //팝업
//            DialogController.showBottomDialog(getContext(), new ChooseCleaningTimeDialog("", "", "", model.time).setCommonCallback(new ViewDialog.DialogCommonCallback() {
//                @Override
//                public void onCallback(Object dialog, Map<String, Object> params) {
//                    String time = (String) params.get("TIME");
//                    model.time = time;
//
//                    setChecked(pos, true);
//                    DialogController.dismissDialog(dialog);
//                }
//            }), new OnCancelListener() {
//                @Override
//                public void onCancel(DialogPlus dialog) {
//                    if(model.time.equals("")){
//                        setChecked(pos, false);
//                    }
//                }
//            });
//        }
//    }


    class TimeModel{
        public String time;

        public TimeModel(String time) {
            this.time = time;
        }
    }
}
