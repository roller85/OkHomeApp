package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.DrawerLayoutController;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.view.drawerlayout.MainDrawerViewComponentInitiator;
import id.co.okhome.okhomeapp.view.fragment.tabitem.MakeReservationFragment;

import static id.co.okhome.okhomeapp.lib.DrawerLayoutController.with;

public class MainActivity extends OkHomeActivityParent implements View.OnClickListener{

    @BindView(R.id.actMain_dlContent)
    DrawerLayout drawerLayout;

    @BindView(R.id.actMain_vbtnSetting)
    View vbtnSetting;

    DrawerLayoutController drawerLayoutController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);
        vbtnSetting.setVisibility(View.INVISIBLE);
        drawerLayoutController = DrawerLayoutController
                .with(this, R.id.actMain_flContent, drawerLayout)
                .setViewComponentInitiator(new MainDrawerViewComponentInitiator())
                .commit()
                .show(new MakeReservationFragment());
    }



    public void setSettingBtnClickListener(View.OnClickListener onClickListener){
        if(onClickListener == null){
            vbtnSetting.setVisibility(View.GONE);
        }else{
            vbtnSetting.setOnClickListener(onClickListener);
            vbtnSetting.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.actMain_llbtnMore)
    public void onClickMore(){
        drawerLayoutController.toggle();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        }
        with(this).show(new MakeReservationFragment());
    }
}
