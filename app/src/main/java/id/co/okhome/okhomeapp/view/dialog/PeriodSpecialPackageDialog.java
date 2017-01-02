package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class PeriodSpecialPackageDialog extends ViewDialog{

    DialogCommonCallback callback;
    public PeriodSpecialPackageDialog(DialogCommonCallback callback) {
        this.callback = callback;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_period_specialpackage, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){
//        dismiss();
        callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "OK"));
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){
        dismiss();
    }

}
