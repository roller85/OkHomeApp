package id.co.okhome.okhomeapp.view.dialog;

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
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CommonCheckitemModel;
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.adapter.CommonChoiceViewController;

/**
 * Created by josongmin on 2016-08-09.
 */

public class HomeInformationDialog extends ViewDialog{

    @BindView(R.id.dialogHomeInformation_vgChkItemHomeType)          ViewGroup vgChkItemHomeType;
    @BindView(R.id.dialogHomeInformation_vgChkItemHomeSize)          ViewGroup vgChkItemHomeSize;
    @BindView(R.id.dialogHomeInformation_vgChkItemRestroomCount)     ViewGroup vgChkItemRestroomCount;
    @BindView(R.id.dialogHomeInformation_vgChkItemRoomCount)         ViewGroup vgChkItemRoomCount;
    @BindView(R.id.dialogHomeInformation_vgChkItemPet)               ViewGroup vgChkItemPet;


    DialogCommonCallback callback;
    Map<String, CommonChoiceViewController> mapChoiceView = new HashMap<>();

    HomeModel homeModel;

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

        mapChoiceView.put("HOMETYPE"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemHomeType, false, 3)
                        .addItem(new CommonCheckitemModel("Apartment", CommonCheckitemModel.HOMETYPE_APARTMENT))
                        .addItem(new CommonCheckitemModel("House", CommonCheckitemModel.HOMETYPE_HOUSE))
                        .addItem(new CommonCheckitemModel("Studio", CommonCheckitemModel.HOMETYPE_STUDIO))
                        .build()
                        .setChecked(0, true));

        mapChoiceView.put("HOMESIZE"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemHomeSize, false, 3)
                        .addItem(new CommonCheckitemModel("100 or less", CommonCheckitemModel.HOMESIZE_100LESS))
                        .addItem(new CommonCheckitemModel("200 or less", CommonCheckitemModel.HOMETYPE_200LESS))
                        .addItem(new CommonCheckitemModel("300 or more", CommonCheckitemModel.HOMETYPE_300MORE))
                        .build());

        mapChoiceView.put("FLOORCOUNT"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemRoomCount, false, 4)
                        .addItem(new CommonCheckitemModel("1", "0")).addItem(new CommonCheckitemModel("2", "1"))
                        .addItem(new CommonCheckitemModel("3", "2")).addItem(new CommonCheckitemModel("4 or more", CommonCheckitemModel.COUNT_4_OR_MORE))
                        .build());

        mapChoiceView.put("TOILETCOUNT"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemRestroomCount, false, 4)
                        .addItem(new CommonCheckitemModel("1", "0")).addItem(new CommonCheckitemModel("2", "1"))
                        .addItem(new CommonCheckitemModel("3", "2")).addItem(new CommonCheckitemModel("4 or more", CommonCheckitemModel.COUNT_4_OR_MORE))
                        .build());

        mapChoiceView.put("PET"
                , (CommonChoiceViewController)new CommonChoiceViewController(getContext(), vgChkItemPet, true, 4)
                        .addItem(new CommonCheckitemModel("Dog", "0"))
                        .addItem(new CommonCheckitemModel("Cat", "1"))
                        .addItem(new CommonCheckitemModel("Etc", "2"))
                        .addItem(new CommonCheckitemModel("Nope", CommonCheckitemModel.NOPE), true)
                        .build());

        initDefaultChk();
    }

    private void initDefaultChk(){

        if(!homeModel.type.equals("")){
            chkDefault("HOMETYPE", homeModel.type);
            chkDefault("HOMESIZE", homeModel.homeSize);
            chkDefault("FLOORCOUNT", homeModel.floorCnt);
            chkDefault("TOILETCOUNT", homeModel.restroomCnt);
            chkDefault("HOMETYPE", homeModel.type);

            String[] petFields = homeModel.pets.split(",");
            for(String pet : petFields){
                chkDefault("PET", pet);
            }
        }


    }

    private void chkDefault(String tag, String value){
        int pos = CommonCheckitemModel.getFieldPosition(value);
        if(pos >= 0){
            mapChoiceView.get(tag).setChecked(pos, true);
        }

    }


    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){
        String homeType = "", homeSize = "", floorCount = "", toiletCount = "", pet = "";

        try{
            if(mapChoiceView.get("HOMETYPE").getCheckedItemList().size() <= 0){
                throw new OkhomeException("What is your hometype?");
            }

            if(mapChoiceView.get("HOMESIZE").getCheckedItemList().size() <= 0){
                throw new OkhomeException("check your home size");
            }

            if(mapChoiceView.get("FLOORCOUNT").getCheckedItemList().size() <= 0){
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
            return ;
        }

        String homeId = CurrentUserInfo.getHomeId(getContext());
        homeType = CommonCheckitemModel.getCommaValue(mapChoiceView.get("HOMETYPE").getCheckedItemList());
        homeSize = CommonCheckitemModel.getCommaValue(mapChoiceView.get("HOMESIZE").getCheckedItemList());
        floorCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get("FLOORCOUNT").getCheckedItemList());
        toiletCount = CommonCheckitemModel.getCommaValue(mapChoiceView.get("TOILETCOUNT").getCheckedItemList());
        pet = CommonCheckitemModel.getCommaValue(mapChoiceView.get("PET").getCheckedItemList());


        final int pId = ProgressDialogController.show(getContext());
        RestClient.getHomeClient().updateHomeExtraInfo(homeModel.id, homeType, homeSize, floorCount, toiletCount, pet).enqueue(new RetrofitCallback<String>() {

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
