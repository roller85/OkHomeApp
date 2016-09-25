package id.co.okhome.okhomeapp.view.activity;

import android.app.ProgressDialog;
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
import id.co.okhome.okhomeapp.lib.SCryptPassword;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class SigninActivity extends OkHomeActivityParent {

    @BindView(R.id.actSignin_etEmail)           EditText etEmail;
    @BindView(R.id.actSignin_etPass)            EditText etPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ButterKnife.bind(this);
    }
    @OnClick(R.id.fragmentMakeReservationGuest_vbtnLogin)
    public void onLogin(View v){
        final String email = etEmail.getText().toString();
        final String pass = etPass.getText().toString();

        //검증
        try{
            if(!Util.isValidEmail(email)){
                throw new OkhomeException("Invalid email");
            }

            if(pass.length() < Variables.PASSWORD_MIN_LENGTH){
                throw new OkhomeException("Minimum password length is " + Variables.PASSWORD_MIN_LENGTH);
            }

        }catch(OkhomeException e){
            Util.showToast(this, e.getMessage());
            return;
        }

        final ProgressDialog pd = ProgressDialog.show(SigninActivity.this, null, "Loading");

        new Thread(){
            String passEncrypt = "";
            @Override
            public void run() {
                super.run();
                passEncrypt = SCryptPassword.makePassword(pass);
                RestClient.getUserRestClient().login(email, passEncrypt).enqueue(new RetrofitCallback<UserModel>() {
                    @Override
                    public void onSuccess(UserModel result) {
                        pd.dismiss();
                        onLoginSuccess(result);

                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        Util.showToast(SigninActivity.this, jodevErrorModel.message);
                        pd.dismiss();
                    }

                });
            }
        }.start();



    }

    //로그인성공
    private void onLoginSuccess(UserModel userModel){
        CurrentUserInfo.set(this, userModel);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.actSignin_vbtnFb)
    public void onFbLogin(View v){
        startActivityForResult(new Intent(this, ThirdpartyLoginActivity.class).putExtra(ThirdpartyLoginActivity.TAG_3PARTY_NAME, ThirdpartyLoginActivity.FACEBOOK), 100);
    }

    @OnClick(R.id.actSignin_vbtnGoogle)
    public void onGoogleLogin(View v){
        startActivityForResult(new Intent(this, ThirdpartyLoginActivity.class).putExtra(ThirdpartyLoginActivity.TAG_3PARTY_NAME, ThirdpartyLoginActivity.GOOGLE), 100);
    }

    @OnClick(R.id.actSignin_tvbtnForgetPassword)
    public void onForgotPassword(View v){
        ;
    }

    @OnClick(R.id.actSignin_tvbtnSignUp)
    public void onSignup(View v){
        startActivity(new Intent(this, SigninActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK ){
            finish();
        }

    }
}


