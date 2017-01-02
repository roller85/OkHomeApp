package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CommonCheckitemModel;
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.adapter.CommonChoiceViewController;

import static id.co.okhome.okhomeapp.model.HomeModel.TAG_HOMESIZE_HOME_APT;
import static id.co.okhome.okhomeapp.model.HomeModel.TAG_HOMESIZE_STUDIO;
import static id.co.okhome.okhomeapp.model.HomeModel.TAG_HOMETYPE;
import static id.co.okhome.okhomeapp.model.HomeModel.TAG_PET;
import static id.co.okhome.okhomeapp.model.HomeModel.TAG_ROOMCOUNT;
import static id.co.okhome.okhomeapp.model.HomeModel.TAG_TOILETCOUNT;

/**
 * Created by josongmin on 2016-08-09.
 */

public class HomeInformationDialog extends ViewDialog implements CommonChoiceViewController.ItemListener{


    @BindView(R.id.dialogHomeInformation_vgChkItemHomeAptSizeParent)       ViewGroup vgChkItemHomeAptSizeParent;
    @BindView(R.id.dialogHomeInformation_vgChkItemStudioSizeParent)        ViewGroup vgChkItemStudioSizeParent;
    @BindView(R.id.dialogHomeInformation_vgChkItemRoomCountParent)        ViewGroup vgChkItemRoomCountParent;


    @BindView(R.id.dialogHomeInformation_vgChkItemHomeType)          ViewGroup vgChkItemHomeType;
    @BindView(R.id.dialogHomeInformation_vgChkItemHomeAptSize)       ViewGroup vgChkItemHomeAptSize;
    @BindView(R.id.dialogHomeInformation_vgChkItemStudioSize)        ViewGroup vgChkItemStudioSize;
    @BindView(R.id.dialogHomeInformation_vgChkItemRestroomCount)     ViewGroup vgChkItemRestroomCount;
    @BindView(R.id.dialogHomeInformation_vgChkItemRoomCount)         ViewGroup vgChkItemRoomCount;
    @BindView(R.id.dialogHomeInformation_vgChkItemPet)               ViewGroup vgChkItemPet;

    @BindView(R.id.dialogHomeInformation_tvHour)                     TextView tvHour;
    @BindView(R.id.dialogHomeInformation_tvPricePer1)                TextView tvPrice;

    DialogCommonCallback callback;
    Map<String, CommonChoiceViewController> mapChoiceView = new HashMap<>();
    HomeModel homeModel;
    float hourPer = 0;
    int pricePer = 0;
    float hourMoveinPer = 0;
    int priceMoveinPer = 0;

    public HomeInformationDialog(HomeModel homeModel, DialogCommonCallback callback) {
        this.callback = callback;
        this.homeModel = homeModel;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_home_information, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        mapChoiceView.put(TAG_HOMETYPE
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemHomeType, false, 3)
                        .addItem(new CommonCheckitemModel("Apartment", HomeModel.HOMETYPE_APT))
                        .addItem(new CommonCheckitemModel("House", HomeModel.HOMETYPE_HOUSE))
                        .addItem(new CommonCheckitemModel("Studio", HomeModel.HOMETYPE_STUDIO))
                        .build());
        mapChoiceView.get(TAG_HOMETYPE).addItemListener(this);
        mapChoiceView.get(TAG_HOMETYPE).addItemListener(new CommonChoiceViewController.ItemListener() {
            @Override
            public void onItemChecked(CommonCheckitemModel model, int pos) {
                vgChkItemHomeAptSizeParent.setVisibility(View.GONE);
                vgChkItemStudioSizeParent.setVisibility(View.GONE);
                vgChkItemRoomCountParent.setVisibility(View.GONE);

                if(model.tag.equals(HomeModel.HOMETYPE_STUDIO)){
                    mapChoiceView.get(TAG_ROOMCOUNT).clear();
                    vgChkItemStudioSizeParent.setVisibility(View.VISIBLE);
                }else{
                    vgChkItemRoomCountParent.setVisibility(View.VISIBLE);
                    vgChkItemHomeAptSizeParent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemChanged(CommonCheckitemModel model, int pos) {

            }
        });

        mapChoiceView.put(TAG_HOMESIZE_STUDIO
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemStudioSize, false, 3)
                        .addItem(new CommonCheckitemModel("100미만", HomeModel.HOMESIZE_50LESS))
                        .addItem(new CommonCheckitemModel("50~100", HomeModel.HOMESIZE_50_100))
                        .addItem(new CommonCheckitemModel("몰라용", HomeModel.HOMESIZE_DONTKNOW))
                        .build());
        mapChoiceView.get(TAG_HOMESIZE_STUDIO).addItemListener(this);

        mapChoiceView.put(TAG_HOMESIZE_HOME_APT
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemHomeAptSize, false, 3)
                        .addItem(new CommonCheckitemModel("100미만", HomeModel.HOMESIZE_100LESS))
                        .addItem(new CommonCheckitemModel("100 ~ 150", HomeModel.HOMESIZE_100_150))
                        .addItem(new CommonCheckitemModel("150 ~ 200", HomeModel.HOMESIZE_150_200))
                        .addItem(new CommonCheckitemModel("200 ~ 250", HomeModel.HOMESIZE_200_250))
                        .addItem(new CommonCheckitemModel("250 이상", HomeModel.HOMESIZE_250MORE))
                        .addItem(new CommonCheckitemModel("몰라용", HomeModel.HOMESIZE_DONTKNOW))
                        .build());
        mapChoiceView.get(TAG_HOMESIZE_HOME_APT).addItemListener(this);

        mapChoiceView.put(TAG_ROOMCOUNT
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemRoomCount, false, 4)
                        .addItem(new CommonCheckitemModel("하나", HomeModel.COUNT_1))
                        .addItem(new CommonCheckitemModel("둘", HomeModel.COUNT_2))
                        .addItem(new CommonCheckitemModel("셋", HomeModel.COUNT_3))
                        .addItem(new CommonCheckitemModel("넷 이상", HomeModel.COUNT_4MORE))
                        .build());
        mapChoiceView.get(TAG_ROOMCOUNT).addItemListener(this);

        mapChoiceView.put(TAG_TOILETCOUNT
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemRestroomCount, false, 4)
                        .addItem(new CommonCheckitemModel("하나", HomeModel.COUNT_1))
                        .addItem(new CommonCheckitemModel("둘", HomeModel.COUNT_2))
                        .addItem(new CommonCheckitemModel("셋", HomeModel.COUNT_3))
                        .addItem(new CommonCheckitemModel("넷 이상", HomeModel.COUNT_4MORE))
                        .build());
        mapChoiceView.get(TAG_TOILETCOUNT).addItemListener(this);

        mapChoiceView.put(TAG_PET
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemPet, true, 4)
                        .addItem(new CommonCheckitemModel("Dog", HomeModel.HOMEPET_DOG))
                        .addItem(new CommonCheckitemModel("Cat", HomeModel.HOMEPET_CAT))
                        .addItem(new CommonCheckitemModel("Etc", HomeModel.HOMEPET_ETC))
                        .addItem(new CommonCheckitemModel("Nope", HomeModel.HOMEPET_NOPE), true)
                        .build());
        mapChoiceView.get(TAG_PET).addItemListener(this);


        initDefaultChk();
    }

    @Override
    public void onItemChanged(CommonCheckitemModel model, int pos) {
        String homeType = "", homeSize = "", roomCount = "", toiletCount = "", pet = "";
        String homeId = CurrentUserInfo.getHomeId(getContext());
        homeType = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMETYPE).getCheckedItemList());homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMESIZE_HOME_APT).getCheckedItemList());

        //집 크기
        if(homeType.equals(HomeModel.HOMETYPE_STUDIO)){
            homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMESIZE_STUDIO).getCheckedItemList());
        }else{
            homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMESIZE_HOME_APT).getCheckedItemList());
        }

        roomCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_ROOMCOUNT).getCheckedItemList());
        toiletCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_TOILETCOUNT).getCheckedItemList());
        pet = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_PET).getCheckedItemList());

        //가격, 시간 계산
        float hour = HomeModel.getCleaningHour(homeType, homeSize, roomCount, toiletCount);
        int rupiah = HomeModel.getCleaningPrice(homeType, homeSize, roomCount, toiletCount);

        this.hourPer = hour;
        this.pricePer = rupiah;

        this.hourMoveinPer = HomeModel.getCleaningHourMoveIn(homeType, homeSize, roomCount, toiletCount);
        this.priceMoveinPer = HomeModel.getCleaningPriceMoveIn(homeType, homeSize, roomCount, toiletCount);

        if(hour <= 0){
            tvPrice.setText("-");
            tvHour.setText("-");
        }else{
            tvPrice.setText(Util.getMoneyString(rupiah, '.') + " Rupiah");
            tvHour.setText(hour + " Hours");
        }
    }

    //아이템 체크되었을때 호출ㄴ
    @Override
    public void onItemChecked(CommonCheckitemModel model, int pos) {
        ;

    }

    private void initDefaultChk(){

        if(!homeModel.type.equals("")){
            chkDefault(HomeModel.TAG_HOMETYPE, homeModel.type);
            chkDefault(TAG_HOMESIZE_HOME_APT, homeModel.homeSize);
            chkDefault(TAG_HOMESIZE_STUDIO, homeModel.homeSize);
            chkDefault(HomeModel.TAG_ROOMCOUNT, homeModel.roomCnt);
            chkDefault(TAG_TOILETCOUNT, homeModel.restroomCnt);

            String[] petFields = homeModel.pets.split(",");
            for(String pet : petFields){
                chkDefault(TAG_PET, pet);
            }
        }
    }

    //기본설정
    private void chkDefault(String tag, String value){
        int pos = CommonCheckitemModel.getFieldPosition(tag, value);
        if(pos >= 0){
            mapChoiceView.get(tag).setChecked(pos, true);
        }
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){
        String homeType = "", homeSize = "", roomCount = "", toiletCount = "", pet = "";

        try{
            if(mapChoiceView.get(HomeModel.TAG_HOMETYPE).getCheckedItemList().size() <= 0){
                throw new OkhomeException("What is your hometype?");
            }

            homeType = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMETYPE).getCheckedItemList());
            if(homeType.equals(HomeModel.HOMETYPE_STUDIO)){
                if(mapChoiceView.get(TAG_HOMESIZE_STUDIO).getCheckedItemList().size() <= 0){
                    throw new OkhomeException("check your home size");
                }
            }else{
                if(mapChoiceView.get(TAG_HOMESIZE_HOME_APT).getCheckedItemList().size() <= 0){
                    throw new OkhomeException("check your home size");
                }

                if(mapChoiceView.get(TAG_ROOMCOUNT).getCheckedItemList().size() <= 0){
                    throw new OkhomeException("방 개수가 몇개인가요");
                }
            }
            //홈사이즈




            if(mapChoiceView.get(TAG_TOILETCOUNT).getCheckedItemList().size() <= 0){
                throw new OkhomeException("화장실");
            }

            if(mapChoiceView.get(TAG_PET).getCheckedItemList().size() <= 0){
                throw new OkhomeException("동물 뭐 키우심");
            }

        }catch(OkhomeException e){
            Util.showToast(getContext(), e.getMessage());
            return ;
        }

        String homeId = CurrentUserInfo.getHomeId(getContext());
        homeType = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMETYPE).getCheckedItemList());homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMESIZE_HOME_APT).getCheckedItemList());

        //집 크기
        if(homeType.equals(HomeModel.HOMETYPE_STUDIO)){
            homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMESIZE_STUDIO).getCheckedItemList());
        }else{
            homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_HOMESIZE_HOME_APT).getCheckedItemList());
        }

        roomCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_ROOMCOUNT).getCheckedItemList());
        if(roomCount.equals("")) roomCount = HomeModel.COUNT_0;

        toiletCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_TOILETCOUNT).getCheckedItemList());
        pet = CommonCheckitemModel.getCommaValue(mapChoiceView.get(TAG_PET).getCheckedItemList());


        final int pId = ProgressDialogController.show(getContext());
        RestClient.getHomeClient().updateHomeExtraInfo(
                homeModel.id, homeType, homeSize, roomCount, toiletCount, pet, hourPer, pricePer, hourMoveinPer, priceMoveinPer).enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(String result) {
                dismiss();
                callback.onCallback(getDialog(), Util.makeMap("SUCCESS", true));
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });

        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){
        dismiss();
    }


}
