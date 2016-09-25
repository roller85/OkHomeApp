package id.co.okhome.okhomeapp.view.drawerlayout;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsengvn.typekit.Typekit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.DrawerLayoutController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.view.activity.MainActivity;
import id.co.okhome.okhomeapp.view.activity.SigninActivity;
import id.co.okhome.okhomeapp.view.activity.SignupActivity;
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

public class MainDrawerViewComponentInitiator implements DrawerLayoutController.ViewComponentInitiator, CurrentUserInfo.OnUserInfoChangeListener{

    @BindView(R.id.layerMenuItems_tvCredit)             TextView tvCredit;
    @BindView(R.id.layerMenuItems_tvName)               TextView tvName;
    @BindView(R.id.layerMenuItems_ivPhoto)              ImageView ivPhoto;
    @BindView(R.id.layerMenuItems_ivMore)               ImageView ivMore;


    @BindView(R.id.layerMenuItems_vBtnMakeReservation)  View vbtnMakeReservation;
    @BindView(R.id.layerMenuItems_vBtnCustomerCenter)   View vbtnCustomerCenter;
    @BindView(R.id.layerMenuItems_vBtnHistory)          View vbtnHistory;
    @BindView(R.id.layerMenuItems_vBtnCharge)           View vbtnCharge;
    @BindView(R.id.layerMenuItems_vBtnNotice)           View vbtnNotice;
    @BindView(R.id.layerMenuItems_vBtnSchedule)         View vbtnSchedule;
    @BindView(R.id.layerMenuItems_vBtnSetting)          View vbtnSetting;
    @BindView(R.id.layerMenuItems_vBtnUserInfo)         View vbtnUserInfo;
    @BindView(R.id.layerMenuItems_vgCredit)             View vgCredit;
    @BindView(R.id.layerMenuItems_vPadding)             View vPadding;
    @BindView(R.id.layerMenuItems_vBtnLogin)            View vbtnLogin;
    @BindView(R.id.layerMenuItems_vBtnSignup)           View vbtnSignup;



    DrawerLayoutController drawerLayoutController;
    FragmentActivity activity;
    View vTabIconBefore = null;
    TextView tvTitleBefore = null;
    boolean isGuest = false;

    //서랍뷰들 초기화
    @Override
    public void initDrawerContent(FragmentActivity activity, DrawerLayoutController drawerLayoutController, DrawerLayout drawerLayout, View vParent) {
        ButterKnife.bind(this, vParent);
        this.activity = activity;
        this.drawerLayoutController = drawerLayoutController;

        initTextViewSetting();

        //최초마킹
        markTabItem(activity.findViewById(R.id.layerMenuItems_vBtnMakeReservation));
        CurrentUserInfo.registerUserInfoChangeListener(this); //사용자 정보 갖고오는 리스너 등록
        CurrentUserInfo.invokeCallback(activity, this); //리스너 초기 실행
    }

    //게스트일경우 처리
    private void initGuestInfo(){
        isGuest = true;
        tvName.setText("Hello guest!");
        tvCredit.setText("-");

        ivMore.setVisibility(View.GONE);
        vPadding.setVisibility(View.GONE);
        vgCredit.setVisibility(View.GONE);
        vbtnCharge.setVisibility(View.GONE);
        vbtnHistory.setVisibility(View.GONE);
        vbtnSchedule.setVisibility(View.GONE);
        vbtnSetting.setVisibility(View.GONE);
    }


    //회원일경우 처리
    private void initUserInfo(UserModel userModel){
        CurrentUserInfo.loadUserImg(activity, userModel.photoUrl, ivPhoto);
        tvCredit.setText(Util.getMoneyString(userModel.getTotalCredit(), '.'));
        tvName.setText(userModel.name);

        vbtnLogin.setVisibility(View.GONE);
        vbtnSignup.setVisibility(View.GONE);
    }

    //유저정보 변경됨.
    @Override
    public void onUserInfoChanged(UserModel userModel) {
        if(userModel == null){
            //게스트
            initGuestInfo();
        }else{
            initUserInfo(userModel);
        }
    }

    private void initTextViewSetting(){

    }

    @Override
    public void onDestroy() {
        CurrentUserInfo.unregisterUserInfoChangeListener(this);
    }

    /**탭 마킹*/
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
            tvTitleBefore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.5f);
            tvTitleBefore.setTextColor(Color.parseColor("#818895"));
            tvTitleBefore.setTypeface(Typekit.createFromAsset(activity, "NotoSans-Regular.ttf"));
        }

        //타이틀부분 변경
        TextView tvTitle = (TextView)((ViewGroup)vBtnTab).getChildAt(1);
        if(tvTitle != null){
            //텍스트 크고 굵게
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
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

    @OnClick({R.id.layerMenuItems_vBtnUserInfo, R.id.layerMenuItems_vBtnSignup, R.id.layerMenuItems_vBtnLogin})
    public void onBtnStartActivityClick(View v){
        switch(v.getId()) {
            //activity;
            case R.id.layerMenuItems_vBtnUserInfo:
                if(!isGuest){
                    activity.startActivity(new Intent(activity, UserInfoActivity.class));
                }
                return;

            case R.id.layerMenuItems_vBtnSignup:
                activity.startActivity(new Intent(activity, SignupActivity.class));
                break;

            case R.id.layerMenuItems_vBtnLogin:
                activity.startActivity(new Intent(activity, SigninActivity.class));
                break;
        }


    }


}
