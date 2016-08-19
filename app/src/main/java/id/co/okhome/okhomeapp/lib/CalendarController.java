package id.co.okhome.okhomeapp.lib;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarMonthView;

/**
 * Created by josongmin on 2016-08-05.
 */

public class CalendarController {

    @BindView(R.id.layerCalendar_tvYearMonth)
    TextView tvYearMonth;

    @BindView(R.id.layerCalendar_pbLoading)
    View vLoading;

    @BindView(R.id.layerCalendar_vp)
    ViewPager vp;

    private CalendarMonthView.CalendarType calendarType;
    private OnCalendarChangeListener onCalendarChangeListener;
    private CalendarMonthView.OnDayClickListener onDayClickListner;
    private CalendarMonthView[] monthViews;
    private MonthlySlidePagerAdapter pagerAdapter;
    private Context context;

    private int pageDelayCount = 3; //버그해결용

    public static final CalendarController with(Activity activity){
        CalendarController calendarController = new CalendarController(activity);
        return calendarController;
    }

    public static final CalendarController with(Fragment fragment){
        CalendarController calendarController = new CalendarController(fragment);
        return calendarController;
    }

    public CalendarController(Activity activity){
        ButterKnife.bind(this, activity);
        this.context = activity;
    }

    public CalendarController(Fragment fragment){
        ButterKnife.bind(this, fragment.getView());
        context = fragment.getContext();
    }

    public CalendarController setCalendarType(CalendarMonthView.CalendarType calendarType){
        this.calendarType = calendarType;
        return this;
    }

    public CalendarController setOnCalendarChangeListener(OnCalendarChangeListener onCalendarChangeListener) {
        this.onCalendarChangeListener = onCalendarChangeListener;
        return this;
    }

    public CalendarController setOnDayClickListner(CalendarMonthView.OnDayClickListener onDayClickListner){
        this.onDayClickListner = onDayClickListner;
        return this;
    }

    public View getDayViewSample(){
        return pagerAdapter.getCurrentMonthView().getDayViewSample();
    }

    //캘린더초기화
    public CalendarController initCalendar(){
        vLoading.setVisibility(View.VISIBLE);
        monthViews = new CalendarMonthView[MonthlySlidePagerAdapter.PAGES];
        vp.setVisibility(View.INVISIBLE);
        tvYearMonth.setVisibility(View.INVISIBLE);

        //달력 콜백
        final Handler handlerForUpdate = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                pagerAdapter = new MonthlySlidePagerAdapter(context);

                vp.setAdapter(pagerAdapter);
                vp.addOnPageChangeListener(pagerAdapter);
                vp.setCurrentItem(pagerAdapter.getPosition(Util.getCurrentYear(), Util.getCurrentMonth()+pageDelayCount));
                vp.setOffscreenPageLimit(1);

                //왠 이상한 버그때문에 이렇게 함
                new Handler(){
                    @Override
                    public void dispatchMessage(Message msg) {
                        if(msg.what > 0){
                            vp.setCurrentItem(pagerAdapter.getPosition(Util.getCurrentYear(), Util.getCurrentMonth()+ msg.what));
                            sendEmptyMessageDelayed(msg.what - 1, 100);
                        }else{
                            vp.setCurrentItem(pagerAdapter.getPosition(Util.getCurrentYear(), Util.getCurrentMonth()));
                            vp.setVisibility(View.VISIBLE);
                            tvYearMonth.setVisibility(View.VISIBLE);

                            vLoading.setVisibility(View.GONE);

                            if(onCalendarChangeListener != null){
                                onCalendarChangeListener.onCalendarLoad();
                            }
                        }
                    }
                }.sendEmptyMessageDelayed(pageDelayCount-1, 100);


            }
        };

        //달력 스레드. 생성작업은 백그라운드로 간다
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < MonthlySlidePagerAdapter.PAGES; i++) {
                    monthViews[i] = new CalendarMonthView(context);
                    monthViews[i].setCalendarType(calendarType);
                    monthViews[i].setOnDayClickListener(onDayClickListner);
                    monthViews[i].init();

                }
                handlerForUpdate.sendEmptyMessageDelayed(0, 0);
            }
        }).start();

        return this;
    }
    private void onMonthChange(int year, int month){
        tvYearMonth.setText(year + "." + Util.fillupWithZero(month, "XX"));
    }


    //어댑터
    class MonthlySlidePagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        @SuppressWarnings("unused")
        private Context mContext;

        final static int BASE_YEAR = 2015;
        final static int BASE_MONTH = Calendar.JANUARY;
        final Calendar BASE_CAL;
        final static int PAGES = 5;
        final static int LOOPS = 1000;
        final static int BASE_POSITION = PAGES * LOOPS / 2;
        private int previousPosition;

        public MonthlySlidePagerAdapter(Context context) {
            this.mContext = context;
            Calendar base = Calendar.getInstance();
            base.set(BASE_YEAR, BASE_MONTH, 1);
            BASE_CAL = base;
        }

        public Map<String, Integer> getYearMonth(int position) {
            Calendar cal = (Calendar)BASE_CAL.clone();
            cal.add(Calendar.MONTH, position - BASE_POSITION);

            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("YEAR", cal.get(Calendar.YEAR));
            map.put("MONTH", cal.get(Calendar.MONTH));

            return map;
        }

        public CalendarMonthView getCurrentMonthView(){
            return monthViews[vp.getCurrentItem() % PAGES];
        }

        public int getPosition(int year, int month) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month-1, 1);
            return BASE_POSITION + howFarFromBase(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        }

        private int howFarFromBase(int year, int month) {
            int disY = (year - BASE_YEAR) * 12;
            int disM = month - BASE_MONTH;
            return disY + disM;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            int howFarFromBase = position - BASE_POSITION;
            Calendar cal = (Calendar) BASE_CAL.clone();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);

            cal.add(Calendar.MONTH, howFarFromBase);
            position = position % PAGES;
            container.addView(monthViews[position]);

            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            monthViews[position].make(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));

            return monthViews[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return PAGES * LOOPS;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch(state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    previousPosition = vp.getCurrentItem();
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if(previousPosition != position) {
                previousPosition = position;
                Map<String, Integer> params = getYearMonth(position);
            }
        }

        @Override
        public void onPageSelected(int position) {
            Map<String, Integer> params = getYearMonth(position);
            int year, month;
            year = params.get("YEAR");
            month = params.get("MONTH")+1;
            onMonthChange(year, month);
            if(onCalendarChangeListener != null){
                onCalendarChangeListener.onMonthChange(year, month);
            }
        }
    }

    public interface OnCalendarChangeListener{
        public void onMonthChange(int year, int month);
        public void onCalendarLoad();
    }



}
