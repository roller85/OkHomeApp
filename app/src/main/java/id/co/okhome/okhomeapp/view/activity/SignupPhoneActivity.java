package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.SmsReceiver;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class SignupPhoneActivity extends OkHomeActivityParent {

    @BindView(R.id.actSignupPhone_vgVerification)              ViewGroup vgVertification;
    @BindView(R.id.actSignupPhone_vgConfirmVerfication)        ViewGroup vgConfirmVerfication;
    @BindView(R.id.actSignupPhone_vgVerificationCode)          ViewGroup vgVerificationCode;

    @BindView(R.id.actSignupPhone_etPhone)                     EditText etPhone;
    @BindView(R.id.actSignupPhone_etVerificationCode)          EditText etVerificationCode;

    @BindView(R.id.actSignupPhone_vbtnConfirmVerificationCode) View vbtnConfirmVerificationCode;
    @BindView(R.id.actSignupPhone_vbtnSendVerificationCode)    View vbtnSendVerificationCode;

    @BindView(R.id.activitySignupPhone_tvHello)                TextView tvHello;

    String phoneNumberProgressed ="";
    String userEmail;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_phone);
        ButterKnife.bind(this);

        UserModel user =  CurrentUserInfo.get(this);

        userId = CurrentUserInfo.getId(this);

        //가시상태 바꾸기
        vgVertification.setVisibility(View.VISIBLE);

        vgVerificationCode.setVisibility(View.GONE);
        vgConfirmVerfication.setVisibility(View.GONE);

        tvHello.setText("Hello! our valuable customer \"" + user.name +"\"");
    }

    private void updatePhoneNumber(){
        //여기 넘어올대 회원 아이디 가져와야댐.

        final int pid = ProgressDialogController.show(this);
        RestClient.getUserRestClient().updatePhone(userId, phoneNumberProgressed).enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pid);
            }

            @Override
            public void onSuccess(String result) {
                //메인으로 고고..
                UserModel userModel = CurrentUserInfo.get(SignupPhoneActivity.this);
                userModel.phone = phoneNumberProgressed;
                CurrentUserInfo.set(SignupPhoneActivity.this, userModel);

                startActivity(new Intent(SignupPhoneActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    //인증코드 보내기
    @OnClick({R.id.actSignupPhone_vbtnSendVerificationCode, R.id.actSignupPhone_vbtnResendVerificationCode})
    public void onBtnSendVerificationCode(View v){

        //send
        String phone = etPhone.getText().toString();
        if(phone.length() < 5){
            Util.showToast(this, "Check your phone number");
            return;
        }

        phoneNumberProgressed = phone;
        new SmsReceiver(this).init(new SmsReceiver.OnSmsReceivedListener() {
            @Override
            public void onSmsReceive(String message) {
                //원본
            }

            @Override
            public void onOkhomeVerificationSmsReceive(String message) {
                etVerificationCode.setText(message);
            }
        });

        final int pId =ProgressDialogController.show(this);
        RestClient.getCertificationClient().sendCertCode(phone, "Y").enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(String result) {
                Util.showToast(SignupPhoneActivity.this, "You can receive verification code after a while");
                vbtnSendVerificationCode.setVisibility(View.GONE);
                vgConfirmVerfication.setVisibility(View.VISIBLE);
                vgVerificationCode.setVisibility(View.VISIBLE);


            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(SignupPhoneActivity.this, jodevErrorModel.message);
            }
        });

    }

    //인증번호 최종 확인
    @OnClick(R.id.actSignupPhone_vbtnConfirmVerificationCode)
    public void onBtnConfirmVerificationCode(View v){

        //확인 api 호출

        final String code = etVerificationCode.getText().toString();
        if(code.length() < 4 ) return;

        final int pId = ProgressDialogController.show(this);

        RestClient.getCertificationClient().chkValidCode(phoneNumberProgressed, code).enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(Boolean result) {
                updatePhoneNumber();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(SignupPhoneActivity.this, jodevErrorModel.message);
            }
        });
    }



}
