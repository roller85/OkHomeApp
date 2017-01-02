package id.co.okhome.okhomeapp.view.fragment.makereservation.libs;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoRepeatorCallback;
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;
import id.co.okhome.okhomeapp.view.fragment.makereservation.model.SpecialCleaingForDayModel;

import static id.co.okhome.okhomeapp.R.id.itemPerioidCleaningForSpecial_tvAddition;

/**
 * Created by josong on 2017-01-02.
 */

public class SpecialCleaingForDayCallback extends JoRepeatorCallback<SpecialCleaingForDayModel> {

    @BindView(R.id.itemPerioidCleaningForSpecial_tvDate)        TextView tvDate;
    @BindView(R.id.itemPerioidCleaningForSpecial_tvNum)         TextView tvNum;
    @BindView(itemPerioidCleaningForSpecial_tvAddition)         TextView tvAddition;
    @BindView(R.id.itemPerioidCleaningForSpecial_tvbtnReq)      TextView tvbtnReq;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc1)        ImageView ivSpc1;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc2)      ImageView ivSpc2;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc3)      ImageView ivSpc3;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc4)      ImageView ivSpc4;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc5)      ImageView ivSpc5;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc6)      ImageView ivSpc6;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc7)      ImageView ivSpc7;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc8)      ImageView ivSpc8;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc9)      ImageView ivSpc9;
    @BindView(R.id.itemPerioidCleaningForSpecial_ivSpc10)      ImageView ivSpc10;

    Context context;
    float cleaningHourPer;
    OnItemClickListener onItemClickListener;
    String cleaningType;

    public SpecialCleaingForDayCallback(Context context, String cleaningType, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.cleaningType = cleaningType;
    }

    @Override
    public void onBind(View v, final SpecialCleaingForDayModel model) {
        ButterKnife.bind(this, v);

        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yy년 MM월 dd일(E)");
        String datetime = dtfOut.withLocale(Locale.KOREAN).print(model.jodaDate);
        tvDate.setText(datetime);


        dtfOut = DateTimeFormat.forPattern("HH:mm");
        String startTime = dtfOut.withLocale(Locale.KOREAN).print(model.jodaDate);

        HomeModel homeModel = CurrentUserInfo.get(context).listHomeModel.get(0);
        if(cleaningType.equals("MOVEIN")){
            cleaningHourPer = HomeModel.getCleaningHourMoveIn(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);
        }else{
            cleaningHourPer = HomeModel.getCleaningHour(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);
        }

        float addTime = cleaningHourPer;
        if(model.listSpcCleaning != null){
            for(SpcCleaningModel sm : model.listSpcCleaning){
                addTime += sm.hour;
            }
        }

        //여기서 8시간이상되면 사람들어가서 네시간으로 함.

        int iAddTime = (int)addTime;
        float minute = addTime % 1 * 60;

        String endTime = dtfOut.withLocale(Locale.KOREAN).print(model.jodaDate.plusHours(iAddTime).plusMinutes((int)minute));


        tvNum.setText(startTime + " ~ " + endTime + " (" + addTime + "시간)");
        tvAddition.setVisibility(View.INVISIBLE);

        Map<Integer, ImageView> mapSpcViews = new HashMap<>();
        mapSpcViews.put(SpcCleaningModel.ID_CELLING, ivSpc1);
        mapSpcViews.put(SpcCleaningModel.ID_WINDOW, ivSpc2);
        mapSpcViews.put(SpcCleaningModel.ID_BALCONY, ivSpc3);
        mapSpcViews.put(SpcCleaningModel.ID_PILL, ivSpc4);
        mapSpcViews.put(SpcCleaningModel.ID_REFRIGERATOR, ivSpc5);
        mapSpcViews.put(SpcCleaningModel.ID_TIDYUP, ivSpc6);
        mapSpcViews.put(SpcCleaningModel.ID_BATHROOM, ivSpc7);
        mapSpcViews.put(SpcCleaningModel.ID_KITCHEN, ivSpc8);
        mapSpcViews.put(SpcCleaningModel.ID_WALL, ivSpc9);
        mapSpcViews.put(SpcCleaningModel.ID_VENTILATOR, ivSpc10);

        Set key = mapSpcViews.keySet();


        if(model.listSpcCleaning == null || model.listSpcCleaning.size() <= 0){
            tvbtnReq.setText("신청하기");
            for (Iterator<Integer> iterator = key.iterator(); iterator.hasNext();) {
                int spcKey = iterator.next();
                ImageView ivItem = mapSpcViews.get(spcKey);
                ivItem.setAlpha(0.4f);
            }

        }else{
            tvbtnReq.setText("변경하기");
            int spcTotalHour = 0;
            int spcTotalPrice = 0;
            //스페셜 신청한게 있으면
            Map<Integer, SpcCleaningModel> mapSpcItemChecked = new HashMap<>();
            for(SpcCleaningModel m : model.listSpcCleaning){
                mapSpcItemChecked.put(Integer.parseInt(m.id), m);
            }

            for (Iterator<Integer> iterator = key.iterator(); iterator.hasNext();) {
                int spcKey = iterator.next();
                ImageView ivItem = mapSpcViews.get(spcKey);
                SpcCleaningModel spcCleaningModel = mapSpcItemChecked.get(spcKey);
                if(spcCleaningModel != null){
                    ivItem.setAlpha(1f);
                    spcTotalHour += spcCleaningModel.hour;
                    spcTotalPrice += spcCleaningModel.price;
                }else{
                    ivItem.setAlpha(0.4f);
                }
            }

            tvAddition.setVisibility(View.INVISIBLE);
            tvAddition.setText(spcTotalHour + "시간, " + Util.getMoneyString(spcTotalPrice, '.') + "Rp 추가됨");
        }

        tvbtnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(model);
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(SpecialCleaingForDayModel model);
    }
}
