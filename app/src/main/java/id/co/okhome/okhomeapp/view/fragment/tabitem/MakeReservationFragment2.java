package id.co.okhome.okhomeapp.view.fragment.tabitem;

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
import id.co.okhome.okhomeapp.lib.AnimatedTooltipImageController;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.activity.SigninActivity;
import id.co.okhome.okhomeapp.view.activity.SignupActivity;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTypeDialog;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MakeReservationFragment2 extends Fragment implements TabFragmentFlow {


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


    }

    @Override
    public String getTitle() {
        return "Make reservation";
    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {
        return null;
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


}
