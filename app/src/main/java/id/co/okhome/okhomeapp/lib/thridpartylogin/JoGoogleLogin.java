package id.co.okhome.okhomeapp.lib.thridpartylogin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.Map;

import id.co.okhome.okhomeapp.lib.Util;

/**
 * Created by josongmin on 2016-08-28.
 */

public class JoGoogleLogin extends JoThirdpartyLogin{

    private final static int RC_SIGNIN = 11231;
    Activity activity;

    GoogleApiClient googleApiClient;

    @Override
    public void init(Activity activity) {
        this.activity = activity;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage((FragmentActivity)activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        loginListener.onFailed(connectionResult.toString());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    public void login() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGNIN);
    }

    @Override
    public void logout() {

        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                loginListener.onLogout();
            }
        });

    }

    private void onLoginSuccess(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();



            Map param = Util.makeMap(
                    JoThirdpartyLogin.PARAM_AUTHKEY, "",
                    JoThirdpartyLogin.PARAM_IMG_URL, acct.getPhotoUrl().toString(),
                    JoThirdpartyLogin.PARAM_EMAIL, acct.getEmail(),
                    JoThirdpartyLogin.PARAM_ID, acct.getId(),
                    JoThirdpartyLogin.PARAM_NAME, acct.getDisplayName(),
                    JoThirdpartyLogin.PARAM_RAW, acct);

            loginListener.onLogin(param);
        } else {
            loginListener.onLogout();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGNIN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            onLoginSuccess(result);
        }
    }
}
