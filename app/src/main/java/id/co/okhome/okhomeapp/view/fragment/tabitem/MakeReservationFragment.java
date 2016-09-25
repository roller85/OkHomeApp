package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipImageController;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.activity.MainActivity;
import id.co.okhome.okhomeapp.view.activity.SigninActivity;
import id.co.okhome.okhomeapp.view.activity.SignupActivity;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTypeDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MakeReservationFragment extends Fragment {


    @BindView(R.id.fragmentMakeReservationGuest_ivThumb)
    ImageView ivThumb;

    @BindView(R.id.fragmentMakeReservationGuest_tvSubTitle)
    TextView tvSubTitle;

    @BindView(R.id.fragmentMakeReservation_vgLogin)
    ViewGroup vgLogin;

    AnimatedTooltipImageController tooltipImageController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_makereservation_guest, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        tooltipImageController =
                AnimatedTooltipImageController.with(ivThumb)
                        .setArrTooltips(
                                new int[]{R.drawable.img_clean_plunger, R.drawable.img_clean_gloves, R.drawable.img_clean_mop,
                                        R.drawable.img_clean_spray})
                        .setDelay(1500);

        tooltipImageController.start();

        if(CurrentUserInfo.isLogin(getContext())){
            onMember();
        }else{
            onGuest();
        }

//        AnimatedTooltipTextController.with(tvSubTitle).setArrTooltips(
//                new String[]{"You've just started to\nChange your life for better1"
//                        , "You've just started to\nChange your life for better2"
//                        , "You've just started to\nChange your life for better3"
//                        , "You've just started to\nChange your life for better4"
//                        , "You've just started to\nChange your life for better5"}).start();

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
        tooltipImageController.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.fragmentMakeReservationGuest_vbtnDo)
    public void onMakeReservation(View v){
        DialogController.showBottomDialog(getContext(), new ChooseCleaningTypeDialog(this, true, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {

            }
        }));

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
}
