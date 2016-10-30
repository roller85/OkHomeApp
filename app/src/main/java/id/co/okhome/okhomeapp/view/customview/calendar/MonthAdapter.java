package id.co.okhome.okhomeapp.view.customview.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MonthAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{

    static Queue<MonthGridView> queueView = new LinkedList();

    final int size = 10000;
    int pivotYear, pivotMonth, pivotPos;
    int year, month;
    int height = 0;

    MonthViewListener monthViewListener;
    ViewPager vp;
    LayoutInflater inflater;
    Context context;

    Map<String, MonthGridView> mapMonthView = new HashMap<>();
    Map<String, List<DayModel>> mapListMonthDayModels = new HashMap<>();

    public MonthAdapter(ViewPager vp, int year, int month, int height, MonthViewListener monthViewListener) {
        this.vp = vp;
        this.context = vp.getContext();
        this.year = year;
        this.month = month;
        this.height = height;
        this.monthViewListener = monthViewListener;
        pivotYear = year;
        pivotMonth = month;
        pivotPos = size / 2;

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MonthGridView getCurrentMonthView(){

        MonthGridView monthGridView = mapMonthView.get(makeKey(year, month));
        return monthGridView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        MonthGridView monthView = queueView.poll();
        if(monthView == null){
            monthView = makeMonthView(position);
        }

        int year, month;
        Map<String, Integer> mapYearMonth = getCurrentYearMonth(position);
        year = mapYearMonth.get("year");
        month = mapYearMonth.get("month");

        //기존 내역있는지 확인.
        String key = makeKey(year, month);

        List<DayModel> listDayModelsTemp = mapListMonthDayModels.get(key);
        monthView.init(year, month, height, listDayModelsTemp);

        mapMonthView.put(key, monthView);
        mapListMonthDayModels.put(key, monthView.getListDayModels());

        container.addView(monthView);
        return monthView;
    }

    private String makeKey(int year, int month){
        String key = year + "_" + month;
        return key;
    }

    //월 뷰 만든다
    private MonthGridView makeMonthView(int position){

        MonthGridView monthView = new MonthGridView(context, monthViewListener);
        return monthView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        MonthGridView vBefore = (MonthGridView)object;
        queueView.offer(vBefore);
        container.removeView(vBefore);
//        mapMonthView.remove(makeKey(vBefore.getYear(), vBefore.getMonth()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //선택되었다.
        Map<String, Integer> mapYearMonth = getCurrentYearMonth(position);
        this.year = mapYearMonth.get("year");
        this.month =  mapYearMonth.get("month");
        Log.d("JO", "position : " + position + " " + mapYearMonth.get("year") + " " + mapYearMonth.get("month"));
        MonthGridView monthGridView = getCurrentMonthView();
        monthViewListener.onMonthSelected(year, month, monthGridView);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //현재 년, 월 가져오기
    private Map<String, Integer> getCurrentYearMonth(final int position){
        int pos = position;
        int diffPos = pos - pivotPos;
        int diffYear = 0, posMonth, currentYear, currentMonth;

        if(diffPos < 0){
            //작으면
            posMonth = pivotMonth + diffPos;   //-2
            while(posMonth < 0){
                posMonth = posMonth + 12;
                diffYear ++;
            }
            currentMonth = posMonth;
            currentYear = pivotYear - diffYear;
        }else{
            //크면
            posMonth = pivotMonth + diffPos;
            while(posMonth > 12){
                posMonth = posMonth - 12;
                diffYear ++;
            }
            currentMonth = posMonth;
            currentYear = pivotYear + diffYear;
        }

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("year", currentYear);
        mapReturn.put("month", currentMonth);

        return mapReturn;
    }
}