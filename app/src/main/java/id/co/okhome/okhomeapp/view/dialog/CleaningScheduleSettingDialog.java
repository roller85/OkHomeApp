package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class CleaningScheduleSettingDialog extends ViewDialog{

    @BindView(R.id.dialogCalendarSetting_vgHoldingTerm)
    ViewGroup vgHoldingTerm;

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_calendar_setting, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());
        vgHoldingTerm.setVisibility(View.GONE);
    }

    @OnClick(R.id.dialogCalendarSetting_vbtnTerm)
    public void onTermClick(View v){
        vgHoldingTerm.setVisibility(View.VISIBLE);
    }

}
