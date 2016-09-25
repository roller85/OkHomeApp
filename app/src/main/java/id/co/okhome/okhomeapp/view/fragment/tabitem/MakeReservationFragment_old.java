package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTypeDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MakeReservationFragment_old extends Fragment {

    @BindView(R.id.layerMenuItems_ivPhoto)  ImageView ivPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("JO", "MakeReservationFragment_old onCreateView");
        return inflater.inflate(R.layout.fragment_makereservation2, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("JO", "MakeReservationFragment_old onStart");
        ButterKnife.bind(this, getView());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ivPhoto.getDrawable().setCallback(null);
    }

    @OnClick(R.id.fragmentMakeReservation_vbtnDo)
    public void onClick(View v){

        DialogController.showBottomDialog(getContext(), new ChooseCleaningTypeDialog(this, true, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {

            }
        }));
    }
}
