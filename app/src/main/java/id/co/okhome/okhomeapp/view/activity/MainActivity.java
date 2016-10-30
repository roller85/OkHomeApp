package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.facebook.FacebookSdk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.DelayedFinish;
import id.co.okhome.okhomeapp.lib.DrawerLayoutController;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.view.drawerlayout.MainDrawerViewComponentInitiator;
import id.co.okhome.okhomeapp.view.fragment.tabitem.MakeReservationFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.MakeReservationFragment_old;

import static id.co.okhome.okhomeapp.lib.DrawerLayoutController.with;

public class MainActivity extends OkHomeActivityParent implements View.OnClickListener{

    @BindView(R.id.actMain_dlContent)
    DrawerLayout drawerLayout;

    @BindView(R.id.actMain_vbtnSetting)
    View vbtnSetting;

    public DrawerLayoutController drawerLayoutController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitCallback.setApplicationContext(getApplicationContext());

        ButterKnife.bind(this);
        vbtnSetting.setVisibility(View.INVISIBLE);
        drawerLayoutController = DrawerLayoutController
                .with(this, R.id.actMain_flContent, drawerLayout)
                .setViewComponentInitiator(new MainDrawerViewComponentInitiator())
                .commit();

        //로그인따라 첫페이지 다름
        if(CurrentUserInfo.isLogin(this)){
            drawerLayoutController.show(new MakeReservationFragment());
        }else{
            drawerLayoutController.show(new MakeReservationFragment());
        }


        FacebookSdk.sdkInitialize(getApplicationContext());
    }



    @OnClick(R.id.actMain_llbtnMore)
    public void onClickMore(){
        drawerLayoutController.toggle();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        }
        with(this).show(new MakeReservationFragment_old());
    }

    @Override
    protected void onDestroy() {
        drawerLayoutController.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount() <= 0){
            if(drawerLayoutController.getLastFragmentString().equals(MakeReservationFragment.class.getName())){
                DelayedFinish.finish(this, "한번 더 누르면 종료됩니다.");
            }else{
                drawerLayoutController.show(new MakeReservationFragment());
            }
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
