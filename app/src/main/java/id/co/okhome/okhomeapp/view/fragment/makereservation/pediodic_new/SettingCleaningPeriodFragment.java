package id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoChoiceViewController;
import id.co.okhome.okhomeapp.lib.OkhomeCleaningTimeChooser;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.ViewHolderUtil;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoRepeatorCallback;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class SettingCleaningPeriodFragment extends Fragment implements MakeCleaningReservationFlow {


    @BindView(R.id.fragmentMakeReservationChoosePeriod_vgItems)                     ViewGroup vgItems;
    @BindView(R.id.fragmentMakeReservationChoosePeriod_vLoading)                    View vLoading;

    ItemAdapter itemAdapter;

    MakeCleaningReservationParam params;
    boolean isFirst = true;
    int itemWidth = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_period, null);
    }

    @Override
    public boolean next(MakeCleaningReservationParam params) {

        //예외처리
        try{
            //주기 선택되어있는지
            if(params.periodType.equals("")){
                throw new OkhomeException("주기를 선택하세요");
            }

            ItemModel item = itemAdapter.getCheckedItemList().get(0);

            //여러개 선택한 일정이면 스케쥴 선택되었는지 체크
            boolean hasSchedule = false;
            String[] arrTimes = new String[7];

            for(int i = 0; i < item.joViewRepeator.getList().size(); i++){
                TimeModel m = (TimeModel)item.joViewRepeator.getList().get(i);
                if(!m.time.equals("")){
                    arrTimes[i] = m.timeValue;
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
            params.periodValue = period;

            if(!hasSchedule){
                throw new OkhomeException("어떤 요일에 청소를 시작할지 선택해주세요");
            }

        }catch(OkhomeException e){
            Util.showToast(getActivity(), e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeCleaningReservationParam params) {
        Util.Log("onCurrentPage" + pos);
        this.params = params;
        if(isFirst){
            isFirst= false;
            adaptItemRepeator();

            vLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

    }

    //주기설정 아이템 초기화
    private void adaptItemRepeator(){

        itemWidth = vgItems.getWidth() / 7;
        List<ItemModel> listItems = new ArrayList<>();
        listItems.add(new ItemModel(MakeCleaningReservationParam.PERIODIC_DAYS_WEEK, "일주일마다 여러번", true));
        listItems.add(new ItemModel(MakeCleaningReservationParam.PERIODIC_1DAY_WEEK, "일주일마다 한번", false));
        listItems.add(new ItemModel(MakeCleaningReservationParam.PERIODIC_1DAY_2WEEK, "2주에 한번", false));
        listItems.add(new ItemModel(MakeCleaningReservationParam.PERIODIC_1DAY_4WEEK, "한달에 한번", false));


        itemAdapter = new ItemAdapter(getContext(), vgItems, false, 1);
        itemAdapter.setList(listItems);
        itemAdapter.build();

    }



    class DayTimeRepeatorCallback extends JoRepeatorCallback<TimeModel>{

        @BindView(R.id.itemDayTime_tvTime)      TextView tvTime;
        @BindView(R.id.itemDayTime_vgFrame)     ViewGroup vgFrame;

        boolean multi = true;
        public DayTimeRepeatorCallback(boolean multi) {
            this.multi = multi;
        }

        @Override
        public void onBind(View v, final TimeModel model) {
            ButterKnife.bind(this, v);

            tvTime.setText(model.time);
            if(model.time.equals("")){
                vgFrame.setBackgroundResource(R.drawable.bg_inputbox);
            }else{
                vgFrame.setBackgroundColor(getResources().getColor(R.color.colorAppPrimary));
                tvTime.setTextColor(Color.parseColor("#ffffff"));
            }


            //클릭시 팝업
            vgFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(model.time.equals("")){
                        new OkhomeCleaningTimeChooser(getActivity(), new OkhomeCleaningTimeChooser.TimeListener() {
                            @Override
                            public void onTimeChoosed(String time, String timeValue) {

                                //여러개 선택모드
                                if(multi){
                                    model.time = time;
                                    model.timeValue = timeValue;
                                    getViewRepeator().notifyDataChange(model);
                                }

                                //하나만 선택모드
                                else{
                                    //자기꺼뺴고 전부다 클리어
                                    for(TimeModel timeModel : (List<TimeModel>)getViewRepeator().getList()){
                                        if(timeModel == model){
                                            model.time = time;
                                            model.timeValue = timeValue;
                                        }else{
                                            timeModel.time = "";
                                            model.timeValue = "";
                                        }
                                    }
                                    getViewRepeator().notifyAllDataChange();
                                }
                            }
                        }).show();
                    }else{
                        model.time = "";
                        model.timeValue = "";
                        getViewRepeator().notifyDataChange(model);
                    }
                }
            });


        }

    }

    //아이템 반복자
    class ItemAdapter extends JoChoiceViewController<ItemModel> {
        public ItemAdapter(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
            super(context, vgContent, multiChoice, spanSize);
        }

        @Override
        public View getItemView(LayoutInflater inflater, ItemModel model, int pos) {
            View vItem = inflater.inflate(R.layout.item_cleaning_period, null);
            TextView tvTitle = ViewHolderUtil.getView(vItem, R.id.itemCleaningPeriod_tvTitle);
            ViewGroup vgDayContents = ViewHolderUtil.getView(vItem, R.id.itemCleaningPeriod_vgDayContents);
            ViewGroup vgSubContents = ViewHolderUtil.getView(vItem, R.id.itemCleaningPeriod_vgItem);
            tvTitle.setText(model.title);
            vgSubContents.setVisibility(View.GONE);


            initDayChooser(vgDayContents, model);

            return vItem;
        }

        @Override
        public void onItemCheckChanged(View vItem, ItemModel model, boolean checked, int pos) {
            ImageView ivChk = ViewHolderUtil.getView(vItem, R.id.itemCleaningPeriod_ivChk);
            ViewGroup vgDays = ViewHolderUtil.getView(vItem, R.id.itemCleaningPeriod_vgItem);
            ViewGroup vgSubContents = ViewHolderUtil.getView(vItem, R.id.itemCleaningPeriod_vgItem);

            if(checked){
                params.periodType = model.type;

                ivChk.setImageResource(R.drawable.ic_checked);
                vgDays.setVisibility(View.VISIBLE);
                vgSubContents.setVisibility(View.VISIBLE);
            }else{
                ivChk.setImageResource(R.drawable.ic_check_not_deep);
                vgDays.setVisibility(View.GONE);
                vgSubContents.setVisibility(View.GONE);
            }
            model.isChk = checked;
        }

        //데이선택지
        private void initDayChooser(ViewGroup container, ItemModel model){
            boolean multiOk = model.isMulti;

            JoViewRepeator joViewRepeator = new JoViewRepeator<TimeModel>(getContext())
                    .setContainer(container)
                    .setItemLayoutId(R.layout.item_day_time)
                    .setSpanSize(7)
                    .setCallBack(new DayTimeRepeatorCallback(multiOk));
            model.joViewRepeator = joViewRepeator;

            //1번
            List<TimeModel> listTimeModel = new ArrayList<>();
            for(int i = 0; i < 7; i++){
                listTimeModel.add(new TimeModel("", ""));
            }
            joViewRepeator.setList(listTimeModel);
            joViewRepeator.notifyDataSetChanged();
            joViewRepeator.enableSquare(itemWidth);
        }


    }


    class TimeModel{
        public String time, timeValue;
        public TimeModel(String time, String timeValue) {
            this.time = time; this.timeValue = timeValue;
        }
    }

    class ItemModel{
        public boolean isChk = false;
        public String title ="";
        public String type;
        JoViewRepeator joViewRepeator;
        boolean isMulti;

        public ItemModel(String type, String title, boolean isMulti) {
            this.type = type;
            this.title = title;
            this.isMulti = isMulti;
        }
    }
}

