package id.co.okhome.okhomeapp.view.dialog;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipTextController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.activity.MakeReservationActivity;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChooseCleaningTypeDialog extends ViewDialog{

    public final static int REQ_CODE = 1000;

    @BindView(R.id.dialogChooseCleaningType_tvSubText)
    TextView tvSubText;

    @BindView(R.id.dialogChooseCleaningType_vbtnPeriodicCleaning)
    ViewGroup vgCleaning;

    DialogCommonCallback callback;
    boolean hasPeriodicalCleaning;
    String yyyymmdd = null;

    Fragment fragment;

    public ChooseCleaningTypeDialog(Fragment fragment, boolean hasPeriodicalCleaning, DialogCommonCallback callback) {
        this.callback = callback;
        this.fragment = fragment;
        this.hasPeriodicalCleaning = hasPeriodicalCleaning;
    }

    public ChooseCleaningTypeDialog(Fragment fragment, boolean hasPeriodicalCleaning, String yyyymmddd) {
        this.fragment = fragment;
        this.hasPeriodicalCleaning = hasPeriodicalCleaning;
        this.yyyymmdd = yyyymmddd;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_choose_cleaning_type, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());
        AnimatedTooltipTextController.with(tvSubText)
                .setArrTooltips(new String[]{"We recommend you periodic cleaning", "Feel comportable and cozy", "How can i help you"})
                .start();


        if(!hasPeriodicalCleaning){
            vgCleaning.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.dialogChooseCleaningType_vbtnMoveInCleaning)
    public void onBtnMoveInCleaning(View v){
        fragment.startActivityForResult(new Intent(getContext(), MakeReservationActivity.class).putExtra("TYPE", "MOVEIN").putExtra("DATE", yyyymmdd), REQ_CODE);
        if(callback != null)    callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "MOVEIN"));

        dismiss();
    }

    @OnClick(R.id.dialogChooseCleaningType_vbtnOneTimeCleaning)
    public void onBtnOneTimeCleaning(View v){

        fragment.startActivityForResult(new Intent(getContext(), MakeReservationActivity.class).putExtra("TYPE", "ONEDAY").putExtra("DATE", yyyymmdd), REQ_CODE);

        if(callback != null)    callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "ONETIME"));
        dismiss();
    }

    @OnClick(R.id.dialogChooseCleaningType_vbtnPeriodicCleaning)
    public void onBtnPeriodicCleaning(View v){
        fragment.startActivityForResult(new Intent(getContext(), MakeReservationActivity.class).putExtra("TYPE", "PERIODIC"), REQ_CODE);
        if(callback != null)    callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "PERIODIC"));

        dismiss();
    }

}
