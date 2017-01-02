package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.config.Variables;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.SCryptPassword;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class SignupActivity extends OkHomeActivityParent {

    @BindView(R.id.actSignUp_etEmail)                   EditText etEmail;
    @BindView(R.id.actSignUp_etName)                    EditText etName;
    @BindView(R.id.actSignUp_etPass)                    EditText etPass;
    @BindView(R.id.actSignUp_etPassConfirm)             EditText etPassConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.actSignUp_tvbtnPolicy)
    public void onClickPolicy(View v){
        startActivity(new Intent(this, WebpageActivity.class).putExtra(WebpageActivity.TAG_WEBTYPE, WebpageActivity.WEBTYPE_PRIVACYPOLICY));
    }

    @OnClick(R.id.actSignUp_tvbtnTerm)
    public void onClickTerm(View v){
        startActivity(new Intent(this, WebpageActivity.class).putExtra(WebpageActivity.TAG_WEBTYPE, WebpageActivity.WEBTYPE_TERMSOFSERVICE));
    }

    @OnClick(R.id.actSignUp_vbtnJoin)
    public void onClickJoin(View v){
        join();
    }

    private void onLogin(UserModel userModel){
        CurrentUserInfo.set(this, userModel);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private void getUserInfo(String id){
        final int pId = ProgressDialogController.show(this);

        RestClient.getUserRestClient().getUser(id).enqueue(new RetrofitCallback<UserModel>() {
            @Override
            public void onSuccess(UserModel result) {
                ProgressDialogController.dismiss(pId);
                onLogin(result);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                ProgressDialogController.dismiss(pId);
            }
        });
    }

    public void join(){
        final String email = etEmail.getText().toString();
        final String pass = etPass.getText().toString();
        final String passConfirm = etPassConfirm.getText().toString();
        final String name = etName.getText().toString();

        try{
            if(!Util.isValidEmail(email)){
                throw new OkhomeException("Invalid email");
            }

            if(pass.length() < Variables.PASSWORD_MIN_LENGTH){
                throw new OkhomeException("Minimum password length is " + Variables.PASSWORD_MIN_LENGTH);
            }

            if(!pass.equals(passConfirm)){
                throw new OkhomeException("Check your password");
            }

            if(name.length() <= 0){
                throw new OkhomeException("Input your name");
            }

        }catch(OkhomeException e){
            Util.showToast(this, e.getMessage());
            return;
        }

        final int pId = ProgressDialogController.show(SignupActivity.this);

        new Thread(){
            String encryptPass = null;
            @Override
            public void run() {
                encryptPass = SCryptPassword.makePassword(pass);
                RestClient.getUserRestClient().join(name, email, "", encryptPass, "").enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String id) {
                        ProgressDialogController.dismiss(pId);
                        getUserInfo(id);
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        ProgressDialogController.dismiss(pId);
                        Util.showToast(SignupActivity.this, jodevErrorModel.message);
                    }
                });
            }
        }.start();
    }

    @OnClick(R.id.actSignup_vbtnFb)
    public void onFbLogin(View v){
        startActivityForResult(new Intent(this, ThirdpartyLoginActivity.class).putExtra(ThirdpartyLoginActivity.TAG_3PARTY_NAME, ThirdpartyLoginActivity.FACEBOOK), 100);
    }

    @OnClick(R.id.actSignup_vbtnGoogle)
    public void onGoogleLogin(View v){
        startActivityForResult(new Intent(this, ThirdpartyLoginActivity.class).putExtra(ThirdpartyLoginActivity.TAG_3PARTY_NAME, ThirdpartyLoginActivity.GOOGLE), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK ){
            finish();
        }

    }


}
