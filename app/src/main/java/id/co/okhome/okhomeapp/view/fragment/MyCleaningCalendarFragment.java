package id.co.okhome.okhomeapp.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.CalendarController;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MyCleaningCalendarFragment extends Fragment {

    CalendarController calendarController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_cleaning_calendar, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        calendarController = CalendarController.with(this).initCalendar();
    }



}
