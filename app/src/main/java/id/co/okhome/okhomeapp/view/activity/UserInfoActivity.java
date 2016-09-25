package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.OkHomeUtil;
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
import id.co.okhome.okhomeapp.view.dialog.HomeInformationDialog;
import id.co.okhome.okhomeapp.view.dialog.HouseAddressDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoActivity extends OkHomeActivityParent implements CurrentUserInfo.OnUserInfoChangeListener{

    @BindView(R.id.actUserInfo_tvPhone)         TextView tvPhone;
    @BindView(R.id.actUserInfo_tvEmail)         TextView tvEmail;
    @BindView(R.id.actUserInfo_tvName)          TextView tvName;
    @BindView(R.id.actUserInfo_tvAddress)       TextView tvAddress;
    @BindView(R.id.actUserInfo_tvHomeinfo)      TextView tvHomeInfo;
    @BindView(R.id.actUserInfo_ivPhoto)         ImageView ivPhoto;
    @BindView(R.id.actUserInfo_vLoading)        View vLoading;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        ButterKnife.bind(this);
        init();

        CurrentUserInfo.registerUserInfoChangeListener(this);
        getUserInfo();
    }


    @Override
    protected void onPause() {
        CurrentUserInfo.unregisterUserInfoChangeListener(this);
        super.onPause();
    }


    //유저정보 불ㅇ러괴
    private void getUserInfo(){
        vLoading.setVisibility(View.VISIBLE);
        RestClient.getUserRestClient().getUser(CurrentUserInfo.get(this).id).enqueue(new RetrofitCallback<UserModel>() {

            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);

            }

            @Override
            public void onSuccess(UserModel result) {
                CurrentUserInfo.set(UserInfoActivity.this, result); //invoke함
                onUserInfoChanged(result);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(UserInfoActivity.this, jodevErrorModel.message);
            }
        });
    }


    @Override
    public void onUserInfoChanged(UserModel userModel) {
        this.userModel = userModel;
        tvEmail.setText(userModel.email);
        tvPhone.setText(userModel.phone);
        tvName.setText(userModel.name);
        CurrentUserInfo.loadUserImg(this, userModel.photoUrl, ivPhoto);

        //집정보
        if(userModel.listHomeModel != null && userModel.listHomeModel.size() > 0){
            HomeModel homeModel = userModel.listHomeModel.get(0);
            if(!homeModel.address1.equals("")){
                tvAddress.setText(homeModel.address1 + " " + homeModel.address2 + " " + homeModel.address3 + " " + homeModel.address4);
            }

            if(!homeModel.homeSize.equals("")){
                tvHomeInfo.setText(homeModel.type +", " + homeModel.homeSize + ", " + homeModel.restroomCnt + " restroom, " + homeModel.floorCnt + " floor");
            }
        }else{
            ;;
        }
    }


    private void init(){
        OkHomeUtil.setBackbtnListener(this);
    }

    private void refresh(){
        getUserInfo();
    }

    @OnClick(R.id.actUserInfo_vbtnPhoto)
    public void onPhotoClick(View v){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), ImageChooserActivity.REQ_GET_IMAGE);
    }

    @OnClick({R.id.actUserInfo_vbtnAddress})
    public void onAddressClick(View v){
        DialogController.showCenterDialog(this, new HouseAddressDialog(userModel.listHomeModel.get(0).id, true, new ViewDialog.DialogCommonCallback() {
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
    @OnClick(R.id.actUserInfo_vbtnName)
    public void onNameClick(View v){
        final String name = tvName.getText().toString();
        DialogController.showCenterDialog(this, new CommonInputDialog("Change your name", "What is your name?", name, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
//                Util.makeMap("ONCLICK", "OK", "TEXT", etInput.getText().toString()));
                String method = (String)params.get("ONCLICK");
                String changedName = (String)params.get("TEXT");

                if(method.equals("OK")){
                    final int pId = ProgressDialogController.show(UserInfoActivity.this);
                    RestClient.getUserRestClient().updateUserSingleInfo(CurrentUserInfo.getId(UserInfoActivity.this), "name", changedName).enqueue(new RetrofitCallback<String>() {

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
                            Util.showToast(UserInfoActivity.this, jodevErrorModel.message);
                        }
                    });
                }

            }
        }));
    }

    //홈 엑스트라 정보
    @OnClick(R.id.actUserInfo_vbtnHomeInfo)
    public void onUpdatehomeClick(View v){

        //다이얼로그띄움
        DialogController.showBottomDialog(this, new HomeInformationDialog(userModel.listHomeModel.get(0),
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

    @OnClick(R.id.actUserInfo_vbtnPhone)
    public void onPhoneClick(View v){
        String currentPhone = userModel.phone;
        DialogController.showCenterDialog(this, new ChangePhoneNumberDialog(currentPhone, new ViewDialog.DialogCommonCallback() {
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

    private void uploadFile(String filePath, RetrofitCallback<String> callback){
        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file1", file.getName(), requestFile);

        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        RestClient.getCommonClient().uploadFile(description, body).enqueue(callback);
    }

    private void onPhotoChoosed(String imgFilePath){
        final int pId = ProgressDialogController.show(this);
        uploadFile(imgFilePath, new RetrofitCallback<String>() {


            @Override
            public void onSuccess(String result) {
                //경로 업데이트
                RestClient.getUserRestClient().updateUserSingleInfo(CurrentUserInfo.getId(UserInfoActivity.this), "photoUrl", result).enqueue(new RetrofitCallback<String>() {

                    @Override
                    public void onFinish() {
                        ProgressDialogController.dismiss(pId);
                    }

                    @Override
                    public void onSuccess(String result) {
                        //재시작
                        refresh();
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        Util.showToast(UserInfoActivity.this, jodevErrorModel.message);
                    }
                });
                Util.Log(result);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                ProgressDialogController.dismiss(pId);
                Util.showToast(UserInfoActivity.this, jodevErrorModel.message);
                Util.Log(jodevErrorModel.message);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ImageChooserActivity.REQ_GET_IMAGE){
            String imgPath = data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH);
            Util.Log(imgPath);

            onPhotoChoosed(imgPath);
        }
    }














}
