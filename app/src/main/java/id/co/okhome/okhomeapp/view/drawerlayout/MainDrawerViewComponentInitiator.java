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
import id.co.okhome.okhomeapp.view.activity.CleaningTicketActivity;
import id.co.okhome.okhomeapp.view.activity.PointActivity;
import id.co.okhome.okhomeapp.view.activity.SigninActivity;
import id.co.okhome.okhomeapp.view.activity.SignupActivity;
import id.co.okhome.okhomeapp.view.activity.UserInfoActivity;
import id.co.okhome.okhomeapp.view.fragment.tabitem.MyCleaningCalendarFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.PromotionFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.CustomerCenterFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.HistoryFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.InviteUserFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.MakeReservationFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.NoticeFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.SettingFragment;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MainDrawerViewComponentInitiator implements DrawerLayoutController.ViewComponentInitiator, CurrentUserInfo.OnUserInfoChangeListener{

    TextView tvTitle;
    View vbtnRightSetting;
    ImageView ivRightSetting;

    @BindView(R.id.layerMenuItems_tvCredit)             TextView tvCredit;
    @BindView(R.id.layerMenuItems_tvName)               TextView tvName;
    @BindView(R.id.layerMenuItems_ivMore)               ImageView ivMore;

    @BindView(R.id.layerMenuItems_vBtnMakeReservation)  View vbtnMakeReservation;
    @BindView(R.id.layerMenuItems_vBtnCustomerCenter)   View vbtnCustomerCenter;
    @BindView(R.id.layerMenuItems_vBtnHistory)          View vbtnHistory;
    @BindView(R.id.layerMenuItems_vBtnPromotion)        View vbtnPromotion;
    @BindView(R.id.layerMenuItems_vBtnNotice)           View vbtnNotice;
    @BindView(R.id.layerMenuItems_vBtnSchedule)         View vbtnSchedule;
    @BindView(R.id.layerMenuItems_vBtnSetting)          View vbtnSetting;
    @BindView(R.id.layerMenuItems_vBtnUserInfo)         View vbtnUserInfo;
    @BindView(R.id.layerMenuItems_vgCredit)             View vgCredit;
    @BindView(R.id.layerMenuItems_vPadding)             View vPadding;
    @BindView(R.id.layerMenuItems_vBtnLogin)            View vbtnLogin;
    @BindView(R.id.layerMenuItems_vBtnSignup)           View vbtnSignup;
    @BindView(R.id.layerMenuItems_vBtnInviteFriend)     View vbtnInviteFriends;



    DrawerLayoutController drawerLayoutController;
    FragmentActivity activity;
    View vTabIconBefore = null;
    TextView tvTitleBefore = null;
    boolean isGuest = false;
    MyCleaningCalendarFragment myCleaningCalendarFragment = null;
    //서랍뷰들 초기화
    @Override
    public void initDrawerContent(FragmentActivity activity, DrawerLayoutController drawerLayoutController, DrawerLayout drawerLayout, View vParent) {
        ButterKnife.bind(this, vParent);
        this.activity = activity;
        this.drawerLayoutController = drawerLayoutController;

        initTextViewSetting();

        tvTitle  = (TextView)activity.findViewById(R.id.actMain_tvTitle);
        vbtnRightSetting = activity.findViewById(R.id.actMain_vbtnSetting);
        ivRightSetting = (ImageView)activity.findViewById(R.id.actMain_ivSetting);

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
        vbtnPromotion.setVisibility(View.GONE);
        vbtnHistory.setVisibility(View.GONE);
        vbtnSchedule.setVisibility(View.GONE);
        vbtnSetting.setVisibility(View.GONE);
        vbtnInviteFriends.setVisibility(View.GONE);
    }


    //회원일경우 처리
    private void initUserInfo(UserModel userModel){
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
            tvTitleBefore.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.leftmenu_text));
            tvTitleBefore.setTextColor(Color.parseColor("#818895"));
            tvTitleBefore.setTypeface(Typekit.createFromAsset(activity, "NotoSans-Regular.ttf"));
        }

        //타이틀부분 변경
        TextView tvTitle = (TextView)((ViewGroup)vBtnTab).getChildAt(1);
        if(tvTitle != null){
            //텍스트 크고 굵게
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.leftmenu_boldtext));
            tvTitle.setTextColor(Color.parseColor("#3b3e45"));
            tvTitle.setTypeface(Typekit.createFromAsset(activity, "NotoSans-Bold.ttf"));
            tvTitleBefore = tvTitle;
        }
    }


    @OnClick({R.id.layerMenuItems_vBtnMakeReservation, R.id.layerMenuItems_vBtnSetting, R.id.layerMenuItems_vBtnHistory
            , R.id.layerMenuItems_vBtnNotice
            , R.id.layerMenuItems_vBtnSchedule, R.id.layerMenuItems_vBtnPromotion, R.id.layerMenuItems_vBtnCustomerCenter, R.id.layerMenuItems_vBtnInviteFriend})
    public void onBtnStartFragmentClick(View v){

        markTabItem(v);
        Fragment f = null;
        switch(v.getId()){
            case R.id.layerMenuItems_vBtnMakeReservation:
                f = new MakeReservationFragment();
                break;

            case R.id.layerMenuItems_vBtnSchedule:
                if(myCleaningCalendarFragment == null){
                    myCleaningCalendarFragment = new MyCleaningCalendarFragment();
                }
                f = myCleaningCalendarFragment;
                break;

            case R.id.layerMenuItems_vBtnPromotion:
                f = new PromotionFragment();
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

            case R.id.layerMenuItems_vBtnInviteFriend:
                f = new InviteUserFragment();
                break;
        }

        startFragment(f);
    }

    public void startFragment(Fragment fragment){
        drawerLayoutController.show(fragment);

        if(fragment instanceof TabFragmentFlow){
            TabFragmentFlow tabFlow = (TabFragmentFlow)fragment;
            String title = tabFlow.getTitle();
            tvTitle.setText(title);

            View.OnClickListener onClickListener = tabFlow.onTabSettingClick(ivRightSetting);
            vbtnRightSetting.setOnClickListener(onClickListener);
            if(onClickListener == null){
                vbtnRightSetting.setVisibility(View.INVISIBLE);
            }else{
                vbtnRightSetting.setVisibility(View.VISIBLE);
            }
        }


    }

    @OnClick({R.id.layerMenuItems_vBtnUserInfo, R.id.layerMenuItems_vBtnSignup, R.id.layerMenuItems_vBtnLogin
            , R.id.layerMenuItems_vBtnPoint, R.id.layerMenuItems_vBtnCleaningTicket})
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

            case R.id.layerMenuItems_vBtnCleaningTicket:
                activity.startActivity(new Intent(activity, CleaningTicketActivity.class));
                break;

            case R.id.layerMenuItems_vBtnPoint:
                activity.startActivity(new Intent(activity, PointActivity.class));
                break;
        }


    }


}
