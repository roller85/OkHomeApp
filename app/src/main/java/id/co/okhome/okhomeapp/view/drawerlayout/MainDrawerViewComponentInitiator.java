package id.co.okhome.okhomeapp.view.drawerlayout;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.DrawerLayoutController;
import id.co.okhome.okhomeapp.view.activity.UserInfoActivity;
import id.co.okhome.okhomeapp.view.fragment.CustomerCenterFragment;
import id.co.okhome.okhomeapp.view.fragment.MakeReservationFragment;
import id.co.okhome.okhomeapp.view.fragment.MyCleaningCalendarFragment;
import id.co.okhome.okhomeapp.view.fragment.NoticeFragment;
import id.co.okhome.okhomeapp.view.fragment.SettingFragment;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MainDrawerViewComponentInitiator implements DrawerLayoutController.ViewComponentInitiator{

    DrawerLayoutController drawerLayoutController;
    FragmentActivity activity;
    //서랍뷰들 초기화
    @Override
    public void initDrawerContent(FragmentActivity activity, DrawerLayoutController drawerLayoutController, DrawerLayout drawerLayout,  View vParent) {
        ButterKnife.bind(this, vParent);
        this.activity = activity;
        this.drawerLayoutController = drawerLayoutController;
    }


    @OnClick({
            R.id.layerMenuItems_vBtnUserInfo, R.id.layerMenuItems_vBtnMakeReservation, R.id.layerMenuItems_vBtnSetting
            , R.id.layerMenuItems_vBtnNotice
            , R.id.layerMenuItems_vBtnSchedule, R.id.layerMenuItems_vBtnCharge, R.id.layerMenuItems_vBtnCustomerCenter
    })
    public void onBtnMakeReservationClick(View v){
        Fragment f = null;
        switch(v.getId()){

            //activity;
            case R.id.layerMenuItems_vBtnUserInfo:
                activity.startActivity(new Intent(activity, UserInfoActivity.class));
                return;

            //fragment
            case R.id.layerMenuItems_vBtnMakeReservation:
                f = new MakeReservationFragment();
                break;

            case R.id.layerMenuItems_vBtnSchedule:
                f = new MyCleaningCalendarFragment();
                break;

            case R.id.layerMenuItems_vBtnCharge:
                f = new SettingFragment();
                break;

            case R.id.layerMenuItems_vBtnSetting:
                f = new SettingFragment();
                break;

            case R.id.layerMenuItems_vBtnNotice:
                f = new NoticeFragment();
                break;

            case R.id.layerMenuItems_vBtnCustomerCenter:
                f = new CustomerCenterFragment();
                break;
        }

        drawerLayoutController.show(f);
    }


}
