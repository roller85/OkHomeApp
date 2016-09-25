package id.co.okhome.okhomeapp.view.fragment.makereservation.flow;

/**
 * Created by josongmin on 2016-09-07.
 */

public interface MakeReservationFlow {
    public void onCurrentPage(int pos, MakeReservationParam params);
    public boolean next(MakeReservationParam params);
}
