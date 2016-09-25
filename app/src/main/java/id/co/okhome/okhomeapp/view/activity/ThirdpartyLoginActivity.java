package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.lib.thridpartylogin.JoFacebookLogin;
import id.co.okhome.okhomeapp.lib.thridpartylogin.JoGoogleLogin;
import id.co.okhome.okhomeapp.lib.thridpartylogin.JoThirdpartyLogin;
import id.co.okhome.okhomeapp.lib.thridpartylogin.JoThirdpartyLoginListener;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class ThirdpartyLoginActivity extends OkHomeActivityParent implements JoThirdpartyLoginListener{

    public static final String TAG_3PARTY_NAME  = "3PARTY_NAME";
    public static final String FACEBOOK         = "FB";
    public static final String GOOGLE           = "GG";


    JoThirdpartyLogin thirdpartyLogin;
    String partyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdparty_login);
        Util.Log("ThirdpartyLoginActivity onCreate");
        //페북, 구글 로그인 분기
        partyName = getIntent().getStringExtra(TAG_3PARTY_NAME);
        if (partyName == null) {
            partyName = FACEBOOK;
        }
        if(partyName.equals(FACEBOOK)){
            thirdpartyLogin = new JoFacebookLogin();
        }else{
            partyName = GOOGLE;
            thirdpartyLogin = new JoGoogleLogin();

        }

        thirdpartyLogin.init(this);
        thirdpartyLogin.setLoginListener(this);

        //로그인 시작
        thirdpartyLogin.login();
    }

    //로그인성공시 콜백 param에 다 들어가있음
    @Override
    public void onLogin(Map<String, Object> params) {

        String authKey = (String)params.get(JoThirdpartyLogin.PARAM_AUTHKEY);

        String imgUrl = (String)params.get(JoThirdpartyLogin.PARAM_IMG_URL);
        String name = (String)params.get(JoThirdpartyLogin.PARAM_NAME);
        String id = (String)params.get(JoThirdpartyLogin.PARAM_ID);
        String email = (String)params.get(JoThirdpartyLogin.PARAM_EMAIL);

        //로그인
        authentication(partyName, authKey, id, email, name, imgUrl);
    }

    private void onAuthenticationFinish(){
        OkHomeActivityParent.finishAllActivities();
        startActivity(new Intent(this, MainActivity.class));
//        setResult(RESULT_OK);
//        finish();
    }

    private void authentication(String type, String authKey, String id, String email, String name, String imgUrl){
        final int pId = ProgressDialogController.show(this);

        RestClient.getUserRestClient().authentication(type, authKey, email, name, "", imgUrl).enqueue(new RetrofitCallback<UserModel>() {
            @Override
            public void onSuccess(UserModel result) {
                CurrentUserInfo.set(ThirdpartyLoginActivity.this, result);
                ProgressDialogController.dismiss(pId);

                onAuthenticationFinish();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(ThirdpartyLoginActivity.this, jodevErrorModel.message);
                ProgressDialogController.dismiss(pId);
                finish();
            }
        });

    }

    @Override
    public void onFailed(String message) {
        Util.Log("failed : " + message);
    }

    @Override
    public void onLogout() {
        Util.Log("logout : ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        thirdpartyLogin.onActivityResult(requestCode, resultCode, data);
    }
}
