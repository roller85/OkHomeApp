package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialogplus.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChooseCleaningTimeDialog extends ViewDialog{

    @BindView(R.id.dialogChooseCleaningTime_ivChkTime1)     ImageView ivChktime1;
    @BindView(R.id.dialogChooseCleaningTime_ivChkTime2)     ImageView ivChktime2;
    @BindView(R.id.actMakeOneDayReservation_tvDate)         TextView tvDate;

    String year, month, day, chkTime;
    public ChooseCleaningTimeDialog(String year, String month, String day, String chkTime) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.chkTime = chkTime;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_choose_cleaning_time, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());
        tvDate.setText(year + "." + month + "." + day);
    }


    @OnClick({R.id.dialogChooseCleaningTime_vbtnTime1, R.id.dialogChooseCleaningTime_vbtnTime2})
    public void onBtnTimeClick(View v){
        String time = "";
        switch(v.getId()){
            case R.id.dialogChooseCleaningTime_vbtnTime1:
                time = "13:00";
                break;
            case R.id.dialogChooseCleaningTime_vbtnTime2:
                time = "15:00";
                break;
        }

        getCommonCallback().onCallback(getDialogPlus(), Util.makeMap("TIME", time));
    }


}
