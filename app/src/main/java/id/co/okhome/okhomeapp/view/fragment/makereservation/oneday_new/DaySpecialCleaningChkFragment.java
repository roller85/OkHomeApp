package id.co.okhome.okhomeapp.view.fragment.makereservation.oneday_new;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.TutoriorPreference;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningGridDialog;
import id.co.okhome.okhomeapp.view.etc.FullpopupSpecialCleaningPromotion;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationParam;
import id.co.okhome.okhomeapp.view.fragment.makereservation.libs.SpecialCleaingForDayCallback;
import id.co.okhome.okhomeapp.view.fragment.makereservation.model.SpecialCleaingForDayModel;

import static org.joda.time.format.DateTimeFormat.forPattern;

/**
 * Created by josongmin on 2016-07-28.
 */

public class DaySpecialCleaningChkFragment extends Fragment implements MakeCleaningReservationFlow{

    @BindView(R.id.fragmentMRPSC_tvPrice)               TextView tvPrice;
    @BindView(R.id.fragmentMRPSC_tvHour)                TextView tvHours;
    @BindView(R.id.dialogPeriodSpecialPackage_vgItems)  ViewGroup vgItems;

    MakeCleaningReservationParam params;
    FullpopupSpecialCleaningPromotion fullpopupSpecialCleaningPromotion;
    JoViewRepeator<SpecialCleaingForDayModel> repeator;
    List<SpecialCleaingForDayModel> list= null;
    String beforeThumb = "";
    float cleaningHourPer = 0;
    int totalPrice = 0, totalHour = 0;
    int spcPriceSum = 0;
    String cleaningType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_period_spc_chk, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
        cleaningType = getArguments().getString("type");
        HomeModel homeModel = CurrentUserInfo.get(getContext()).listHomeModel.get(0);
//            String homeType, String homeSize, String roomCount, String restroomCount)
        cleaningHourPer = HomeModel.getCleaningHour(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);


        fullpopupSpecialCleaningPromotion = new FullpopupSpecialCleaningPromotion(getActivity());
        fullpopupSpecialCleaningPromotion.setClickListener(new FullpopupSpecialCleaningPromotion.ClickListener() {
            @Override
            public void onOkClick() {
                ; //do nothing
            }
        });

        if(repeator == null){
            repeator = new JoViewRepeator<SpecialCleaingForDayModel>(getActivity())
                    .setContainer(vgItems)
                    .setSpanSize(1)
                    .setItemLayoutId(R.layout.item_periodcleaning_forspecial)
                    .setCallBack(new SpecialCleaingForDayCallback(getContext(), cleaningType, new SpecialCleaingForDayCallback.OnItemClickListener() {
                        @Override
                        public void onItemClick(final SpecialCleaingForDayModel model) {
                            DialogController.showChooseExtraCleaningList(getActivity(), 0, model.listSpcCleaning, new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
                                @Override
                                public void onChoosed(List<SpcCleaningModel> list) {
                                    model.hasExtra = true;
                                    model.listSpcCleaning = list;
                                    repeator.notifyDataChange(model);
                                    adaptTotalPriceAndDuration();;
                                }
                            });
                        }
                    }));
        }

    }

    @Override
    public boolean next(MakeCleaningReservationParam params) {


        String cleaningDateTimes = "";

        //스페셜 청소 번호 가져오기
        List<SpecialCleaingForDayModel> listCleanings = repeator.getList();
        List<Integer> listNumber = new ArrayList<>();
        List<String> listSpcCleaningIds = new ArrayList<>();
        String spcCleaningIdsSets = ""; //{1,2,3}{1,2} 요런형태로
        String cleaningNumbers = "";
        for(SpecialCleaingForDayModel m : listCleanings){
            if(m.hasExtra){
                int pos = m.pos;
                String spcCleaningIds = "";
                for(SpcCleaningModel spcCleaningModel : m.listSpcCleaning){
                    spcCleaningIds += "," + spcCleaningModel.id;
                }
                if(spcCleaningIds.contains(","))    spcCleaningIds = spcCleaningIds.substring(1);

                listNumber.add(pos);
                listSpcCleaningIds.add(spcCleaningIds);
            }
        }

        for(int number : listNumber){
            cleaningNumbers += "," + number;
        }
        if(cleaningNumbers.contains(","))   cleaningNumbers = cleaningNumbers.substring(1);
        //{1,2,3}{1,2} 요런형태로
        for(String spcCleaningIds : listSpcCleaningIds){
            spcCleaningIdsSets = spcCleaningIdsSets + "{" + spcCleaningIds + "}";
        }


        //현재 청소 시간들 가져오기
        TreeMap<String, String> treeMap = new TreeMap<>(params.mapCleaningDateTime);
        Iterator<String> iteratorKey = treeMap.keySet( ).iterator( );   //키값 오름차순 정렬(기본)

        while(iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            String time = treeMap.get(key);

            LocalDateTime resultDateTime  = forPattern("yyyyMMdd HH:mm").parseLocalDateTime(key + " " + time);
            String dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(resultDateTime);

            cleaningDateTimes += "," + dateTime;
        }

        cleaningDateTimes = cleaningDateTimes.substring(1);

        HomeModel homeModel = CurrentUserInfo.get(getContext()).listHomeModel.get(0);
        int cleaningPrice = 0;
        if(cleaningType.equals("MOVEIN")){
            cleaningPrice = HomeModel.getCleaningPriceMoveIn(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);
        }else{
            cleaningPrice = HomeModel.getCleaningPrice(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);
        }

        int cleaningCount = params.mapCleaningDateTime.size();
        int totalBasicCleaningPrice = (int)(cleaningCount * cleaningPrice); //일반청소 총 가격. cleaningHour * 한번에 청소 가격

        //파람정리
        params.spcCleaningIdsSets = spcCleaningIdsSets;
        params.cleaningDateTimes = cleaningDateTimes;
        params.cleaningNumbersForSpc = cleaningNumbers;
        params.totalBasicCleaningPrice = totalBasicCleaningPrice;
        params.totalPrice = totalPrice;
        params.cleaningType = getArguments().getString("type");
        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeCleaningReservationParam params) {
        //좌우로
        this.params = params;
        if(TutoriorPreference.isFirst(TutoriorPreference.TUTORIORKEY_SPECIALCLEANING)){
            new Handler(){
                @Override
                public void dispatchMessage(Message msg) {
                    fullpopupSpecialCleaningPromotion.show();
                    TutoriorPreference.chkRead(TutoriorPreference.TUTORIORKEY_SPECIALCLEANING);
                }
            }.sendEmptyMessageDelayed(0, 500);
        }

        loadList();

        adaptTotalPriceAndDuration();
    }

    private void adaptTotalPriceAndDuration(){
        if(getArguments().getString("type").equals("MOVEIN")){
            adaptTotalPriceAndDurationForMoveIn();
        }else{
            adaptTotalPriceAndDurationForNormal();
        }
    }

    private void adaptTotalPriceAndDurationForMoveIn(){
        HomeModel homeModel = CurrentUserInfo.get(getContext()).listHomeModel.get(0);
        float cleaningHourPer = HomeModel.getCleaningHourMoveIn(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);
        int cleaningPrice = HomeModel.getCleaningPriceMoveIn(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);

        int spcHourSum = 0;
        int spcPriceSum = 0;
        int cleaningHour = (int)cleaningHourPer;
        int cleaningCount = params.mapCleaningDateTime.size();
        int totalBasicCleaningPrice = (int)(cleaningCount * cleaningPrice); //일반청소 총 가격. cleaningHour * 한번에 청소 가격

        List<SpecialCleaingForDayModel> list = repeator.getList();
        if(list != null){
            for(SpecialCleaingForDayModel m : list){
                if(m.listSpcCleaning == null)   continue;
                for(SpcCleaningModel spcCleaning : m.listSpcCleaning){
                    //선택된것들
                    spcHourSum += spcCleaning.hour;
                    spcPriceSum += spcCleaning.price;
                }
            }
        }

        spcPriceSum += totalBasicCleaningPrice;
        float totalCleaningHour = cleaningHour * cleaningCount;
        String sTotalCleaningHour = cleaningHour * cleaningCount + "";
        if(sTotalCleaningHour.contains(".")){
            sTotalCleaningHour = sTotalCleaningHour.substring(0, sTotalCleaningHour.indexOf("."));
        }
        boolean hasRemainder = Math.abs((totalCleaningHour - (int)totalCleaningHour)) > 0 ? true : false ;
        String hourTail = spcHourSum > 0 ? " + 스페셜청소 " + spcHourSum + "시간" : "";

        String timeCaption = "이사청소 " + (hasRemainder ? totalCleaningHour : sTotalCleaningHour) + "시간" + hourTail;
        tvPrice.setText(Util.getMoneyString(spcPriceSum, '.') + " Rupiah");
        tvHours.setText(timeCaption);

        params.orderName = timeCaption;

        //ㄴ나중에 넘길거
        totalPrice = spcPriceSum;
        totalHour = (int)totalCleaningHour + spcHourSum;
    }

    private void adaptTotalPriceAndDurationForNormal(){
        HomeModel homeModel = CurrentUserInfo.get(getContext()).listHomeModel.get(0);
        float cleaningHourPer = HomeModel.getCleaningHour(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);
        int cleaningPrice = HomeModel.getCleaningPrice(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);

        int spcHourSum = 0;
        int spcPriceSum = 0;
        int cleaningHour = (int)cleaningHourPer;
        int cleaningCount = params.mapCleaningDateTime.size();
        int totalBasicCleaningPrice = (int)(cleaningCount * cleaningPrice); //일반청소 총 가격. cleaningHour * 한번에 청소 가격

        List<SpecialCleaingForDayModel> list = repeator.getList();
        if(list != null){
            for(SpecialCleaingForDayModel m : list){
                if(m.listSpcCleaning == null)   continue;
                for(SpcCleaningModel spcCleaning : m.listSpcCleaning){
                    //선택된것들
                    spcHourSum += spcCleaning.hour;
                    spcPriceSum += spcCleaning.price;
                }
            }
        }

        spcPriceSum += totalBasicCleaningPrice;
        float totalCleaningHour = cleaningHour * cleaningCount;
        String sTotalCleaningHour = cleaningHour * cleaningCount + "";
        if(sTotalCleaningHour.contains(".")){
            sTotalCleaningHour = sTotalCleaningHour.substring(0, sTotalCleaningHour.indexOf("."));
        }
        boolean hasRemainder = Math.abs((totalCleaningHour - (int)totalCleaningHour)) > 0 ? true : false ;
        String hourTail = spcHourSum > 0 ? " + 스페셜청소 " + spcHourSum + "시간" : "";

        String timeCaption = "기본청소 " + (hasRemainder ? totalCleaningHour : sTotalCleaningHour) + "시간" + hourTail;
        tvPrice.setText(Util.getMoneyString(spcPriceSum, '.') + " Rupiah");
        tvHours.setText(timeCaption);

        params.orderName = timeCaption;

        //ㄴ나중에 넘길거
        totalPrice = spcPriceSum;
        totalHour = (int)totalCleaningHour + spcHourSum;

    }

    private void loadList(){

        String thumb = "";
        TreeMap<String, String> treeMap = new TreeMap<>(params.mapCleaningDateTime);
        Iterator<String> iteratorKey = treeMap.keySet().iterator( );   //키값 오름차순 정렬(기본)

        int pos = 0;
        while(iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            String time = treeMap.get(key);

            thumb += key;
        }


        if(beforeThumb.equals(thumb)){
            ;
        }else{
            beforeThumb = thumb;
            list = makeDayExtraCleaningItem();
            repeator.setList(list);
            repeator.notifyDataSetChanged();
        }
    }
    //
    private List<SpecialCleaingForDayModel> makeDayExtraCleaningItem(){
        //시작일 기준으로
        List<SpecialCleaingForDayModel> list = new ArrayList<>();

        TreeMap<String, String> treeMap = new TreeMap<>(params.mapCleaningDateTime);
        Iterator<String> iteratorKey = treeMap.keySet( ).iterator( );   //키값 오름차순 정렬(기본)

        int pos = 0;
        while(iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            String time = treeMap.get(key);

            LocalDateTime resultDateTime  = forPattern("yyyyMMdd HH:mm").parseLocalDateTime(key + " " + time);
            list.add(new SpecialCleaingForDayModel(resultDateTime, pos++));

//            String dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(resultDateTime);
        }

        return list;
    }




}
