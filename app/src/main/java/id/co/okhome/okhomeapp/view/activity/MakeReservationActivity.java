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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import id.co.okhome.okhomeapp.view.fragment.makereservation.periodic.ChooseCleaningPeriodFragment;

public class MakeReservationActivity extends OkHomeActivityParent {

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
        pagerAdapter = new Reservation1DayCleaningAdapter(getSupportFragmentManager(), makeTabItems(getIntent().getStringExtra("TYPE")));
        pdv.setMaxCount(pagerAdapter.getCount());

        vpContents.setPagingEnabled(false);
        vpContents.addOnPageChangeListener(pagerAdapter);
        vpContents.setOffscreenPageLimit(pagerAdapter.getCount());
        vpContents.setAdapter(pagerAdapter);
        vpContents.setCurrentItem(0);

        pagerAdapter.onPageSelected(0);

    }

    private List<ModelPageItem> makeTabItems(String type){
        List<ModelPageItem> list = new ArrayList<>();

        if(type == null || type.equals("ONEDAY")){
            list.add(new ModelPageItem(new ChooseDayFragment(), "When do you want to get our service?"));
            list.add(new ModelPageItem(new HouseInfoFragment(), "Let us know your home information"));
            list.add(new ModelPageItem(new RequestorInfoFragment(), "Let us your information"));
            list.add(new ModelPageItem(new OnedayCleaningInvoiceFragment(), "Check invoice below"));
        }else{
            list.add(new ModelPageItem(new ChooseCleaningPeriodFragment(), "When do you want to get our service?"));
            list.add(new ModelPageItem(new HouseInfoFragment(), "Let us know your home information"));
            list.add(new ModelPageItem(new RequestorInfoFragment(), "Let us your information"));

        }

        return list;
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

    class ModelPageItem{
        public ModelPageItem(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }

        Fragment fragment;
        String title;
    }

    class Reservation1DayCleaningAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{

        final Map<Integer, Fragment> mapFragment = new HashMap<Integer, Fragment>();

        List<ModelPageItem> listItem = new ArrayList<>();
        public Reservation1DayCleaningAdapter(FragmentManager fm, List<ModelPageItem> list) {
            super(fm);
            this.listItem = list;
        }


        @Override
        public Fragment getItem(int position) {
            Fragment f = listItem.get(position).fragment;
            mapFragment.put(position, f);
            return f;
        }

        @Override
        public int getCount() {
            return listItem.size();
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tvCount.setText("(" + (position+1) + "/" + this.getCount() + ")");
            pdv.setCurrentPos(position);
            //텍스트 변경
            tvTitle.setText(listItem.get(position).title);

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
