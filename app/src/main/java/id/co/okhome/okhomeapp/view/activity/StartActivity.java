package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;

public class StartActivity extends OkHomeActivityParent {

    @BindView(R.id.actSignin_ivbtnFb)
    ImageView ivBtnFb;

    @BindView(R.id.actSignin_ivbtnGg)
    ImageView ivBtnGg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ButterKnife.bind(this);
    }

    private void rotateAnim(){
        RotateAnimation roAnim = new RotateAnimation(-10, 10, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        roAnim.setDuration(800);
        roAnim.setRepeatCount(Animation.INFINITE);
        roAnim.setRepeatMode(Animation.REVERSE);
        roAnim.setFillAfter(true);

        ivBtnFb.startAnimation(roAnim);
        ivBtnGg.startAnimation(roAnim);
    }
}


