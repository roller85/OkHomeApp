package id.co.okhome.okhomeapp.view.fragment.makereservation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.model.UserModel;
import id.co.okhome.okhomeapp.view.dialog.ChangePhoneNumberDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonInputDialog;
import id.co.okhome.okhomeapp.view.dialog.HouseAddressDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class RequestorInfoFragment extends Fragment implements MakeReservationFlow {

    @BindView(R.id.fragmentMakeReservationRequestorInfo_tvName)      TextView tvName;
    @BindView(R.id.fragmentMakeReservationRequestorInfo_tvPhone)     TextView tvPhone;
    @BindView(R.id.fragmentMakeReservationRequestorInfo_tvEmail)     TextView tvEmail;
    @BindView(R.id.fragmentMakeReservationRequestorInfo_tvAddress)   TextView tvAddress;

    public Map<String, String> param = new HashMap<>();

    String homeId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_requestorinfo, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        getPageData();
        notifyDatasetChanged();
    }

    @Override
    public boolean next(MakeReservationParam params) {

        try {
            if (Util.isEmpty(param.get("name"))) throw new OkhomeException("이름을 입력하세요");
            if (Util.isEmpty(param.get("phone"))) throw new OkhomeException("핸드폰번호를 입력하세요");
            if (Util.isEmpty(param.get("address1"))) throw new OkhomeException("주소를 입력하세요");
            if (Util.isEmpty(param.get("address2"))) throw new OkhomeException("주소를 입력하세요");
            if (Util.isEmpty(param.get("address3"))) throw new OkhomeException("주소를 입력하세요");
            if (Util.isEmpty(param.get("address4"))) throw new OkhomeException("주소를 입력하세요");

        } catch (OkhomeException e) {
            Util.showToast(getContext(), e.message);
            return false;

        }

        params.address1 = param.get("address1");
        params.address2 = param.get("address2");
        params.address3 = param.get("address3");
        params.address4 = param.get("address4");
        params.phone = param.get("phone");
        params.name = param.get("name");

        return true;
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {

    }

    private void getPageData(){
        UserModel userModel = CurrentUserInfo.get(getContext());
        if(userModel == null){
            return;
        }

        param.put("name", userModel.name);
        param.put("email", userModel.email);
        param.put("phone", userModel.phone);

        if(userModel.listHomeModel != null){
            HomeModel homeModel = userModel.listHomeModel.get(0);
            param.put("address1", homeModel.address1);
            param.put("address2", homeModel.address2);
            param.put("address3", homeModel.address3);
            param.put("address4", homeModel.address4);
            homeId = homeModel.id;
        }
    }

    //모델이랑 뷰 값 연결
    private void notifyDatasetChanged(){
        tvName.setText(param.get("name"));
        tvEmail.setText(param.get("email"));
        tvPhone.setText(param.get("phone"));
        if(param.get("address1") != null){
            tvAddress.setText(param.get("address1") + " " + param.get("address2") + " " + param.get("address3") + " " + param.get("address4"));
        }
    }

    @OnClick({R.id.fragmentMakeReservationRequestorInfo_vbtnName, R.id.fragmentMakeReservationRequestorInfo_tvName})
    public void onClick(View v){
        DialogController.showCenterDialog(getContext(), new CommonInputDialog("Input your name", "What is your name", tvName.getText().toString(), new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                if(params.get("ONCLICK").equals("OK")){
                    String text = (String)params.get("TEXT");
                    param.put("name", text);
                    tvName.setText(text);
                }
            }
        }));
    }

    @OnClick({R.id.fragmentMakeReservationRequestorInfo_vbtnPhone, R.id.fragmentMakeReservationRequestorInfo_tvPhone})
    public void onClickPhone(View v){
        DialogController.showCenterDialog(getContext(), new ChangePhoneNumberDialog("", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                if(params.get("ONCLICK").equals("OK")){
                    String text = (String)params.get("TEXT");
                    param.put("phone", text);
                    tvPhone.setText(text);
                }
            }
        }));
    }

    @OnClick({R.id.fragmentMakeReservationRequestorInfo_vbtnEmail, R.id.fragmentMakeReservationRequestorInfo_tvEmail})
    public void onClickEmail(View v){
        DialogController.showCenterDialog(getContext(), new CommonInputDialog("Input your phone email", "What is your email", tvEmail.getText().toString(), new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                if(params.get("ONCLICK").equals("OK")){
                    String text = (String)params.get("TEXT");
                    param.put("email", text);
                    tvEmail.setText(text);
                }
            }
        }));
    }

    @OnClick({R.id.fragmentMakeReservationRequestorInfo_vbtnAddress, R.id.fragmentMakeReservationRequestorInfo_tvAddress})
    public void onClickAddress(View v){
        DialogController.showCenterDialog(getContext(), new HouseAddressDialog(homeId, false, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                boolean isSuccess = (Boolean)params.get("SUCCESS");

                if(isSuccess){
                    String address1 = (String)params.get("address1");
                    String address2 = (String)params.get("address2");
                    String address3 = (String)params.get("address3");
                    String address4 = (String)params.get("address4");

                    param.put("address1", address1);
                    param.put("address2", address2);
                    param.put("address3", address3);
                    param.put("address4", address4);

                    notifyDatasetChanged();
                }
            }
        }));

    }
}
