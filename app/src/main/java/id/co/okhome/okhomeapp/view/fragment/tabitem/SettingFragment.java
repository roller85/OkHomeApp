package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.content.Intent;
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
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.activity.MainActivity;
import id.co.okhome.okhomeapp.view.activity.SigninActivity;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;

/**
 * Created by josongmin on 2016-07-28.
 */

public class SettingFragment extends Fragment implements TabFragmentFlow {

    @BindView(R.id.fragmentSetting_ivPushChk)           ImageView ivPushChk;
    @BindView(R.id.fragmentSetting_ivSoundChk)          ImageView ivSoundChk;
    @BindView(R.id.fragmentSetting_ivVibeChk)           ImageView ivVibeChk;
    @BindView(R.id.fragmentSetting_tvCurrentVersion)    TextView tvCurrentVersion;
    @BindView(R.id.fragmentSetting_tvVersionSubText)    TextView tvVersionText;

    UserModel userModel;


    @Override
    public String getTitle() {
        return "Setting";
    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        getUserInfo();
        getVersionInfo();
    }

    private void updateUserView(){
        if(userModel.pushYN.equals("Y")){
            ivPushChk.setImageResource(R.drawable.ic_checked);
        }else{
            ivPushChk.setImageResource(R.drawable.ic_check_not);
        }

        if(userModel.vibeYN.equals("Y")){
            ivVibeChk.setImageResource(R.drawable.ic_checked);
        }else{
            ivVibeChk.setImageResource(R.drawable.ic_check_not);
        }

        if(userModel.soundYN.equals("Y")){
            ivSoundChk.setImageResource(R.drawable.ic_checked);
        }else{
            ivSoundChk.setImageResource(R.drawable.ic_check_not);
        }
    }

    private void updateSettingViews(){
        String version = Util.getAppVersionName(getActivity());
        tvCurrentVersion.setText(version);
    }

    //저장되있는 유저인포가져오기
    private void getUserInfo(){
        userModel = CurrentUserInfo.get(getContext());
        updateUserView();
    }

    //유저정보 업데이트(프로그래스 없음)
    private void updateUserInfo(String key, String value){
        //프리퍼런스 갱신
        CurrentUserInfo.set(getContext(), userModel);

        RestClient.getUserRestClient().updateUserSingleInfo(CurrentUserInfo.getId(getContext()), key, value).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ;
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                ;
            }
        });
    }

    //세팅정보 가져오기
    private void getVersionInfo(){
        updateSettingViews();
        tvVersionText.setText("Loading");
        String key = "VERSION_ANDROID";
        RestClient.getCommonClient().getVariableValue(key).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //version 갖고옴
                if(!Util.getAppVersionName(getActivity()).equals(result)){
                    //다름..
                    tvVersionText.setText("New version available");
                }else{
                    tvVersionText.setText("The latest version");
                }
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                tvVersionText.setText(jodevErrorModel.message);
            }
        });
    }

    @OnClick(R.id.fragmentSetting_vPushChk)
    public void onPushChkClick(View v){
        userModel.pushYN = userModel.pushYN.equals("Y") ? "N" : "Y";
        updateUserView();
        updateUserInfo("pushYN", userModel.pushYN);
    }

    @OnClick(R.id.fragmentSetting_vSoundChk)
    public void onSoundChkClick(View v){
        userModel.soundYN = userModel.soundYN.equals("Y") ? "N" : "Y";
        updateUserView();
        updateUserInfo("soundYN", userModel.soundYN);
    }

    @OnClick(R.id.fragmentSetting_vVibeChk)
    public void onVibeChkClick(View v){
        userModel.vibeYN = userModel.vibeYN.equals("Y") ? "N" : "Y";
        updateUserView();
        updateUserInfo("vibeYN", userModel.vibeYN);
    }

    @OnClick(R.id.fragSetting_vbtnDeleteAccount)
    public void onDeleteClick(){
        DialogController.showCenterDialog(getContext(),
                new CommonTextDialog("Really cancel your account?", "Your profile and any listings will disappear.\nWe'll miss you terribly", new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {
                        final int pId = ProgressDialogController.show(getContext());
                        RestClient.getUserRestClient().quit(CurrentUserInfo.getId(getContext())).enqueue(new RetrofitCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                ProgressDialogController.dismiss(pId);
                                getActivity().finish();
                                startActivity(new Intent(getContext(), SigninActivity.class));
                            }

                            @Override
                            public void onJodevError(ErrorModel jodevErrorModel) {
                                ProgressDialogController.dismiss(pId);
                                Util.showToast(getContext(), jodevErrorModel.message);
                            }
                        });
                    }
                }));
    }

    @OnClick(R.id.fragSetting_vbtnLogout)
    public void onLogoutClick(){
        DialogController.showCenterDialog(getContext(),
                new CommonTextDialog("Really logout?", "We'll miss you terribly", new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {

                        String onClick = (String)params.get("ONCLICK");
                        if(onClick.equals("OK")){
                            //로그아웃 프로세스
                            final int pId = ProgressDialogController.show(getContext());
                            RestClient.getUserRestClient().logout(CurrentUserInfo.getId(getContext())).enqueue(new RetrofitCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    CurrentUserInfo.clear(getContext());
                                    ProgressDialogController.dismiss(pId);
                                    getActivity().finish();
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                }

                                @Override
                                public void onJodevError(ErrorModel jodevErrorModel) {
                                    ProgressDialogController.dismiss(pId);
                                    Util.showToast(getContext(), jodevErrorModel.message);
                                }
                            });
                        }

                    }
                }));
    }
}
