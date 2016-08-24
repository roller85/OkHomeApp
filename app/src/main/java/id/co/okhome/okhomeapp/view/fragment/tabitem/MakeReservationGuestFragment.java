package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipImageController;
import id.co.okhome.okhomeapp.view.activity.SignupActivity;
import id.co.okhome.okhomeapp.view.activity.StartActivity;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MakeReservationGuestFragment extends Fragment {


    @BindView(R.id.fragmentMakeReservationGuest_ivThumb)
    ImageView ivThumb;

    @BindView(R.id.fragmentMakeReservationGuest_tvSubTitle)
    TextView tvSubTitle;

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

//        AnimatedTooltipTextController.with(tvSubTitle).setArrTooltips(
//                new String[]{"You've just started to\nChange your life for better1"
//                        , "You've just started to\nChange your life for better2"
//                        , "You've just started to\nChange your life for better3"
//                        , "You've just started to\nChange your life for better4"
//                        , "You've just started to\nChange your life for better5"}).start();

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

    @OnClick(R.id.fragmentMakeReservationGuest_tvbtnSignup)
    public void onBtnSignup(View v){
        startActivity(new Intent(getContext(), SignupActivity.class));
    }

    @OnClick(R.id.fragmentMakeReservationGuest_tvbtnLogin)
    public void onBtnLogin(View v){
        startActivity(new Intent(getContext(), StartActivity.class));
    }


}
