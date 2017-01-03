package id.co.okhome.okhomeapp.view.customview.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.GridView;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.view.customview.calendar.adapter.OnedayCleaningStartDayGridAdapter;
import id.co.okhome.okhomeapp.view.customview.calendar.adapter.DayGridAdapter;
import id.co.okhome.okhomeapp.view.customview.calendar.adapter.ParentDayGridAdapter;
import id.co.okhome.okhomeapp.view.customview.calendar.adapter.PeriodCleaningStartDayGridAdapter;

/**
 * Created by josongmin on 2016-09-25.
 */

public class MonthGridView extends FrameLayout{

    GridView gv;

    ParentDayGridAdapter dayGridAdapter;
    int year, month;
    List<DayModel> listDayModels = null;
    Map<String, DayModel> hashDayModel = new HashMap<>();
    int height = 0;
    MonthViewListener monthViewListener;
    String gridAdapterType = "";
    Map<String, Object> params;

    public MonthGridView(Context context, MonthViewListener monthViewListener) {
        super(context);
        this.monthViewListener = monthViewListener;
        initViews();
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }


    public MonthGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public MonthGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews(){
        if(gv == null){
            gv = new GridView(getContext());
            gv.setNumColumns(7);
            addView(gv);
        }
    }

    public void setGridAdapterType(String gridAdapterType) {
        this.gridAdapterType = gridAdapterType;
    }

    public List<DayModel> getListDayModels() {
        return listDayModels;
    }

    public void init(int year, int month, int height, List<DayModel> listDayModelsTemp){
        //뷰 6개 준비
        this.year = year;
        this.month = month;
        this.height = height;
        this.listDayModels = listDayModelsTemp;

        makeDayViews(height);
    }

    public void clear(){
        for(DayModel m : listDayModels){
            m.clear();
        }

    }

    public void clear(String key){
        for(DayModel m : listDayModels){
            m.clear(key);
        }

    }

    public DayModel getDayModel(String yyyymmdd){
        if(hashDayModel.get(yyyymmdd) == null){
            Log.d("JO", "getDayModel err: " + year + " " + month + " " + hashDayModel.size());
            return hashDayModel.get(yyyymmdd);
        }else{
            return hashDayModel.get(yyyymmdd);
        }
    }

    //뷰만듦.
    private void makeDayViews(int height){

        if(listDayModels == null){
            listDayModels = new ArrayList<>();

            Util.Log("makeDayViews " + year + " " + month);

            LocalDate targetDate = new LocalDate().withYear(year).withMonthOfYear(month).withDayOfMonth(1).minusWeeks(1).withDayOfWeek(DateTimeConstants.SUNDAY);
            for(int i = 0; i < 42; i++){

                DayModel dayModel = new DayModel(
                        targetDate.getYear(), targetDate.getMonthOfYear(), targetDate.getDayOfMonth(), targetDate.getDayOfWeek(),
                        targetDate.toDate().getTime());

                String yyyymmdd = dayModel.yyyymmdd;
                //yyyymmdd만들어서 키로 보관
                hashDayModel.put(yyyymmdd, dayModel);
                listDayModels.add(dayModel);

                targetDate = targetDate.plusDays(1);
            }
        }else{
            for(DayModel m : listDayModels){
                hashDayModel.put(m.yyyymmdd, m);
            }
        }

        //캘린더 돌면서 시간땡긴다.
        if(dayGridAdapter == null){

            //새 초기화일때만
            int cellHeight = height / 6;
            if(cellHeight <= 0){
                cellHeight = 100;
            }

            if(gridAdapterType.equals(CalendarView.GRIDTYPE_CHOOSE_STARTDAY)){
                dayGridAdapter = new PeriodCleaningStartDayGridAdapter(getContext(), listDayModels, cellHeight, monthViewListener);
            }else if(gridAdapterType.equals(CalendarView.GRIDTYPE_CHOOSE_ONEDAY_STARTDAY)){
                dayGridAdapter = new OnedayCleaningStartDayGridAdapter(getContext(), listDayModels, cellHeight, monthViewListener);
            }

            else{
                dayGridAdapter = new DayGridAdapter(getContext(), listDayModels, cellHeight, monthViewListener);
            }

            dayGridAdapter.setYearMonth(year, month);
            dayGridAdapter.setParams(params);
            gv.setAdapter(dayGridAdapter);

        }else{
            dayGridAdapter.listDayModel = listDayModels;
            dayGridAdapter.setYearMonth(year, month);
            dayGridAdapter.notifyDataSetChanged();
        }

    }

    public void notifyDataSetChanged(){
        dayGridAdapter.notifyDataSetChanged();
    }

}
