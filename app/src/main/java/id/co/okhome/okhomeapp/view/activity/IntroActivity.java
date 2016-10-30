package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.List;

import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.JoSharedPreference;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CleaningModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class IntroActivity extends OkHomeActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        new android.os.Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                initDefaultDatas();

            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    private void initDefaultDatas(){
        RestClient.getCleaningClient().getCleaningList().enqueue(new RetrofitCallback<List<CleaningModel>>() {
            @Override
            public void onSuccess(List<CleaningModel> result) {
                JoSharedPreference.with(IntroActivity.this).push("ExtraCleaningList", result);
                login();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(IntroActivity.this, jodevErrorModel.message);
            }
        });
    }

    private void login(){
        String id = CurrentUserInfo.getId(this);
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
                    Util.showToast(IntroActivity.this, jodevErrorModel.message);
                    finish();
                }
            });
        }

    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        animate();
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
