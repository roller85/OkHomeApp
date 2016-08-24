package id.co.okhome.okhomeapp.view.fragment.makereservation.periodic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChooseCleaningPeriodFragment extends Fragment {


    @BindView(R.id.fragmentMakeReservationChoosePeriod_llEveryWeek)
    LinearLayout llEveryWeek;

    int weekViewWidth = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_period, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    private void calcViewSize(){
        getView().post(new Runnable() {
            @Override
            public void run() {
                weekViewWidth = llEveryWeek.getWidth();
                onFinishCalcViewSize();
            }
        });
    }

    private void onFinishCalcViewSize(){
        llEveryWeek.getLayoutParams().height = weekViewWidth / 7;
    }

}
