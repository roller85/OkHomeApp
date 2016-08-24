package id.co.okhome.okhomeapp.view.drawerlayout;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsengvn.typekit.Typekit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.DrawerLayoutController;
import id.co.okhome.okhomeapp.view.activity.MainActivity;
import id.co.okhome.okhomeapp.view.activity.UserInfoActivity;
import id.co.okhome.okhomeapp.view.fragment.tabitem.ChargePointFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.CustomerCenterFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.HistoryFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.MakeReservationFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.MyCleaningCalendarFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.NoticeFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.SettingFragment;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MainDrawerViewComponentInitiator implements DrawerLayoutController.ViewComponentInitiator{

    DrawerLayoutController drawerLayoutController;
    FragmentActivity activity;
    View vTabIconBefore = null;
    TextView tvTitleBefore = null;
    //서랍뷰들 초기화
    @Override
    public void initDrawerContent(FragmentActivity activity, DrawerLayoutController drawerLayoutController, DrawerLayout drawerLayout, View vParent) {
        ButterKnife.bind(this, vParent);
        this.activity = activity;
        this.drawerLayoutController = drawerLayoutController;

        //최초마킹
        markTabItem(activity.findViewById(R.id.layerMenuItems_vBtnMakeReservation));
    }

    private void markTabItem(View vBtnTab){



        if(vTabIconBefore != null){
            vTabIconBefore.setVisibility(View.INVISIBLE);
        }

        View vTabIcon = vBtnTab.findViewById(R.id.tabIcon);
        if(vTabIcon != null){
            vTabIcon.setVisibility(View.VISIBLE);
            vTabIconBefore  = vTabIcon;
        }

        //--------------------

        if(tvTitleBefore != null){
            //텍스트 작고 안 굵게
            tvTitleBefore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            tvTitleBefore.setTextColor(Color.parseColor("#818895"));
            tvTitleBefore.setTypeface(Typekit.createFromAsset(activity, "NotoSans-Regular.ttf"));
        }

        //타이틀부분 변경
        TextView tvTitle = (TextView)((ViewGroup)vBtnTab).getChildAt(1);
        if(tvTitle != null){
            //텍스트 크고 굵게
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            tvTitle.setTextColor(Color.parseColor("#3b3e45"));
            tvTitle.setTypeface(Typekit.createFromAsset(activity, "NotoSans-Bold.ttf"));
            tvTitleBefore = tvTitle;
        }
    }

    @OnClick({R.id.layerMenuItems_vBtnMakeReservation, R.id.layerMenuItems_vBtnSetting, R.id.layerMenuItems_vBtnHistory
            , R.id.layerMenuItems_vBtnNotice
            , R.id.layerMenuItems_vBtnSchedule, R.id.layerMenuItems_vBtnCharge, R.id.layerMenuItems_vBtnCustomerCenter})
    public void onBtnStartFragmentClick(View v){

        markTabItem(v);
        if(activity instanceof MainActivity){
            ((MainActivity) activity).setSettingBtnClickListener(null); //리스너 취소. 버튼도 gone상태로
        }

        Fragment f = null;
        switch(v.getId()){
            case R.id.layerMenuItems_vBtnMakeReservation:
                f = new MakeReservationFragment();
                break;

            case R.id.layerMenuItems_vBtnSchedule:
                f = new MyCleaningCalendarFragment();
                break;

            case R.id.layerMenuItems_vBtnCharge:
                f = new ChargePointFragment();
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

            case R.id.layerMenuItems_vBtnHistory:
                f = new HistoryFragment();
                break;


        }
        drawerLayoutController.show(f);
    }

    @OnClick({R.id.layerMenuItems_vBtnUserInfo})
    public void onBtnStartActivityClick(View v){
        switch(v.getId()) {
            //activity;
            case R.id.layerMenuItems_vBtnUserInfo:
                activity.startActivity(new Intent(activity, UserInfoActivity.class));
                return;
        }


    }


}
