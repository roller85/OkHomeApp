package id.co.okhome.okhomeapp.view.fragment.makereservation.periodic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.adapter.DayTimeRepeatorCallback;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

import static android.app.Activity.RESULT_OK;
import static id.co.okhome.okhomeapp.R.id.fragmentMakeReservationPeriodInvoice_vgDays;

/**
 * Created by josongmin on 2016-07-28.
 */

public class PeriodicCleaningInvoiceFragment extends Fragment implements MakeReservationFlow {


    @BindView(R.id.fragmentMakeReservationPeriodInvoice_tvStartDate)
    TextView tvStartDate;

    @BindView(R.id.fragmentMakeReservationPeriodInvoice_tvPrice)
    TextView tvPrice;

    @BindView(fragmentMakeReservationPeriodInvoice_vgDays)
    ViewGroup vgDays;

    JoViewRepeator repeatorDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_period_cleaning_invoice, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        repeatorDay = new JoViewRepeator<DayTimeRepeatorCallback.TimeModel>(getContext())
                .setContainer(vgDays)
                .setSpanSize(7)
                .setItemLayoutId(R.layout.item_day_time)
                .setCallBack(new DayTimeRepeatorCallback());
        List<DayTimeRepeatorCallback.TimeModel> listTimeModel = new ArrayList<>();
        for(int  i = 0; i < 7; i ++){
            listTimeModel.add(new DayTimeRepeatorCallback.TimeModel(i+""));
        }
        repeatorDay.setList(listTimeModel);
        repeatorDay.notifyDataSetChanged();
        repeatorDay.enableSquare();

    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {
        int pricePerHour = MakeReservationParam.BASIC_CLEANING_PAY_PER_HOUR;
        int duration = params.getDuration();

        Util.getPatternedTimeString(params.startDate, "yyyy-MM-dd", "yyyy-MM-dd(E)");

        tvStartDate.setText(params.startDate);
        tvPrice.setText(Util.getMoneyString(pricePerHour, '.') + "Rp / Hour" + " X " + duration + "Hours = " + Util.getMoneyString(pricePerHour * duration, '.') + "Rp");


        List<DayTimeRepeatorCallback.TimeModel> listTimeModel = new ArrayList<>();
        String[] arrDaytime = params.period.split(",");
        for(String dayTime : arrDaytime){
            listTimeModel.add(new DayTimeRepeatorCallback.TimeModel(dayTime));
        }
//
        repeatorDay.setList(listTimeModel);
        repeatorDay.notifyDataSetChanged();
    }


    @Override
    public boolean next(final MakeReservationParam paramsForReservation) {

        paramsForReservation.homeId = CurrentUserInfo.getHomeId(getContext());
        //두두두두..
        try{


        }catch(Exception e){
            Util.showToast(getContext(), e.getMessage());
            return false;
        }

        DialogController.showCenterDialog(getContext(), new CommonTextDialog("Proceed?", "Touch Confirm, Periodic cleaning reservation will be complete", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String onClick = Util.getMapValue(params, "ONCLICK");
                if(onClick.equals("OK")){

                    String paramJson = new Gson().toJson(paramsForReservation);
                    final int pId = ProgressDialogController.show(getContext());
                    RestClient.getCleaningRequestClient().periodicRequest(CurrentUserInfo.getId(getContext()), paramJson).enqueue(new RetrofitCallback<String>() {
                        @Override
                        public void onFinish() {
                            ProgressDialogController.dismiss(pId);
                        }

                        @Override
                        public void onSuccess(String result) {
                            //완료됬슴다
                            Util.showToast(getContext(), "Making reservation complete");
                            Intent i = new Intent();
                            i.putExtra("RESERVATION_COMPLETE_YN", "Y");
                            getActivity().setResult(RESULT_OK, i);
                            getActivity().finish();
                        }

                        @Override
                        public void onJodevError(ErrorModel jodevErrorModel) {
                            Util.showToast(getContext(), jodevErrorModel.message);
                        }
                    });

                }
            }
        }));



        return true;
    }




}
