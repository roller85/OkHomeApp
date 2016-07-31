package id.co.okhome.okhomeapp.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.okhome.okhomeapp.R;

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
//        ButterKnife.bind(this, getView());
    }
}
