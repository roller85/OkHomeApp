package id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import id.co.okhome.okhomeapp.view.fragment.makereservation.model.SpecialCleaingForDayModel;

/**
 * Created by josongmin on 2016-07-28.
 */

public class PeriodicSpecialCleaningChkFragment extends Fragment implements MakeCleaningReservationFlow{

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_period_spc_chk, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

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
                    .setCallBack(new id.co.okhome.okhomeapp.view.fragment.makereservation.libs.SpecialCleaingForDayCallback(getContext(), "NORMAL", new id.co.okhome.okhomeapp.view.fragment.makereservation.libs.SpecialCleaingForDayCallback.OnItemClickListener() {
                        @Override
                        public void onItemClick(final id.co.okhome.okhomeapp.view.fragment.makereservation.model.SpecialCleaingForDayModel model) {
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

        params.totalDuration = totalHour;
        params.totalPrice = totalPrice;

        if(params.isJustPeriod){
            List<SpecialCleaingForDayModel> listCleanings = repeator.getList();
            List<Integer> listNumber = new ArrayList<>();
            List<String> listSpcCleaningIds = new ArrayList<>();
            String cleaningNumbers = "";
            String spcCleaningIdsSets = ""; //{1,2,3}{1,2} 요런형태로

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
            //정리
            for(int number : listNumber){
                cleaningNumbers += "," + number;
            }
            if(cleaningNumbers.contains(","))   cleaningNumbers = cleaningNumbers.substring(1);

            //{1,2,3}{1,2} 요런형태로
            for(String spcCleaningIds : listSpcCleaningIds){
                spcCleaningIdsSets = spcCleaningIdsSets + "{" + spcCleaningIds + "}";
            }

            //끝..
            params.spcCleaningIdsSets = spcCleaningIdsSets;
            params.cleaningNumbersForSpc = cleaningNumbers;
            return true;
        }else{
            return false;
        }

        //아래두개 설정
//        params.cleaningNumbers;
//        params.spcCleaningIdsSets;

        //결제창으로 넘어갑시다.
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

        int spcHourSum = 0;
        int spcPriceSum = 0;
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

        spcPriceSum += params.totalBasicCleaningPrice;
        float totalCleaningHour = params.optCleaningHour * params.cleaningCount;
        String sTotalCleaningHour = params.optCleaningHour * params.cleaningCount + "";
        if(sTotalCleaningHour.contains(".")){
            sTotalCleaningHour = sTotalCleaningHour.substring(0, sTotalCleaningHour.indexOf("."));
        }
        boolean hasRemainder = Math.abs((totalCleaningHour - (int)totalCleaningHour)) > 0 ? true : false ;
        String hourTail = spcHourSum > 0 ? " + 스페셜청소 " + spcHourSum + "시간" : "";

        String timeCaption = "정기청소 " + (hasRemainder ? totalCleaningHour : sTotalCleaningHour) + "시간" + hourTail;
        tvPrice.setText(Util.getMoneyString(spcPriceSum, '.') + " Rupiah");
        tvHours.setText(timeCaption);

        params.orderName = timeCaption;

        //ㄴ나중에 넘길거
        totalPrice = spcPriceSum;
        totalHour = (int)totalCleaningHour + spcHourSum;
    }

    private void loadList(){

        String thumb = params.periodType+params.periodValue+params.cleaningCount+params.periodStartDate;
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


        LocalDateTime targetDateTime  = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDateTime(params.periodStartDate);
        String type = params.periodType;
        int cleaningCount = params.cleaningCount;
        String arrDays[] = params.periodValue.split(",");
        //일요일 0 월요일부터 1
        System.out.println(targetDateTime.getYear() + " " + targetDateTime.getMonthOfYear() + " " + targetDateTime.getDayOfMonth() + " " +targetDateTime.getDayOfWeek());

        //일단 일주일동안 청소 몇번있는지 확인
        Map<Integer, String> mapDay = new HashMap<>();
        for(int i = 0; i < arrDays.length; i++){
            String day = arrDays[i];
            if(!day.equals("X")){
                mapDay.put(i, day);
            }
        }

        // 1월 2화 3수 4목 5금 6토 7일

        // 1%7 = 1
        // 2%7 = 2


        int posCount = 0;
        int absCount = 0;
        int pos = 0;
        do{
            absCount ++;
            int dayOfWeek = targetDateTime.getDayOfWeek();
            String time = mapDay.get(dayOfWeek % 7);
            if(time != null){
                //있다.
                posCount++;
//                System.out.println(posCount + "번째 청소에 해당함 : " + targetDate.getYear() + " " + targetDate.getMonthOfYear() + " " + targetDate.getDayOfMonth() + " " +targetDate.getDayOfWeek());
                //모델생성.

                String dateTime = targetDateTime.getYear() + "-" + targetDateTime.getMonthOfYear() + "-" + targetDateTime.getDayOfMonth() + " " +
                        time;

                LocalDateTime resultDateTime  = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseLocalDateTime(dateTime);

                list.add(new SpecialCleaingForDayModel(resultDateTime, pos++));
            }
            targetDateTime = targetDateTime.plusDays(1);

            if(absCount % 7 == 0){
                if(type.equals("1W")){
                    ;
                }else if(type.equals("2W")){
                    targetDateTime = targetDateTime.plusWeeks(1);
                }else if(type.equals("4W")){
                    targetDateTime = targetDateTime.plusWeeks(3);
                }
            }
        }while(cleaningCount > posCount);

        return list;
    }


}
