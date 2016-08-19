package id.co.okhome.okhomeapp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.view.customview.OkHomeViewPager;
import id.co.okhome.okhomeapp.view.customview.ProgressDotsView;
import id.co.okhome.okhomeapp.view.fragment.makereservation.HouseInfoFragment;
import id.co.okhome.okhomeapp.view.fragment.makereservation.RequestorInfoFragment;
import id.co.okhome.okhomeapp.view.fragment.makereservation.oneday.ChooseDayFragment;
import id.co.okhome.okhomeapp.view.fragment.makereservation.oneday.OnedayCleaningInvoiceFragment;

public class MakeOneDayReservationActivity extends OkHomeActivityParent {

    @BindView(R.id.actMakeOneDayReservation_vpContents) OkHomeViewPager vpContents;
    @BindView(R.id.actMakeOneDayReservation_tvCount)    TextView tvCount;
    @BindView(R.id.actMakeOneDayReservation_pdv)        ProgressDotsView pdv;
    @BindView(R.id.actMakeOneDayReservation_tvTitle)    TextView tvTitle;
    @BindView(R.id.actMakeOneDayReservation_tvLeft)     TextView tvLeft;
    @BindView(R.id.actMakeOneDayReservation_tvRight)    TextView tvRight;


    Reservation1DayCleaningAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_oneday_reservation);

        ButterKnife.bind(this);
        init();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private void init(){
        pagerAdapter = new Reservation1DayCleaningAdapter(getSupportFragmentManager());
        pdv.setMaxCount(pagerAdapter.getCount());

        vpContents.setPagingEnabled(false);
        vpContents.addOnPageChangeListener(pagerAdapter);
        vpContents.setAdapter(pagerAdapter);
        vpContents.setCurrentItem(0);
        vpContents.setOffscreenPageLimit(5);
        pagerAdapter.onPageSelected(0);

    }


    @OnClick(R.id.actMakeOneDayReservation_vbtnRight)
    public void onBtnRightClick(View v){
        int nextPos = vpContents.getCurrentItem() + 1;
        if(nextPos >= pagerAdapter.getCount()){
            Util.showToast(this, "final");
        }else{
            vpContents.setCurrentItem(nextPos);
        }
    }

    @OnClick(R.id.actMakeOneDayReservation_vbtnLeft)
    public void onLeftClick(View v){
        int prevPos = vpContents.getCurrentItem() - 1;
        if(prevPos < 0){
            Util.showToast(this, "끄자는 얘기임?");
        }else{
            vpContents.setCurrentItem(prevPos);
        }
    }


    class Reservation1DayCleaningAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{
        public Reservation1DayCleaningAdapter(FragmentManager fm) {
            super(fm);
        }
        final Map<Integer, Fragment> mapFragment = new HashMap<Integer, Fragment>();
        final String[] arrTabTitle = new String[]{
                "When do you want to get our service?", "Let us know your home information", "Let us your information", "Check invoice below"};
        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch(position){
                case 0:
                    f = new ChooseDayFragment();
                    break;
                case 1:
                    f = new HouseInfoFragment();
                    break;
                case 2:
                    f = new RequestorInfoFragment();
                    break;
                case 3:
                    f = new OnedayCleaningInvoiceFragment();
                    break;

            }
            mapFragment.put(position, f);
            return f;
        }

        @Override
        public int getCount() {
            return 4;
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tvCount.setText("(" + (position+1) + "/" + this.getCount() + ")");
            pdv.setCurrentPos(position);
            //텍스트 변경
            tvTitle.setText(arrTabTitle[position]);

            //버튼텍스트
            if(position == 0){
                tvLeft.setText("Cancel");
            }else{
                tvLeft.setText("Prev");
            }

            if(position+1 == getCount()){
                tvRight.setText("Finish");
            }else{
                tvRight.setText("Berikut");
            }
        }

        public Fragment getCurrentFragment(int pos){
            Fragment f = mapFragment.get(pos);
            return f;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mapFragment.remove(position);
        }

    }
}
