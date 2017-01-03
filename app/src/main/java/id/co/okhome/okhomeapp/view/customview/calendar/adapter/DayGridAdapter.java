package id.co.okhome.okhomeapp.view.customview.calendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTimeConstants;

import java.util.List;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.ViewHolderUtil;
import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthViewListener;

/**
 * Created by josongmin on 2016-09-29.
 */

public class DayGridAdapter extends ParentDayGridAdapter {


    View currentView;
    DayModel currentModel;
    public DayGridAdapter(Context context, List<DayModel> listDayModel, int itemHeight, MonthViewListener monthViewListener) {
        super(context, listDayModel, itemHeight, monthViewListener);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DayModel dayModel = listDayModel.get(position);
        View v = convertView;
        if(v == null){
            v = inflater.inflate(R.layout.item_calendar_day_req_periodic, null);
        }

        currentView = v;
        currentModel = dayModel;

        ViewGroup vgParent = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_vgParent);
        View vItem = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_vItem);
        TextView tvDay = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvDay);
        TextView tvCleaning = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvCleaning);

        if(vgParent.getLayoutParams() != null){
            vgParent.getLayoutParams().height = itemHeight;
        }



        tvDay.setText(dayModel.day+"");
        tvCleaning.setVisibility(View.GONE);
        //추가 필드를 처리할 필요가 있을때 inflating해서 addView

        //공용처리
        //Reserved처리
        filterIsReserved();

        if(dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth()) && year == dayModel.year && (month == dayModel.month)){
            //같으면 현재.
            vItem.setBackgroundColor(colorBgCurrentMonth);

            if(dayModel.dayOfWeek == DateTimeConstants.SUNDAY){
                tvDay.setTextColor(colorTextCurrentSunday);
            }else if(dayModel.dayOfWeek == DateTimeConstants.SATURDAY){
                tvDay.setTextColor(colorTextCurrentSaturday);
            }else{
                tvDay.setTextColor(colorTextCurrentPlainDay);
            }

        }else{
            //다르면 지난, 앞으로.
            vItem.setBackgroundColor(colorBgNotCurrentMonth);
            tvDay.setTextColor(colorTextNotCurrentMonth);
        }


        filterIsStartToMove();
        filterIsAbleToBeMoved();
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthViewListener.onDayClick(position, position / 7, dayModel);
            }
        });
        return v;
    }

    //청소예약 상태 확인
    private void filterIsReserved(){

        TextView tvCleaning = ViewHolderUtil.getView(currentView, R.id.itemCalendarDayBasic_tvCleaning);

        if(currentModel.cleaningScheduleModel != null && currentModel.cleaningScheduleModel.status.equals("OK")){
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText("예약완료");
            tvCleaning.setBackgroundColor(colorCaptionReserved);
        }
    }

    //이동마크처리
    private void filterIsStartToMove(){

        TextView tvCleaning = ViewHolderUtil.getView(currentView, R.id.itemCalendarDayBasic_tvCleaning);

        if(currentModel.startMove){
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText("이동대기");
            tvCleaning.setBackgroundColor(colorRed);
        }else{

        }
    }

    private void filterIsAbleToBeMoved(){
        TextView tvCleaning = ViewHolderUtil.getView(currentView, R.id.itemCalendarDayBasic_tvCleaning);


        if(params.get("expiryMill") != null){
            long expiryMill = (long)params.get("expiryMill");

            if(expiryMill > currentModel.timemill){
                //예약가능
            }else{
                //만료일지남
                tvCleaning.setVisibility(View.VISIBLE);
                tvCleaning.setText("기간경과");


                if(currentModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth()) && year == currentModel.year && (month == currentModel.month)){
                    tvCleaning.setBackgroundColor(colorCaptionReservedAlpha);
                }else{
                    //다르면 지난, 앞으로.
                    tvCleaning.setBackgroundColor(colorCaptionReservedAlpha2);
                }
            }

            if(currentModel.cleaningScheduleModel != null && currentModel.cleaningScheduleModel.status.equals("OK")){
                ;
            }else{
            }

        }


    }


}
