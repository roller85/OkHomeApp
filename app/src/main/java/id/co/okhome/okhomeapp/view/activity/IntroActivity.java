package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.Map;

import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.JoSharedPreference;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.TutoriorPreference;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class IntroActivity extends OkHomeActivityParent {


    boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        TutoriorPreference.clear();
        JoSharedPreference.with().push("ExtraCleaningList", null);

        getVersion();

        //셋
    }

    private void getVersion(){
        RestClient.getCommonClient().getVersion("android").enqueue(new RetrofitCallback<Map>() {
            @Override
            public void onSuccess(Map result) {
                chkVersion((String)result.get("version"), (String)result.get("mandatory"), (String)result.get("text"), (String)result.get("url"));
            }
        });
    }

    private void moveToUrl(String url){
        if(url.contains("market://")){
            Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
            marketLaunch.setData(Uri.parse(url));
            startActivity(marketLaunch);
        }else{
            //웹
            Util.openWebIntent(this, url);
        }
        finish();
    }


    private void chkVersion(String version, String isMandatory, String text, final String url){
        String appVersion = Util.getAppVersionName(this);
        int iAppVersion = Integer.parseInt(appVersion.replace(".", ""));
        int iNewVersion = Integer.parseInt(version.replace(".", ""));

        if(iNewVersion > iAppVersion){
            if(isMandatory.equals("Y")){
                //업데이트 후에 진행가능

                DialogController.showAlertDialog(this, "Okhome 필수업데이트", text, true, new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {
                        //마켓으로 이동
                        moveToUrl(url);
                    }
                });
            }else{
                DialogController.showAlertDialog(this, "Okhome 필수업데이트", text, false, new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {
                        //마켓으로 이동
                        if(params.get(DialogController.TAG_CLICK).equals("OK")){
                            login();
                        }else{
                            moveToUrl(url);
                        }
                    }
                });
                //업데이트 권고
            }

        }else{
            login();
        }

    }

    private void login(){
        final String id = CurrentUserInfo.getId(this);
        if(id == null){
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }else{
            RestClient.getUserRestClient().getUser(id).enqueue(new RetrofitCallback<UserModel>() {

                @Override
                public void onSuccess(UserModel result) {
                    CurrentUserInfo.set(IntroActivity.this, result);
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onJodevError(ErrorModel jodevErrorModel) {
                    Util.showToast(IntroActivity.this, "getUser " + id + jodevErrorModel.message);
                    finish();
                }
            });
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(started == false){
            animate();
            started =true;
        }
    }

    private void animate(){

        View[] arrViewCharacter = new View[]{
                findViewById(R.id.actIntro_ivO), findViewById(R.id.actIntro_ivK), findViewById(R.id.actIntro_ivH),
                findViewById(R.id.actIntro_ivO2), findViewById(R.id.actIntro_ivM), findViewById(R.id.actIntro_ivE)
        };

        final View vJakarta = findViewById(R.id.actIntro_ivJakrta);
        final View vSymbol = findViewById(R.id.actIntro_ivSymbol);
        final View vgCharac = findViewById(R.id.actIntro_vgCharacter);

        vSymbol.setAlpha(0);
        vSymbol.animate().alpha(1f).setDuration(1000).start();

        vJakarta.setAlpha(0);
        vJakarta.animate().alpha(1f).setDuration(2000).start();

        vgCharac.setAlpha(0);
        vgCharac.animate().alpha(1f).setDuration(1800).start();

        vSymbol.setX(vSymbol.getX() - vSymbol.getWidth()/10);
        vSymbol.animate().translationX(0).setDuration(2000).start();
//
        vJakarta.setX(vJakarta.getX() - vJakarta.getWidth()/10);
        vJakarta.animate().translationX(0).setDuration(2000).start();

        for(int i = 0; i < arrViewCharacter.length; i++){
            View vTarget = arrViewCharacter[i];
            vTarget.setX(vTarget.getX() - vTarget.getWidth() / (i+1) * 2f);
            vTarget.animate().translationX(0).setDuration(2000)
                    .setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.accelerate_decelerate_interpolator)).start();
        }


    }

}
