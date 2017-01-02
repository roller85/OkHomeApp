package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipImageController;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerController;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerItem;
import id.co.okhome.okhomeapp.view.activity.MainActivity;
import id.co.okhome.okhomeapp.view.activity.SigninActivity;
import id.co.okhome.okhomeapp.view.activity.SignupActivity;
import id.co.okhome.okhomeapp.view.customview.ProgressDotsView;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTypeDialog;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MakeReservationFragment extends Fragment implements TabFragmentFlow {


    @BindView(R.id.fragmentMakeReservation_vp)
    ViewPager vp;

    @BindView(R.id.fragmentMakeReservation_pdv)
    ProgressDotsView pdv;

    @BindView(R.id.fragmentMakeReservation_vgLogin)
    ViewGroup vgLogin;

    AnimatedTooltipImageController tooltipImageController;
    JoViewPagerController viewPagerController;

    Object sync = new Object();
    boolean isStop = false;
    boolean isDragged = false;
    final int delay = 3000;


    @Override
    public String getTitle() {
        return "청소 예약하기";
    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_makereservation_guest, null);
    }

    synchronized public void setDragged(boolean dragged) {
        synchronized (sync){
            isDragged = dragged;
        }
    }

    synchronized public boolean isDragged() {
        synchronized (sync){
            return isDragged;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        if(CurrentUserInfo.isLogin(getContext())){
            onMember();
        }else{
            onGuest();
        }

        initToolTip();
    }

    private void initToolTip(){
        viewPagerController = JoViewPagerController
                .with(getContext(), vp)
                .setProgressDotsView(pdv)
                .setViewPageItemClass(this, ToolTipItem.class);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch(state){
                    case SCROLL_STATE_IDLE:
                        setDragged(false);
                        break;
                    default:
                        setDragged(true);

                }
            }
        });


        List<ToolTipModel> list = new ArrayList<>();
        list.add(new ToolTipModel("이사청소 설명넣기", "이사청소 고민하지마시고\n오케이홈과 함께 ㄱㄱ해보실까요", R.drawable.img_clean_plunger));
        list.add(new ToolTipModel("주기청소 설명넣기", "주기청소 플랜을 설정해보세요.\n할인된 가격으로 항상 찾아갈께영", R.drawable.img_clean_mop));
        list.add(new ToolTipModel("기타 설명넣기", "You've just started to\nChange your life for better", R.drawable.img_clean_spray));
        list.add(new ToolTipModel("집중청소 설명넣기", "침실, 화장실, 창문 구석구석\n집중청소로 새집처럼 깨끗하게", R.drawable.img_clean_spray));

        viewPagerController.setListModel(list).build();

        android.os.Handler handler = new android.os.Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                if(!isStop){
                    if(isDragged()){
                        ;
                    }else{
                        vp.setCurrentItem((vp.getCurrentItem() + 1) % viewPagerController.getListModel().size());
                    }
                    this.removeMessages(0);
                    this.sendEmptyMessageDelayed(0, delay);

                }
            }
        };

        handler.sendEmptyMessage(delay);
    }

    private void onGuest(){
        vgLogin.setVisibility(View.VISIBLE);

    }

    private void onMember(){
        vgLogin.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    @OnClick(R.id.fragmentMakeReservationGuest_vbtnDo)
    public void onMakeReservation(View v){

        if(CurrentUserInfo.get(getActivity()) == null){
            //회원가입을 해야합니다.
            DialogController.showAlertDialog(getContext(), "Okhome", "회원가입후에 청소신청을 할 수 있습니다!\n5초면 되요!", false, new ViewDialog.DialogCommonCallback() {
                @Override
                public void onCallback(Object dialog, Map<String, Object> params) {
                    String tagClick = (String)params.get(DialogController.TAG_CLICK);
                    if(tagClick.equals("OK")){
                        startActivity(new Intent(getContext(), SignupActivity.class));
                    }
                }
            });

        }else{
            DialogController.showBottomDialog(getContext(), new ChooseCleaningTypeDialog(this, true, new ViewDialog.DialogCommonCallback() {
                @Override
                public void onCallback(Object dialog, Map<String, Object> params) {

                }
            }));
        }

    }

    @OnClick(R.id.fragmentMakeReservation_pdv)
    public void onClick(View v){

    }

    @OnClick(R.id.fragmentMakeReservationGuest_tvbtnSignup)
    public void onBtnSignup(View v){
        startActivity(new Intent(getContext(), SignupActivity.class));
    }

    @OnClick(R.id.fragmentMakeReservationGuest_tvbtnLogin)
    public void onBtnLogin(View v){
        startActivity(new Intent(getContext(), SigninActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ChooseCleaningTypeDialog.REQ_CODE && resultCode == Activity.RESULT_OK){
            String reservationCompleteYn = data.getStringExtra("RESERVATION_COMPLETE_YN");
            if(reservationCompleteYn != null && reservationCompleteYn.equals("Y")){
                //성공햇응게..
                if(getActivity() instanceof MainActivity){
                    ((MainActivity) getActivity()).drawerLayoutController.show(new MyCleaningCalendarFragment());

                }
            }
        }
    }

    public class ToolTipItem extends JoViewPagerItem<ToolTipModel> {

        @BindView(R.id.layerCleaningTipItem_ivImg)      ImageView ivImg;
        @BindView(R.id.layerCleaningTipItem_tvDesc)     TextView tvDesc;
        @BindView(R.id.layerCleaningTipItem_tvTitle)    TextView tvTitle;


        @Override
        public View getView(LayoutInflater inflater) {
            return inflater.inflate(R.layout.layer_cleaningtip_item, null);
        }

        @Override
        public void onViewCreated() {
            ButterKnife.bind(this, getDecorView());
        }

        @Override
        public void onViewSelected(ToolTipModel model, int position) {
            ivImg.setImageResource(model.imgResId);
            tvDesc.setText(model.desc);
            tvTitle.setText(model.title);
        }
    }

    public class ToolTipModel{
        public String title, desc;
        public int imgResId;

        public ToolTipModel(String title, String desc, int imgResId) {
            this.title = title;
            this.desc = desc;
            this.imgResId = imgResId;
        }
    }
}
