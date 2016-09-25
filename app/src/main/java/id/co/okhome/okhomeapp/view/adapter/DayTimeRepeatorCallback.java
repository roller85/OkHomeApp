package id.co.okhome.okhomeapp.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoRepeatorCallback;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTimeDialog;

/**
 * Created by josongmin on 2016-09-19.
 */

public class DayTimeRepeatorCallback extends JoRepeatorCallback<DayTimeRepeatorCallback.TimeModel> {

    @BindView(R.id.itemDayTime_tvTime)      TextView tvTime;
    @BindView(R.id.itemDayTime_vgFrame)     ViewGroup vgFrame;

    boolean ableSetting = false;

    public DayTimeRepeatorCallback() {
    }

    public DayTimeRepeatorCallback(boolean ableSetting) {
        this.ableSetting = ableSetting;
    }

    @Override
    public void onBind(View v, final TimeModel model) {
        ButterKnife.bind(this, v);


        if(!model.time.equals("X")){
            tvTime.setText(model.time);
        }else{
            tvTime.setText("");
        }

        if(model.time.equals("") || model.time.equals("X")){
            vgFrame.setBackgroundResource(R.drawable.bg_inputbox);
        }else{
            vgFrame.setBackgroundResource(R.drawable.bg_inputbox_focus);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogController.showCenterDialog(getViewRepeator().getContext(), new ChooseCleaningTimeDialog("", "", "", model.time).setCommonCallback(new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {
                        String time = (String) params.get("TIME");
                        model.time = time;


                        getViewRepeator().notifyDataChange(model);
                        DialogController.dismissDialog(dialog);

                    }
                }));
            }
        });
    }

    public static class TimeModel{
        public String time;
        public TimeModel(String time) {
            this.time = time;
        }
    }
}