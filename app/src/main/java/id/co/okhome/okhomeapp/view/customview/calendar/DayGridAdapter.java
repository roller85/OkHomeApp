package id.co.okhome.okhomeapp.view.customview.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.ViewHolderUtil;

/**
 * Created by josongmin on 2016-09-29.
 */

public class DayGridAdapter extends BaseAdapter{

    int itemHeight;
    LayoutInflater inflater;
    List<DayModel> listDayModel;
    int size;

    int year, month;

    @IdRes
    int id = 100000;

    int colorBgCurrentMonth, colorBgNotCurrentMonth;
    int colorTextCurrentPlainDay, colorTextCurrentSaturday, colorTextCurrentSunday, colorTextNotCurrentMonth;
    int colorCaptionReserved, colorCaptionReservedAlpha;
    int colorCaptionBlue, colorCaptionBlueAlpha;

    MonthViewListener monthViewListener;

    public DayGridAdapter(Context context, List<DayModel> listDayModel, int itemHeight, MonthViewListener monthViewListener) {
        this.itemHeight = itemHeight;
        this.inflater = (LayoutInflater)(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        this.listDayModel = listDayModel;
        this.size = listDayModel.size();
        this.monthViewListener = monthViewListener;

        colorBgCurrentMonth = Color.parseColor("#f9f9f9");
        colorBgNotCurrentMonth = Color.parseColor("#fdfdfd");
        colorTextCurrentSunday = Color.parseColor("#bc453f");
        colorTextCurrentSaturday = Color.parseColor("#438cd0");
        colorTextCurrentPlainDay = Color.parseColor("#454746");
        colorTextNotCurrentMonth = Color.parseColor("#e1e1e1");
        colorCaptionReserved = Color.parseColor("#b7bec8");
        colorCaptionReservedAlpha = Color.parseColor("#e2e6e9");

        colorCaptionBlue = Color.parseColor("#008fd5");
        colorCaptionBlueAlpha = Color.parseColor("#c0e3f3");

    }

    public void setYearMonth(int year, int month){
        this.year = year;
        this.month = month;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            v = inflater.inflate(R.layout.item_calendar_day_basic, null);
        }

        ViewGroup vgParent = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_vgParent);
        View vItem = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_vItem);
        TextView tvDay = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvDay);
        TextView tvCleaning = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_tvCleaning);
        FrameLayout flLayer = ViewHolderUtil.getView(v, R.id.itemCalendarDayBasic_vgExtraContents);

        if(vgParent.getLayoutParams() != null){
            vgParent.getLayoutParams().height = itemHeight;
        }

        final DayModel dayModel = listDayModel.get(position);

        tvDay.setText(dayModel.day+"");
        tvCleaning.setVisibility(View.GONE);
        //추가 필드를 처리할 필요가 있을때 inflating해서 addView

        //공용처리
        //Reserved처리
        if(dayModel.cleaningScheduleModel != null){
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText("Reserved");
        }


        if(dayModel.isAbleToReservation(Util.getCurrentYear(), Util.getCurrentMonth()) && year == dayModel.year && (month == dayModel.month)){
            //같으면 현재.
            vItem.setBackgroundColor(colorBgCurrentMonth);

            if(dayModel.dayOfWeek == 1){
                tvDay.setTextColor(colorTextCurrentSunday);
            }else if(dayModel.dayOfWeek == 7){
                tvDay.setTextColor(colorTextCurrentSaturday);
            }else{
                tvDay.setTextColor(colorTextCurrentPlainDay);
            }

            tvCleaning.setBackgroundColor(colorCaptionReserved);
        }else{
            //다르면 지난, 앞으로.
            vItem.setBackgroundColor(colorBgNotCurrentMonth);
            tvDay.setTextColor(colorTextNotCurrentMonth);
            tvCleaning.setBackgroundColor(colorCaptionReservedAlpha);
        }


        //하루짜리 청소신청시 시작시간 설정되었을 때
        if(dayModel.optBeginTime != null){
            tvCleaning.setBackgroundColor(colorCaptionBlue);
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText(dayModel.optBeginTime);
        }

        //주기청소 신청시 시작일, 파란색
        else if(dayModel.optBeginDay){
            tvCleaning.setBackgroundColor(colorCaptionBlue);
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText("시작일");
        }

        //주기청소 신청시 예정일
        else if(dayModel.optPlanned){
            tvCleaning.setVisibility(View.VISIBLE);
            tvCleaning.setText("예정일");
        }


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthViewListener.onDayClick(position, position / 7, dayModel);
            }
        });

        return v;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public DayModel getItem(int position) {
        return listDayModel.get(position);
    }

    @Override
    public int getCount() {
        return size;
    }
}
