package id.co.okhome.okhomeapp.lib.thridpartylogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import id.co.okhome.okhomeapp.lib.Util;

import static com.facebook.Profile.getCurrentProfile;

/**
 * Created by josongmin on 2016-08-28.
 */

public class JoFacebookLogin extends JoThirdpartyLogin{

    Activity activity;
    CallbackManager callbackManager;

    @Override
    public void init(Activity activity) {
        this.activity = activity;

        FacebookSdk.sdkInitialize(activity);

        //디버깅모드에서만 쓰자
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                final Profile profile = getCurrentProfile();
                if(profile == null){
                    //에러처리
                    loginListener.onFailed("success but can not get profile");
                }else{
                    getUserInfoBeforeLogin(loginResult.getAccessToken());
                }
            }

            @Override
            public void onCancel() {
                loginListener.onFailed("Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
                loginListener.onFailed(error.toString());
            }
        });
    }

    private void getUserInfoBeforeLogin(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String name = object.getString("name");

                            onLoginSuccess(id, name, email);

                        }catch(Exception e){
                            ;
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void onLoginSuccess(String id, String name, String email){
        Map param = Util.makeMap(
                JoThirdpartyLogin.PARAM_IMG_URL, Profile.getCurrentProfile().getProfilePictureUri(500,500).toString(),
                JoThirdpartyLogin.PARAM_AUTHKEY, AccessToken.getCurrentAccessToken().getToken(),
                JoThirdpartyLogin.PARAM_ID, id,
                JoThirdpartyLogin.PARAM_NAME, name,
                JoThirdpartyLogin.PARAM_EMAIL, email);

        loginListener.onLogin(param);
    }

    @Override
    public void login() {
//        Profile profile = Profile.getCurrentProfile();
//
//        if(profile != null){
//            //이미 로그인되어있음.
//            getUserInfoBeforeLogin(AccessToken.getCurrentAccessToken());
//        }else{
//            //로그인 액티비티 실행
//            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("id", "name", "email"));
//        }

        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void logout() {
        AccessToken.setCurrentAccessToken(null);
        LoginManager.getInstance().logOut();
        loginListener.onLogout();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
