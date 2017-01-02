package id.co.okhome.okhomeapp.view.fragment.makereservation.pediodic_new;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.dialog.ChangePhoneNumberDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonInputDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;
import id.co.okhome.okhomeapp.view.dialog.HomeInformationDialog;
import id.co.okhome.okhomeapp.view.dialog.HouseAddressDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class RequestorInfoFragment extends Fragment implements MakeCleaningReservationFlow, CurrentUserInfo.OnUserInfoChangeListener{

    @BindView(R.id.fragmentMRRequestorInfo_tvPhone)         TextView tvPhone;
    @BindView(R.id.fragmentMRRequestorInfo_tvEmail)         TextView tvEmail;
    @BindView(R.id.fragmentMRRequestorInfo_tvName)          TextView tvName;
    @BindView(R.id.fragmentMRRequestorInfo_tvAddress)       TextView tvAddress;
    @BindView(R.id.fragmentMRRequestorInfo_tvHomeinfo)      TextView tvHomeInfo;
    @BindView(R.id.fragmentMRRequestorInfo_ivPhoto)         ImageView ivPhoto;
    @BindView(R.id.fragmentMRRequestorInfo_vLoading)        View vLoading;


    MakeCleaningReservationParam params;
    UserModel userModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_requestorinfo, null);
    }

    @Override
    public boolean next(MakeCleaningReservationParam params) {

        //회원정보 다 들어가있어야함..근데 실시간으로 업데이트되서 param에서 취급안함.
        try{
            if(tvPhone.getText().length() <= 0){
                throw new OkhomeException("핸드폰 번호를 입력하세요");
            }

            if(tvAddress.getText().length() <= 0){
                throw new OkhomeException("주소를 입력하세요");
            }

            if(tvHomeInfo.getText().length() <= 0){
                throw new OkhomeException("집 정보를 입력하세요! 거의다 왔습니다!");
            }


            HomeModel homeModel = userModel.listHomeModel.get(0);
//            String homeType, String homeSize, String roomCount, String restroomCount)
            params.optCleaningHour = HomeModel.getCleaningHour(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);
            params.optCleaningPrice = HomeModel.getCleaningPrice(homeModel.type, homeModel.homeSize, homeModel.roomCnt, homeModel.restroomCnt);


            if(homeModel.homeSize.equals(HomeModel.HOMESIZE_250MORE)){
                CommonTextDialog commonTextDialog = DialogController.showAlertDialog(getContext(), "Okhome", "죄송합니다! 250sq 이상인 경우 저희 고객센터 상담후 예약 가능합니다!", false, new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {
                        String click = (String)params.get(DialogController.TAG_CLICK);
                        if(click.equals("OK")){
                            //전화하기.. 전화번호뭔데
                            Util.openPhoneDialIntent(getContext(), "081293583396");
                        }else{
                            ;
                        }
                    }
                });
                commonTextDialog.setButtonText("전화하기", "닫기");
                return false;
            }

        }catch(OkhomeException e){
            Util.showToast(getContext(), e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeCleaningReservationParam params) {
        Util.Log("onCurrentPage" + pos);
        this.params = params;
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        CurrentUserInfo.registerUserInfoChangeListener(this);
        getUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CurrentUserInfo.unregisterUserInfoChangeListener(this);
    }

    private void refresh(){
        getUserInfo();
    }

    //유저정보 불ㅇ러괴
    private void getUserInfo(){
        vLoading.setVisibility(View.VISIBLE);
        RestClient.getUserRestClient().getUser(CurrentUserInfo.get(getContext()).id).enqueue(new RetrofitCallback<UserModel>() {

            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);

            }

            @Override
            public void onSuccess(UserModel result) {
                CurrentUserInfo.set(getContext(), result); //invoke함
                onUserInfoChanged(result);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });
    }

    @Override
    public void onUserInfoChanged(UserModel userModel) {
        this.userModel = userModel;
        tvEmail.setText(userModel.email);
        tvPhone.setText(userModel.phone);
        tvName.setText(userModel.name);

        //집정보
        if(userModel.listHomeModel != null && userModel.listHomeModel.size() > 0){
            HomeModel homeModel = userModel.listHomeModel.get(0);
            if(!homeModel.address1.equals("")){
                tvAddress.setText(homeModel.address1 + " " + homeModel.address2 + " " + homeModel.address3 + " " + homeModel.address4);
            }

            if(!homeModel.homeSize.equals("")){
                tvHomeInfo.setText("집 정보가 등록되어있습니다.");
            }
        }else{
            ;;
        }
    }

    @OnClick({R.id.fragmentMRRequestorInfo_vbtnAddress})
    public void onAddressClick(View v){
        DialogController.showCenterDialog(getContext(), new HouseAddressDialog(userModel.listHomeModel.get(0), true, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                boolean isSuccess = (Boolean)params.get("SUCCESS");
                if(isSuccess){
                    refresh();
                }
            }
        }));
    }

    //이름
    @OnClick(R.id.fragmentMRRequestorInfo_vbtnName)
    public void onNameClick(View v){
        final String name = tvName.getText().toString();

        CommonInputDialog commonInputDialog = new CommonInputDialog("Change your name", "What is your name?", name, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String method = (String)params.get("ONCLICK");
                String changedName = (String)params.get("TEXT");

                if(method.equals("OK")){
                    final int pId = ProgressDialogController.show(getContext());
                    RestClient.getUserRestClient().updateUserSingleInfo(CurrentUserInfo.getId(getContext()), "name", changedName).enqueue(new RetrofitCallback<String>() {

                        @Override
                        public void onFinish() {
                            ProgressDialogController.dismiss(pId);
                        }

                        @Override
                        public void onSuccess(String result) {
                            refresh();
                        }

                        @Override
                        public void onJodevError(ErrorModel jodevErrorModel) {
                            Util.showToast(getContext(), jodevErrorModel.message);
                        }
                    });
                }

            }
        });

        DialogController.showCenterDialog(getContext(), commonInputDialog);
        commonInputDialog.getEtInput().setSingleLine(true);
        commonInputDialog.getEtInput().setHint("Input your name");
    }

    //홈 엑스트라 정보
    @OnClick(R.id.fragmentMRRequestorInfo_vbtnHomeInfo)
    public void onUpdatehomeClick(View v){

        //다이얼로그띄움
        DialogController.showBottomDialog(getContext(), new HomeInformationDialog(userModel.listHomeModel.get(0),
                new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {
                        boolean isSuccess = (Boolean)params.get("SUCCESS");
                        if(isSuccess){
                            refresh();
                        }
                    }
                })
        ).show();

    }

    @OnClick(R.id.fragmentMRRequestorInfo_vbtnPhone)
    public void onPhoneClick(View v){
        String currentPhone = userModel.phone;
        DialogController.showCenterDialog(getContext(), new ChangePhoneNumberDialog(getActivity(), currentPhone, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String method = Util.getMapValue(params, "ONCLICK");
                if(method.equals("OK")){
                    getUserInfo();
                }else{
                    //캔슬
                }
            }
        }));
    }


}

