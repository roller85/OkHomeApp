package id.co.okhome.okhomeapp.view.fragment.makereservation.flow;

/**
 * Created by josongmin on 2016-09-07.
 */

public interface MakeCleaningReservationFlow {
    public void onCurrentPage(int pos, MakeCleaningReservationParam params);
    public boolean next(MakeCleaningReservationParam params);
}
