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
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChooseCleaningTimeDialog extends ViewDialog{

    @BindView(R.id.dialogChooseCleaningTime_ivChkTime1)             ImageView ivChktime1;
    @BindView(R.id.dialogChooseCleaningTime_ivChkTime2)             ImageView ivChktime2;
    @BindView(R.id.dialogChooseCleaningTime_ivChkTimeCancel)        ImageView ivChktimeCancel;
    @BindView(R.id.actMakeOneDayReservation_tvDate)                 TextView tvDate;

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
        if(year.equals("")){
            tvDate.setVisibility(View.GONE);
        }else{
            tvDate.setText(year + "." + month + "." + day);
        }

        ivChktime1.setImageResource(R.drawable.ic_check_not);
        ivChktime2.setImageResource(R.drawable.ic_check_not);


        notifyChkChanged();
    }

    private void notifyChkChanged(){
        ivChktime1.setImageResource(R.drawable.ic_check_not);
        ivChktime2.setImageResource(R.drawable.ic_check_not);
        ivChktimeCancel.setImageResource(R.drawable.ic_check_not);

        if(chkTime == null || chkTime.equals("")){
            ;
        }
        else if(chkTime.equals("11:00")){
            ivChktime1.setImageResource(R.drawable.ic_checked);
        }else{
            ivChktime2.setImageResource(R.drawable.ic_checked);
        }

    }


    @OnClick({R.id.dialogChooseCleaningTime_vbtnTime1, R.id.dialogChooseCleaningTime_vbtnTime2, R.id.dialogChooseCleaningTime_vbtnCancel})
    public void onBtnTimeClick(View v){
        String time = "";
        switch(v.getId()){
            case R.id.dialogChooseCleaningTime_vbtnCancel:
                time = "";
                break;
            case R.id.dialogChooseCleaningTime_vbtnTime1:
                time = "11:00";
                break;
            case R.id.dialogChooseCleaningTime_vbtnTime2:
                time = "15:00";
                break;
        }

        chkTime = time;
        notifyChkChanged();

        getCommonCallback().onCallback(getDialog(), Util.makeMap("TIME", time));

    }


}
