package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.customview.OkHomeViewPager;
import id.co.okhome.okhomeapp.view.customview.ProgressDotsView;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationParam;
import id.co.okhome.okhomeapp.view.fragment.makereservation.oneday_new.ChooseDayFragment;
import id.co.okhome.okhomeapp.view.fragment.makereservation.oneday_new.DaySpecialCleaningChkFragment;
import id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new.RequestorInfoFragment;
import id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new.SettingCleaningPeriodFragment;

public class MakeCleaningReservationActivity extends OkHomeActivityParent {

    @BindView(R.id.actMakeOneDayReservation_vpContents) OkHomeViewPager vpContents;
    @BindView(R.id.actMakeOneDayReservation_tvCount)    TextView tvCount;
    @BindView(R.id.actMakeOneDayReservation_pdv)        ProgressDotsView pdv;
    @BindView(R.id.actMakeOneDayReservation_tvTitle)    TextView tvTitle;
    @BindView(R.id.actMakeOneDayReservation_tvLeft)     TextView tvLeft;
    @BindView(R.id.actMakeOneDayReservation_tvRight)    TextView tvRight;
    @BindView(R.id.actMakeOneDayReservation_rlContents) ViewGroup vgMain;
    @BindView(R.id.actMakeOneDayReservation_vbtnRight)  View vbtnRight;

    private  static final int REQ_COLLISION = 100;
    Reservation1DayCleaningAdapter pagerAdapter;
    MakeCleaningReservationParam makeReservationParam = new MakeCleaningReservationParam();
    String defaultDate = null;
    boolean hasOrder = false;
    String orderNo = "";
    String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderNo = MakeCleaningReservationParam.makeOrderNo();
        setContentView(R.layout.activity_make_oneday_reservation);
        defaultDate = getIntent().getStringExtra("DATE");
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("TYPE");
        init();
    }


    //초기화
    private void init(){
        pagerAdapter = new Reservation1DayCleaningAdapter(getSupportFragmentManager(), makeTabItems(type));
        pdv.setMaxCount(pagerAdapter.getCount());

        vpContents.setPagingEnabled(false);
        vpContents.addOnPageChangeListener(pagerAdapter);
        vpContents.setOffscreenPageLimit(pagerAdapter.getCount());
        vpContents.setAdapter(pagerAdapter);
        vpContents.setCurrentItem(0);

        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                pagerAdapter.onPageSelected(0);
            }
        }.sendEmptyMessageDelayed(0, 100);
    }

    //탭아이템 설정
    private List<ModelPageItem> makeTabItems(String type){
        List<ModelPageItem> list = new ArrayList<>();

        Util.Log(MakeCleaningReservationActivity.class + " " + "type :: " + type);

        if(type == null || type.equals("NORMAL")){

            ChooseDayFragment chooseDayFragment = new ChooseDayFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "NORMAL");
            bundle.putString("defaultDate", defaultDate);
            chooseDayFragment.setArguments(bundle);

            RequestorInfoFragment requestorInfoFragment = new RequestorInfoFragment();
            bundle = new Bundle();
            bundle.putString("type", "NORMAL");
            requestorInfoFragment.setArguments(bundle);

            DaySpecialCleaningChkFragment daySpecialCleaningChkFragment = new DaySpecialCleaningChkFragment();
            bundle = new Bundle();
            bundle.putString("type", "NORMAL");
            daySpecialCleaningChkFragment.setArguments(bundle);

            list.add(new ModelPageItem(chooseDayFragment, "청소일자를 선택해주세요"));
            list.add(new ModelPageItem(requestorInfoFragment, "추가 정보가 조금 더 필요해요!"));
            list.add(new ModelPageItem(daySpecialCleaningChkFragment, "스페셜 청소를 선택해보세요!"));

        }else if(type.equals("MOVEIN")){

            ChooseDayFragment chooseDayFragment = new ChooseDayFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "MOVEIN");
            bundle.putString("defaultDate", defaultDate);
            chooseDayFragment.setArguments(bundle);

            RequestorInfoFragment requestorInfoFragment = new RequestorInfoFragment();
            bundle = new Bundle();
            bundle.putString("type", "MOVEIN");
            requestorInfoFragment.setArguments(bundle);

            DaySpecialCleaningChkFragment daySpecialCleaningChkFragment = new DaySpecialCleaningChkFragment();
            bundle = new Bundle();
            bundle.putString("type", "MOVEIN");
            daySpecialCleaningChkFragment.setArguments(bundle);

            list.add(new ModelPageItem(chooseDayFragment, "청소일자를 선택해주세요"));
            list.add(new ModelPageItem(requestorInfoFragment, "추가 정보가 조금 더 필요해요!"));
            list.add(new ModelPageItem(daySpecialCleaningChkFragment, "스페셜 청소를 선택해보세요!"));
        }
        else{
            list.add(new ModelPageItem(new SettingCleaningPeriodFragment(), "원하는 주기청소 일정을 선택하세요"));
            list.add(new ModelPageItem(new id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new.ChooseStartDayFragment(), "청소 언제부터 시작할까요?"));
            list.add(new ModelPageItem(new id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new.RequestorInfoFragment(), "추가 정보가 조금 더 필요해요!"));
            list.add(new ModelPageItem(new id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new.PeriodicCleaningPackageFragment(), "청소 패키지를 선택하세요!"));
            list.add(new ModelPageItem(new id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new.PeriodicSpecialCleaningChkFragment(), "스페셜 청소를 선택해보세요!"));
        }

        return list;
    }

//
//    private void beforeSubmit(){
//        //중복된 청소 있는지 체크
//        final String userId = CurrentUserInfo.getId(this);
//        final String homeId = CurrentUserInfo.getHomeId(this);
//        final String periodType = makeReservationParam.periodType;
//        final String periodValue = makeReservationParam.periodValue;
//        final String cleaningCount = makeReservationParam.cleaningCount + "";
//        final String beginDate = makeReservationParam.periodStartDate;
//
//        final int pInt = ProgressDialogController.show(this);
//        RestClient.getCleaningRequestClient().requestPeriodCleaningCheck(orderNo, userId, homeId, periodType, periodValue, cleaningCount, beginDate).enqueue(new RetrofitCallback<List<CleaningReservationModel>>() {
//            @Override
//            public void onSuccess(List<CleaningReservationModel> result) {
//
//                if(result.size() <= 0){
//                    submit(); //바로제출
//                }else{
//                    //결과가있음.
//                    //대기열에 넣어두자
//                    JoSharedPreference.with().push("READY_CLEANING_QUEUE", result);
//                    startActivityForResult(new Intent(MakeCleaningReservationActivity.this, CollisionCleaningManagerActivity.class), REQ_COLLISION);
//
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                ProgressDialogController.dismiss(pInt);
//            }
//        });
//    }

    private void submitForOnedays(){
        final String userId = CurrentUserInfo.getId(this);
        final String homeId = CurrentUserInfo.getHomeId(this);
        final String spcCleaningIdsSets = makeReservationParam.spcCleaningIdsSets;
        final String cleaningDateTimes = makeReservationParam.cleaningDateTimes;
        final String cleaningNumbers = makeReservationParam.cleaningNumbersForSpc;
        final int cleaningCount = makeReservationParam.mapCleaningDateTime.size();
        final int totalBasicCleaningPrice = makeReservationParam.totalBasicCleaningPrice;
        final int pId = ProgressDialogController.show(this);


        RestClient.getCleaningRequestClient().requestCleanings(type, orderNo, userId, homeId, cleaningCount+"", totalBasicCleaningPrice, cleaningDateTimes, cleaningNumbers, spcCleaningIdsSets).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(String result) {
                hasOrder = true;
//                paramOrderNo = getIntent().getStringExtra("orderNo");
//                paramOrderName = getIntent().getStringExtra("orderName");
//                paramTotalPrice = getIntent().getIntExtra("totalPrice", 0);
                startActivity(new Intent(MakeCleaningReservationActivity.this, PaymentActivity.class)
                        .putExtra("orderNo", orderNo)
                        .putExtra("orderName", makeReservationParam.orderName)
                        .putExtra("totalPrice", makeReservationParam.totalPrice));
            }
        });
    }

    //주기청소 제출
    private void submitForPeriodic(){
        final String userId = CurrentUserInfo.getId(this);
        final String homeId = CurrentUserInfo.getHomeId(this);
        final String periodType = makeReservationParam.periodType;
        final String periodValue = makeReservationParam.periodValue;
        final String cleaningCount = makeReservationParam.cleaningCount + "";
        final String beginDate = makeReservationParam.periodStartDate;
        final String cleaningNumbersForSpc = makeReservationParam.cleaningNumbersForSpc;
        final String spcCleaningIdsSets = makeReservationParam.spcCleaningIdsSets;
        int totalBasicCleaningPrice = makeReservationParam.totalBasicCleaningPrice;


        if(!hasOrder){
            final int pId = ProgressDialogController.show(this);
            orderNo = MakeCleaningReservationParam.makeOrderNo();
            RestClient.getCleaningRequestClient().requestPeriodCleaning(orderNo, userId, homeId, periodType, periodValue, cleaningCount, beginDate, totalBasicCleaningPrice,
                    cleaningNumbersForSpc, spcCleaningIdsSets).enqueue(new RetrofitCallback<String>() {

                @Override
                public void onFinish() {
                    super.onFinish();
                    ProgressDialogController.dismiss(pId);
                }

                @Override
                public void onSuccess(String result) {
                    hasOrder  =true;
//                paramOrderNo = getIntent().getStringExtra("orderNo");
//                paramOrderName = getIntent().getStringExtra("orderName");
//                paramTotalPrice = getIntent().getIntExtra("totalPrice", 0);
                    startActivity(new Intent(MakeCleaningReservationActivity.this, PaymentActivity.class)
                            .putExtra("orderNo", orderNo)
                            .putExtra("orderName", makeReservationParam.orderName)
                            .putExtra("totalPrice", makeReservationParam.totalPrice));
                }
            });
        }else{
            startActivity(new Intent(MakeCleaningReservationActivity.this, PaymentActivity.class)
                    .putExtra("orderNo", orderNo)
                    .putExtra("orderName", makeReservationParam.orderName)
                    .putExtra("totalPrice", makeReservationParam.totalPrice));
        }
    }

    private void submit(){
        String type = getIntent().getStringExtra("TYPE");

        if(type.equals("NORMAL")){
            submitForOnedays();
        }else if(type.equals("MOVEIN")){
            submitForOnedays();
        }else{
            submitForPeriodic();
        }

    }

    @OnClick(R.id.actMakeOneDayReservation_vbtnRight)
    public void onBtnRightClick(View v){

        Fragment fCurrent = pagerAdapter.getCurrentFragment(vpContents.getCurrentItem());
        MakeCleaningReservationFlow flow = (MakeCleaningReservationFlow)fCurrent;

        //다음
        if(flow.next(makeReservationParam)){
            int nextPos = vpContents.getCurrentItem() + 1;
            if(nextPos >= pagerAdapter.getCount()){
                submit();
//                beforeSubmit();

            }else{
                vpContents.setCurrentItem(nextPos);
            }
        }else{
            ;
        }
        //현재 프래그먼트 갖고와서 상태보자

    }

    private void showDismissDialog(){
        DialogController.showAlertDialog(MakeCleaningReservationActivity.this, "Okhome", "예약을 종료할까요?", false, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String click = (String)params.get(DialogController.TAG_CLICK);
                if(click.equals("OK")){
                    finish();
                }else{
                    ;
                }
            }
        });
    }

    public View getVbtnRight() {
        return vbtnRight;
    }

    @OnClick(R.id.actMakeOneDayReservation_vbtnLeft)
    public void onLeftClick(View v){
        int prevPos = vpContents.getCurrentItem() - 1;
        if(prevPos < 0){

            showDismissDialog();;
        }else{
            vpContents.setCurrentItem(prevPos);
        }
    }

    @OnClick(R.id.actMain_llbtnX)
    public void onClickBtnX(View v){
//        DialogController.showCenterDialog(this,
//                new CommonTextDialog("Really cancel reservation?", "All things being written is not saved", new ViewDialog.DialogCommonCallback() {
//                    @Override
//                    public void onCallback(Object dialog, Map<String, Object> params) {
//                        String onClick = (String)params.get("ONCLICK");
//                        if(onClick.equals("OK")){
//                            finish();
//                        }
//                    }
//                }));

        showDismissDialog();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //
    //프래그먼트 어댑터
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
                tvLeft.setText("취소하기");
            }else{
                tvLeft.setText("이전으로");
            }

            if(position+1 == getCount()){
                tvRight.setText("결제하기");
            }else{
                tvRight.setText("다음으로");
            }

            if(getCurrentFragment(position) != null){
                Util.Log("onPageSelected position : " + position);
                MakeCleaningReservationFlow flow = (MakeCleaningReservationFlow)getCurrentFragment(position);
                flow.onCurrentPage(position, makeReservationParam);
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
    class ModelPageItem{
        public ModelPageItem(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }

        Fragment fragment;
        String title;
    }

    @Override
    public void onBackPressed() {
        onLeftClick(null);
    }
}
