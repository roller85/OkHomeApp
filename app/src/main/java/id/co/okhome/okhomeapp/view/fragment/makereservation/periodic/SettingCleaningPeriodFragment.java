package id.co.okhome.okhomeapp.view.fragment.makereservation.periodic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkhomeCleaningTimeChooser;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoRepeatorCallback;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class SettingCleaningPeriodFragment extends Fragment implements MakeReservationFlow{


    JoViewRepeator<TimeModel> repeatorEveryweekMulti;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_period, null);
    }

    @Override
    public boolean next(MakeReservationParam params) {

//        boolean hasSchedule = false;
//        String[] arrTimes = new String[7];
//        JoViewRepeator<TimeModel> repeator = null;
//        repeator = repeatorEveryweekMulti;
//
//        for(int i = 0; i < repeator.getList().size(); i++){
//            TimeModel m = repeator.getList().get(i);
//            if(!m.time.equals("")){
//                arrTimes[i] = m.time;
//                hasSchedule = true;
//            }else{
//                arrTimes[i] = "X";
//            }
//        }
//
//        String period = "";
//        for(String s : arrTimes){
//            period += "," + s;
//        }
//        period = period.substring(1);


        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {
        ;
    }





    class DayTimeRepeatorCallback extends JoRepeatorCallback<TimeModel>{

        @BindView(R.id.itemDayTime_tvTime)      TextView tvTime;
        @BindView(R.id.itemDayTime_vgFrame)     ViewGroup vgFrame;

        @Override
        public void onBind(View v, final TimeModel model) {
            ButterKnife.bind(this, v);

            tvTime.setText(model.time);
            if(model.time.equals("")){
                vgFrame.setBackgroundResource(R.drawable.bg_inputbox);
            }else{
                vgFrame.setBackgroundResource(R.drawable.bg_inputbox_focus);
            }

            //클릭시 팝업
            vgFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(model.time.equals("")){
                        new OkhomeCleaningTimeChooser(getActivity(), new OkhomeCleaningTimeChooser.TimeListener() {
                            @Override
                            public void onTimeChoosed(String time, String timeValue) {
                                model.time = time;
                                getViewRepeator().notifyDataChange(model);
                            }
                        }).show();
                    }else{
                        model.time = "";
                        getViewRepeator().notifyDataChange(model);
                    }




//                    DialogController.showBottomDialog(getContext(), new ChooseCleaningTimeDialog("", "", "", model.time).setCommonCallback(new ViewDialog.DialogCommonCallback() {
//                        @Override
//                        public void onCallback(Object dialog, Map<String, Object> params) {
//                            String time = (String) params.get("TIME");
//                            model.time = time;
//
//
//                            getViewRepeator().notifyDataChange(model);
//                            DialogController.dismissDialog(dialog);
//
//                        }
//                    }), new OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogPlus dialog) {
//                            if(model.time.equals("")){
//
//                            }
//                        }
//                    });
                }
            });
        }

    }


    class TimeModel{
        public String time;
        public TimeModel(String time) {
            this.time = time;
        }
    }
}

