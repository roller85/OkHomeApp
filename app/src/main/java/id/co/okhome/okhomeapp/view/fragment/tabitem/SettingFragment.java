package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class SettingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    @OnClick(R.id.fragSetting_vbtnDeleteAccount)
    public void onDeleteClick(){
        DialogController.showCenterDialog(getContext(),
                new CommonTextDialog("Really cancel your account?", "Your profile and any listings will disappear.\nWe'll miss you terribly", new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {

                    }
                }));
    }

    @OnClick(R.id.fragSetting_vbtnLogout)
    public void onLogoutClick(){
        DialogController.showCenterDialog(getContext(),
                new CommonTextDialog("Really logout?", "We'll miss you terribly", new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {

                    }
                }));
    }
}
