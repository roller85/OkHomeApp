package id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoChoiceViewController;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.ViewHolderUtil;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningGridDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class PeriodicCleaningPackageFragment extends Fragment implements MakeCleaningReservationFlow{

    @BindView(R.id.fragmentMRPCP_tvCleaningTime)        TextView tvCleaningTime;
    @BindView(R.id.fragmentMRPCP_tvPricePer1)           TextView tvPricePer1;
    @BindView(R.id.fragmentMRPCP_tvPeriod)              TextView tvPeriod;
    @BindView(R.id.fragmentMRPCP_vgPayItems)            ViewGroup vgPayItems;

    MakeCleaningReservationParam params;
    PayItemRepeator payItemRepeator;
    ChooseCleaningGridDialog dialogCleaningGrid;
    List<PeriodicPayItemModel> listItems;
    String sameKey = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_periodic_cleaning_package, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
        initCleaningList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean next(MakeCleaningReservationParam params) {
//        View view = getActivity().getLayoutInflater().inflate(R.layout.fullpopup_specialcleaning_promo, null);
//        ((MakeCleaningReservationActivity)getActivity()).addPopupView(view);

//        DialogController.showBottomDialog(getActivity(), dialogCleaningGrid);
//        DialogController.showBottomDialog(getContext(), new PeriodSpecialPackageDialog(new ViewDialog.DialogCommonCallback() {
//            @Override
//            public void onCallback(Object dialog, Map<String, Object> params) {
//
//
//            }
//        }));\\\

        try{
            if(payItemRepeator.getCheckedItemList().size() <= 0){
                throw new OkhomeException("결제상품을 선택하세요");
            }

        }catch(OkhomeException e){
            Util.showToast(getContext(), e.getMessage());
            return false;
        }

        PeriodicPayItemModel m = payItemRepeator.getCheckedItemList().get(0);
        params.cleaningCount = m.count;
        params.totalBasicCleaningPrice = m.price;
        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeCleaningReservationParam params) {
        Util.Log("onCurrentPage" + pos);
        this.params = params;

        adaptData();
        loadList();
    }

    //모든청소 리스트 설정
    private void initCleaningList(){
//        if(dialogCleaningGrid == null){
//            List<SpcCleaningModel> listExtraCleaning =  JoSharedPreference.with(getContext()).get("ExtraCleaningList");
//
//            dialogCleaningGrid = new ChooseCleaningGridDialog(8, listExtraCleaning, new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
//                @Override
//                public void onChoosed(List<SpcCleaningModel> list) {
//                    if(list.size() > 0){
//                        String ids = "";
//                        for(SpcCleaningModel m : list){
//                            ids += "," + m.id;
//                        }
//                        ids = ids.substring(1);
//
//                        //서버에 날릴거
//                    }else{
//                    }
//                }
//            });
//        }
    }


    private void loadList(){

        //청소가격,시간,주기
        String key = params.periodType + params.periodValue + params.optCleaningHour + params.optCleaningPrice;
        if(!sameKey.equals(key)){
            listItems = makePaymentItem();
            payItemRepeator = new PayItemRepeator(getContext(), vgPayItems, false, 1);
            payItemRepeator.setList(listItems);
            payItemRepeator.build();

            sameKey = key;
        }

    }

    //ㅂ랑 데이터 동기화
    private void adaptData(){
        String period = howoftenCleangText();
        tvPeriod.setText(period);
        tvCleaningTime.setText(params.optCleaningHour + " Hours");
        tvPricePer1.setText(Util.getMoneyString(params.optCleaningPrice, '.') + " Rupiah");
    }


    private List<PeriodicPayItemModel> makePaymentItem(){

        List<PeriodicPayItemModel> list = null;

        if(params.periodType.equals(MakeCleaningReservationParam.PERIODIC_1DAY_WEEK) || params.periodType.equals(MakeCleaningReservationParam.PERIODIC_DAYS_WEEK)){
            //4주에 4번

            int cleaningCount = getCleaingDayCount();
            int[] counts = null;
            switch(cleaningCount){
                case 1:
                    counts = new int[]{4, 8, 12};
                    break;
                case 2:
                    counts = new int[]{4, 8, 16};
                    break;
                case 3:
                    counts = new int[]{6, 12, 18};
                    break;
                case 4:
                    counts = new int[]{4, 8, 16};
                    break;
                case 5:
                    counts = new int[]{5, 10, 20};
                    break;
                case 6:
                    counts = new int[]{6, 12, 24};
                    break;
                case 7:
                    counts = new int[]{7, 14, 28};
                    break;
            }

            list = makePaymentListItem(params.periodType, 1, cleaningCount, counts);

        }
        else if(params.periodType.equals(MakeCleaningReservationParam.PERIODIC_1DAY_2WEEK)){
            //2주에 한번
            //4, 8 12
            list = makePaymentListItem(params.periodType, 2, 1, new int[]{4, 8, 12});
        }
        else if(params.periodType.equals(MakeCleaningReservationParam.PERIODIC_1DAY_4WEEK)){
            //4주에 한번
            //2, 4, 6
            list = makePaymentListItem(params.periodType, 4, 1, new int[]{2, 4, 6});
        }

        return list;
    }

    private List makePaymentListItem(String periodType, int perioidWeek, int countByPeriodWeek, int[] forCleaningCounts){
        int pricePer1 = (int)((float)params.optCleaningPrice);
        List<PeriodicPayItemModel> list = new ArrayList<>();

        float period = (float)perioidWeek / (float)countByPeriodWeek;


        for(int cleaningCount : forCleaningCounts){
            int iPeriod = (int)(cleaningCount * period);

            list.add(new PeriodicPayItemModel(
                    periodType
                    , cleaningCount + "회 청소권"
//                    , howoftenCleangText() + ", " + i + "주 동안"
                    , iPeriod + "주 청소 패키지"
                    , cleaningCount
                    , (getDiscoutedPrice(pricePer1, cleaningCount) * cleaningCount) / 10000 * 10000
                    , (pricePer1 * cleaningCount) / 10000 * 10000
                    , getDiscountRate(cleaningCount)
            ));
        }

        return list;
    }

    //주단위 계싼
//    private List makePaymentListItem(String periodType, int pivot, int count){
//        int pricePer1 = (int)((float)params.optCleaningPrice);
//        List<PeriodicPayItemModel> list = new ArrayList<>();
//        for(int i = pivot; i <= pivot * 4; i *= 2){
//
//            list.add(new PeriodicPayItemModel(
//                    periodType
//                    , count + "회 청소권"
////                    , howoftenCleangText() + ", " + i + "주 동안"
//                    , i + "주 청소 패키지"
//                    , count
//                    , (getDiscoutedPrice(pricePer1, count) * count) / 10000 * 10000
//                    , (pricePer1 * count) / 10000 * 10000
//                    , getDiscountRate(count)
//            ));
//            count = count * 2;
//        }
//
//        return list;
//    }

    private int getDiscoutedPrice(int price, int cleaningCount){
        price = price - (price * getDiscountRate(cleaningCount) / 100);
        return price;
    }

    private int getDiscountRate(int count){
        if(count >= 16){
            return 10;
        }else if(count >= 12){
            return 8;
        }else if(count >= 8){
            return 5;
        }else if(count >= 4){
            return 3;
        }else{
            return 0;
        }
    }

    //한주에 청소 몇번?
    private int getCleaingDayCount(){
        int dayCount = 0;
        //요일
        String[] arrDays = params.periodValue.split(",");
        for(String day : arrDays){
            if(!day.equals("X")){
                dayCount ++;
            }
        }
        return dayCount;
    }

    private String howoftenCleangText(){
        String period= "";
        int dayCount = getCleaingDayCount();

        if(params.periodType.equals(MakeCleaningReservationParam.PERIODIC_1DAY_WEEK)){
            period = "일주일에 " + dayCount + "번";
        }
        else if(params.periodType.equals(MakeCleaningReservationParam.PERIODIC_DAYS_WEEK)){
            period = "일주일에 " + dayCount + "번";
        }
        else if(params.periodType.equals(MakeCleaningReservationParam.PERIODIC_1DAY_2WEEK)){
            period = "2주일에 " + dayCount + "번";
        }
        else if(params.periodType.equals(MakeCleaningReservationParam.PERIODIC_1DAY_4WEEK)){
            period = "한달에 " + dayCount + "번";
        }

        return period;
    }




    class PayItemRepeator extends JoChoiceViewController<PeriodicPayItemModel> {
        public PayItemRepeator(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
            super(context, vgContent, multiChoice, spanSize);
        }

        @Override
        public View getItemView(LayoutInflater inflater, PeriodicPayItemModel model, int pos) {
            View vItem = inflater.inflate(R.layout.item_period_payment_item, null);
            TextView tvPrice = ViewHolderUtil.getView(vItem, R.id.itemPeriodPaymentItem_tvPrice);
            TextView tvPriceBefore = ViewHolderUtil.getView(vItem, R.id.itemPeriodPaymentItem_tvPriceBefore);
            TextView tvSub = ViewHolderUtil.getView(vItem, R.id.itemPeriodPaymentItem_tvSub);
            TextView tvTitle = ViewHolderUtil.getView(vItem, R.id.itemPeriodPaymentItem_tvTitle);
            TextView tvDiscountRibu = ViewHolderUtil.getView(vItem, R.id.itemPeriodPaymentItem_tvDiscountRibu);
            View vCancelLine = ViewHolderUtil.getView(vItem, R.id.itemPeriodPaymentItem_vCancelLine);

            int ribuPrice = model.price / 10000 * 10;
            int ribuPriceBefore = model.priceBefore / 10000 * 10;
            tvPrice.setText(Util.getMoneyString(ribuPrice, '.') + " Ribu");
            tvPriceBefore.setText(Util.getMoneyString(ribuPriceBefore, '.') + " Ribu");
            tvSub.setText(model.subTitle);
            tvTitle.setText(model.title);
            tvDiscountRibu.setText("-" + Util.getMoneyString((ribuPriceBefore - ribuPrice), '.') + " Ribu");

            vCancelLine.setVisibility(View.VISIBLE);

            if(model.price == model.priceBefore){
                vCancelLine.setVisibility(View.GONE);
                tvPriceBefore.setText("할인없음");
                tvDiscountRibu.setVisibility(View.GONE);
            }

            return vItem;
        }

        @Override
        public void onItemCheckChanged(View vItem, PeriodicPayItemModel model, boolean checked, int pos) {
            ImageView ivChk = (ImageView)ViewHolderUtil.getView(vItem, R.id.itemPeriodPaymentItem_ivChk);
            if(checked){
                ivChk.setImageResource(R.drawable.ic_done_deepblue);
            }else{
                ivChk.setImageResource(R.drawable.ic_done_circle);
            }
        }
    }


    class PeriodicPayItemModel{
        public String type;
        public String title, subTitle;
        public int count;
        public int price, priceBefore;
        public int discountRate;

        public PeriodicPayItemModel(String type, String title, String subTitle, int count, int price, int priceBefore, int discountRate) {
            this.type = type;
            this.title = title;
            this.subTitle = subTitle;
            this.count = count;
            this.price = price;
            this.priceBefore = priceBefore;
            this.discountRate = discountRate;
        }
    }


}

