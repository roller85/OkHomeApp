package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.view.activity.MakeOneDayReservationActivity;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MakeReservationFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_makereservation2, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }


    @OnClick(R.id.fragmentMakeReservation_vbtnDo)
    public void onClick(View v){
        startActivity(new Intent(getActivity(), MakeOneDayReservationActivity.class));
    }
}
