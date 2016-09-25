package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.Util;

/**
 * Created by josongmin on 2016-07-28.
 */

public class CustomerCenterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customercenter, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }


    @OnClick(R.id.fragmentCustomerCenter_vbtnContactTel)
    public void onContactTelClick(View v){
        Util.openPhoneDialIntent(getContext(), "+6281381006162");
    }

    @OnClick(R.id.fragmentCustomerCenter_vbtnEmail)
    public void onEmailClick(View v){
        Util.openEmailIntent(getContext(), "dhkim@gmail.com", "Inquiry", "\n\n\nfrom " + CurrentUserInfo.get(getContext()).name);
    }

    @OnClick(R.id.fragmentCustomerCenter_vbtnWhatsApp)
    public void onWhatsAppClick(View v){
        Util.openWhatsAppMessageIntent(getContext(), "+821093149449", "Inquiry", "\n\n\nfrom " + CurrentUserInfo.get(getContext()).name);
    }

}
