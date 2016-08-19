package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;

public class IntroActivity extends OkHomeActivityParent {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new android.os.Handler(){
            @Override
            public void dispatchMessage(Message msg) {

                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            }
        }.sendEmptyMessageDelayed(0, 2500);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
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
