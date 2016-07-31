package id.co.okhome.okhomeapp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarMonthView;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MyCleaningCalendarFragment extends Fragment {

    @BindView(R.id.fragMyCleaningCalendar_vp)
    ViewPager vp;

    @BindView(R.id.fragMyCleaningCalendar_tvYearMonth)
    TextView tvYearMonth;

    @BindView(R.id.fragMyCleaningCalendar_pbLoading)
    View vLoading;

    private MonthlySlidePagerAdapter pagerAdapter;
    CalendarMonthView[] monthViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_cleaning_calendar, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        initCalendar();

    }


    //서버에서
//    private void loadSchedule(){
//        CalendarMonthView monthView = pagerAdapter.getCurrentMonthView();
//        CalendarDayView dayView = monthView.getDayView("20160729");
//
//        dayView.getDayModel().isCleaningDay  =true;
//        dayView.refresh();
//    }



    private void initCalendar(){
        vLoading.setVisibility(View.VISIBLE);
        monthViews = new CalendarMonthView[MonthlySlidePagerAdapter.PAGES];
        vp.setVisibility(View.INVISIBLE);
        tvYearMonth.setVisibility(View.INVISIBLE);

        //달력 콜백
        final Handler handlerForUpdate = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                pagerAdapter = new MonthlySlidePagerAdapter(getContext());

                vp.setAdapter(pagerAdapter);
                vp.addOnPageChangeListener(pagerAdapter);
                vp.setCurrentItem(pagerAdapter.getPosition(Util.getCurrentYear(), Util.getCurrentMonth()+1));
                vp.setOffscreenPageLimit(1);

                //왠 이상한 버그때문에 이렇게 함
                new Handler(){
                    @Override
                    public void dispatchMessage(Message msg) {
                        super.dispatchMessage(msg);
                        vp.setCurrentItem(pagerAdapter.getPosition(Util.getCurrentYear(), Util.getCurrentMonth()));
                        vp.setVisibility(View.VISIBLE);
                        tvYearMonth.setVisibility(View.VISIBLE);

                    }
                }.sendEmptyMessageDelayed(0, 200);

                vLoading.setVisibility(View.GONE);
            }
        };

        //달력 스레드
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < MonthlySlidePagerAdapter.PAGES; i++) {
                    monthViews[i] = new CalendarMonthView(getActivity());
                }
                handlerForUpdate.sendEmptyMessageDelayed(0, 0);
            }
        }).start();

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

            Log.d("JO", "year " + year + " " + " month "  +month + " howFarFromBase " + howFarFromBase);
            cal.add(Calendar.MONTH, howFarFromBase);

            position = position % PAGES;

            container.addView(monthViews[position]);

            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            Log.d("JO", "year " + year + " " + " month "  +month);
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
            onMonthChange(params.get("YEAR"), params.get("MONTH")+1);
        }
    }

}
