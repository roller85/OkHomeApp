package id.co.okhome.okhomeapp.view.fragment.makereservation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.CommonCheckitemModel;
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.view.adapter.CommonChoiceViewController;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class HouseInfoFragment extends Fragment implements MakeReservationFlow {

    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemHomeType)          ViewGroup vgChkItemHomeType;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemHomeSize)          ViewGroup vgChkItemHomeSize;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemRestroomCount)     ViewGroup vgChkItemRestroomCount;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemRoomCount)         ViewGroup vgChkItemRoomCount;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemPet)               ViewGroup vgChkItemPet;

    Map<String, CommonChoiceViewController> mapChoiceView = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_houseinfo, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        mapChoiceView.put("HOMETYPE"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemHomeType, false, 3)
                        .addItem(new CommonCheckitemModel("Apartment", "0"))
                        .addItem(new CommonCheckitemModel("House", "1"))
                        .addItem(new CommonCheckitemModel("Studio", "2"))
                        .build()
                        .setChecked(0, true));

        mapChoiceView.put("HOMESIZE"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemHomeSize, false, 3)
                        .addItem(new CommonCheckitemModel("100 or less", "0"))
                        .addItem(new CommonCheckitemModel("100 or less", "1"))
                        .addItem(new CommonCheckitemModel("100 or less", "2"))
                        .build());

        mapChoiceView.put("ROOMCOUNT"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemRoomCount, false, 4)
                        .addItem(new CommonCheckitemModel("1", "0")).addItem(new CommonCheckitemModel("2", "1"))
                        .addItem(new CommonCheckitemModel("3", "2")).addItem(new CommonCheckitemModel("4", "3"))
                        .build());

        mapChoiceView.put("TOILETCOUNT"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemRestroomCount, false, 4)
                        .addItem(new CommonCheckitemModel("1", "0")).addItem(new CommonCheckitemModel("2", "1"))
                        .addItem(new CommonCheckitemModel("3", "2")).addItem(new CommonCheckitemModel("4 ~", "3"))
                        .build());

        mapChoiceView.put("PET"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemPet, true, 4)
                        .addItem(new CommonCheckitemModel("Dog", "0"))
                        .addItem(new CommonCheckitemModel("Cat", "1"))
                        .addItem(new CommonCheckitemModel("Etc", "2"))
                        .addItem(new CommonCheckitemModel("Nope", "3"), true)
                        .build());

        initDefaultDataSet();
    }

    private void initDefaultDataSet(){
        if(CurrentUserInfo.get(getContext()).listHomeModel != null && CurrentUserInfo.get(getContext()).listHomeModel.size() > 0){
            HomeModel hm = CurrentUserInfo.get(getContext()).listHomeModel.get(0);

            markCommonChkbox("HOMETYPE", hm.type);
            markCommonChkbox("HOMESIZE", hm.homeSize);
            markCommonChkbox("ROOMCOUNT", hm.roomCnt);
            markCommonChkbox("TOILETCOUNT", hm.restroomCnt);
            markCommonChkbox("PET", hm.pets);
        }
    }

    private void markCommonChkbox(String key, String value){

        Util.Log(key + " : " + value);
        if(value.equals("")) return;

        if(value.contains("|")){
            String[] arrValue = value.split("\\|");
            for(String s : arrValue){
                Integer pos = Integer.parseInt(s);
                mapChoiceView.get(key).setChecked(pos, true);
            }
        }else{
            Integer pos = Integer.parseInt(value);
            mapChoiceView.get(key).setChecked(pos, true);
        }
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {

    }

    @Override
    public boolean next(MakeReservationParam params) {

        try{
            if(mapChoiceView.get("HOMETYPE").getCheckedItemList().size() <= 0){
                throw new OkhomeException("What is your hometype?");
            }

            if(mapChoiceView.get("HOMESIZE").getCheckedItemList().size() <= 0){
                throw new OkhomeException("check your home size");
            }

            if(mapChoiceView.get("ROOMCOUNT").getCheckedItemList().size() <= 0){
                throw new OkhomeException("몇층집이십니까");
            }

            if(mapChoiceView.get("TOILETCOUNT").getCheckedItemList().size() <= 0){
                throw new OkhomeException("화장실");
            }

            if(mapChoiceView.get("PET").getCheckedItemList().size() <= 0){
                throw new OkhomeException("동물 뭐 키우심");
            }

        }catch(OkhomeException e){
            Util.showToast(getContext(), e.getMessage());
            return false;
        }

//        params.homeId = CurrentUserInfo.getHomeId(getContext());
//        params.homeType = CommonCheckitemModel.getCommaValue(mapChoiceView.get("HOMETYPE").getCheckedItemList());
//        params.homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get("HOMESIZE").getCheckedItemList());
//        params.roomCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get("ROOMCOUNT").getCheckedItemList());
//        params.toiletCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get("TOILETCOUNT").getCheckedItemList());
//        params.pet = CommonCheckitemModel.getCommaValue(mapChoiceView.get("PET").getCheckedItemList());

        return true;
    }


    @OnClick(R.id.fragmentMakeReservationHouseInfo_vbtnHomeAddress)
    public void onHomeAddressClick(View v){
//        DialogController.showCenterDialog(getContext(), new HouseAddressDialog(new ViewDialog.DialogCommonCallback() {
//            @Override
//            public void onCallback(Object dialog, Map<String, Object> params) {
//
//            }
//        }));
    }



}
