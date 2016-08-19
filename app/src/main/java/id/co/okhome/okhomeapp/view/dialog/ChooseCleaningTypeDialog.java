package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipTextController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChooseCleaningTypeDialog extends ViewDialog{

    @BindView(R.id.dialogChooseCleaningType_tvSubText)
    TextView tvSubText;

    DialogCommonCallback callback;
    public ChooseCleaningTypeDialog(DialogCommonCallback callback) {
        this.callback = callback;
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
    }

    @OnClick(R.id.dialogChooseCleaningType_vbtnOneTimeCleaning)
    public void onBtnOneTimeCleaning(View v){
        callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "ONETIME"));

    }

    @OnClick(R.id.dialogChooseCleaningType_vbtnPeriodicCleaning)
    public void onBtnPeriodicCleaning(View v){
        callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "PERIODIC"));

    }

}
