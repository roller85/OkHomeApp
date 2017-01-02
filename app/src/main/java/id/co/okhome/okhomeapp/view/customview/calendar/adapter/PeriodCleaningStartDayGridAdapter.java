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

public class PeriodCleaningStartDayGridAdapter extends ParentDayGridAdapter {


    public PeriodCleaningStartDayGridAdapter(Context context, List<DayModel> listDayModel, int itemHeight, MonthViewListener monthViewListener) {
        super(context, listDayModel, itemHeight, monthViewListener);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            v = inflater.inflate(R.layout.item_calendar_day_req_periodic, null);
        }

        ViewGroup vgParent = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_vgParent);
        View vItem = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_vItem);
        TextView tvDay = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvDay);
        TextView tvCleaning = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvCleaning);

        if(vgParent.getLayoutParams() != null){
            vgParent.getLayoutParams().height = itemHeight;
        }

        final DayModel dayModel = listDayModel.get(position);

        tvDay.setText(dayModel.day+"");
        tvCleaning.setVisibility(View.GONE);
        //추가 필드를 처리할 필요가 있을때 inflating해서 addView

        //공용처리
        //Reserved처리
        filterIsReserved(v, dayModel);

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
//            tvCleaning.setBackgroundColor(colorCaptionReservedAlpha);
        }


        //하루짜리 청소신청시 시작시간 설정되었을 때
        if(dayModel.optBeginTime != null){
            tvCleaning.setBackgroundColor(colorCaptionBlue);
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText(dayModel.optBeginTime);
        }

        //주기청소 신청시 시작일, 파란색

        //예약가능한 날짜인지 확인
        filterIsAbleReservationForPeriod(v, dayModel);

        //시작일인지
        filterIsStartDay(v, dayModel);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthViewListener.onDayClick(position, position / 7, dayModel);
            }
        });
        return v;
    }

    //청소예약 상태 확인
    private void filterIsReserved(View v, DayModel dayModel){

        TextView tvCleaning = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvCleaning);

        if(dayModel.cleaningScheduleModel != null && dayModel.cleaningScheduleModel.status.equals("OK")){
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText("예약완료");
            tvCleaning.setBackgroundColor(colorCaptionReserved);
        }
    }

    //시작일인지 확인
    private void filterIsStartDay(View v, DayModel dayModel){
        TextView tvCleaning = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvCleaning);

        if(dayModel.optBeginDay){
            tvCleaning.setBackgroundColor(colorCaptionBlue);
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText("시작일");
        }
    }

    //예약가능한 날짜인지
    private void filterIsAbleReservationForPeriod(View v, DayModel dayModel){
        TextView tvCleaning = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvCleaning);

        if (dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth())) {
            //현재 보이는 년월
            if (year == dayModel.year && month == dayModel.month) {

            }

            if(dayModel.cleaningScheduleModel != null && dayModel.cleaningScheduleModel.status.equals("OK")){
                ;
            }else{
                if(dayModel.optAbleReservationForPeriod){
                    tvCleaning.setBackgroundColor(colorCaptionBlueAlpha);
                    tvCleaning.setVisibility(View.VISIBLE);
                    tvCleaning.setText("예약가능");
                }
            }
        }
    }
}
