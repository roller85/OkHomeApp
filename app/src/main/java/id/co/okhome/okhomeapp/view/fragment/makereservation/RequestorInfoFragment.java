package id.co.okhome.okhomeapp.view.fragment.makereservation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.dialog.ChangePhoneNumberDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonInputDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class RequestorInfoFragment extends Fragment {

    @BindView(R.id.fragmentMakeReservationRequestorInfo_tvName)      TextView tvName;
    @BindView(R.id.fragmentMakeReservationRequestorInfo_tvPhone)     TextView tvPhone;
    @BindView(R.id.fragmentMakeReservationRequestorInfo_tvEmail)     TextView tvEmail;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_requestorinfo, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    @OnClick({R.id.fragmentMakeReservationRequestorInfo_vbtnName, R.id.fragmentMakeReservationRequestorInfo_tvName})
    public void onClick(View v){
        DialogController.showCenterDialog(getContext(), new CommonInputDialog("Input your name", "What is your name", "", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                if(params.get("ONCLICK").equals("OK")){
                    String text = (String)params.get("TEXT");
                    tvName.setText(text);
                }
            }
        }));
    }

    @OnClick({R.id.fragmentMakeReservationRequestorInfo_vbtnPhone, R.id.fragmentMakeReservationRequestorInfo_tvPhone})
    public void onClickPhone(View v){
        DialogController.showCenterDialog(getContext(), new ChangePhoneNumberDialog(new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                if(params.get("ONCLICK").equals("OK")){
                    String text = (String)params.get("TEXT");
                    tvPhone.setText(text);
                }
            }
        }));
    }

    @OnClick({R.id.fragmentMakeReservationRequestorInfo_vbtnEmail, R.id.fragmentMakeReservationRequestorInfo_tvEmail})
    public void onClickEmail(View v){
        DialogController.showCenterDialog(getContext(), new CommonInputDialog("Input your phone email", "What is your email", "", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                if(params.get("ONCLICK").equals("OK")){
                    String text = (String)params.get("TEXT");
                    tvEmail.setText(text);
                }
            }
        }));
    }
}
