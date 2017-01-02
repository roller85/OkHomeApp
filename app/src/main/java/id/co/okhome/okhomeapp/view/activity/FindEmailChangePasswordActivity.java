package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.Variables;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.SCryptPassword;
import id.co.okhome.okhomeapp.lib.SmsReceiver;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class FindEmailChangePasswordActivity extends OkHomeActivityParent {

    @BindView(R.id.actFECP_vgCheckAndChangeInfo)        ViewGroup vgCheckAndChangeInfo;
    @BindView(R.id.actFECP_vgVerification)              ViewGroup vgVertification;
    @BindView(R.id.actFECP_vgConfirmVerfication)        ViewGroup vgConfirmVerfication;
    @BindView(R.id.actFECP_vgVerificationCode)          ViewGroup vgVerificationCode;

    @BindView(R.id.actFECP_etNewPassword)               EditText etNewPassword;
    @BindView(R.id.actFECP_etNewPassword1More)          EditText etNewPassword1More;
    @BindView(R.id.actFECP_etPhone)                     EditText etPhone;
    @BindView(R.id.actFECP_etVerificationCode)          EditText etVerificationCode;

    @BindView(R.id.actFECP_tvEmail)                     TextView tvEmail;
    @BindView(R.id.actFECP_vbtnChangePassword)          View vbtnChangePassword;
    @BindView(R.id.actFECP_vbtnConfirmVerificationCode) View vbtnConfirmVerificationCode;
    @BindView(R.id.actFECP_vbtnSendVerificationCode)    View vbtnSendVerificationCode;


    String phoneNumberProgressed ="";
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findemail_changepass);
        ButterKnife.bind(this);

        //가시상태 바꾸기
        vgCheckAndChangeInfo.setVisibility(View.GONE);
        vgVertification.setVisibility(View.VISIBLE);

        vgVerificationCode.setVisibility(View.GONE);
        vgConfirmVerfication.setVisibility(View.GONE);
    }

    //인증코드 보내기
    @OnClick({R.id.actFECP_vbtnSendVerificationCode, R.id.actFECP_vbtnResendVerificationCode})
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
        RestClient.getCertificationClient().sendCertCode(phone, "N").enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(String result) {
                Util.showToast(FindEmailChangePasswordActivity.this, "You can receive verification code after a while");
                vbtnSendVerificationCode.setVisibility(View.GONE);
                vgConfirmVerfication.setVisibility(View.VISIBLE);
                vgVerificationCode.setVisibility(View.VISIBLE);


            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(FindEmailChangePasswordActivity.this, jodevErrorModel.message);
            }
        });

    }

    //이메일로 정보 가져오기
    private void getUserEmail(String phone){

        final int pId = ProgressDialogController.show(this);

        RestClient.getUserRestClient().getUserByPhone(phone).enqueue(new RetrofitCallback<UserModel>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(UserModel result) {

                if(result.accountType.equals("EMAIL")){
                    Util.showToast(FindEmailChangePasswordActivity.this, "Verification complete");

                    vgCheckAndChangeInfo.setVisibility(View.VISIBLE);
                    vgVertification.setVisibility(View.GONE);

                    vgCheckAndChangeInfo.setAlpha(0f);
                    vgCheckAndChangeInfo.animate().translationY(0).alpha(1f).setDuration(1000).start();

                    tvEmail.setText(result.email);
                    userEmail = result.email;

                }else{
                    //이메일 아닌경우..
                    String regType = result.accountType.equals("GG") ? "Google account" : "Facebook account";
                    Util.showToast(FindEmailChangePasswordActivity.this, "This phone number was registered with \n" + regType + " (" + result.email + ")");
                }


            }
        });
    }


    //인증번호 최종 확인
    @OnClick(R.id.actFECP_vbtnConfirmVerificationCode)
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
                getUserEmail(phoneNumberProgressed);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(FindEmailChangePasswordActivity.this, jodevErrorModel.message);
            }
        });
    }

    @OnClick(R.id.actFECP_vbtnChangePassword)
    public void onBtnChangePassword(View v){
        final String password = etNewPassword.getText().toString();
        String passwordConfirm = etNewPassword1More.getText().toString();

        try{
            if(!password.equals(passwordConfirm)){
                throw new OkhomeException("Password is not matched");
            }

            if(password.length() < Variables.PASSWORD_MIN_LENGTH){
                throw new OkhomeException("Password length must be at least " + Variables.PASSWORD_MIN_LENGTH);
            }
        }catch(OkhomeException e){
            Util.showToast(this, e.getMessage());
            return;
        }

        final int pId = ProgressDialogController.show(this);

        new Thread() {
            String encryptPass = null;

            @Override
            public void run() {
                encryptPass = SCryptPassword.makePassword(password);

                //얻베이트
                RestClient.getUserRestClient().updatePassword(userEmail, encryptPass).enqueue(new RetrofitCallback<String>() {

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogController.dismiss(pId);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Util.showToast(FindEmailChangePasswordActivity.this, "Your password is changed");
                        finish();
                    }

                });
            }
        }.start();

    }


}
