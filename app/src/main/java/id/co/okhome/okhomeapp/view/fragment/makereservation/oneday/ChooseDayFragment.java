package id.co.okhome.okhomeapp.view.fragment.makereservation.oneday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;

import java.util.List;
import java.util.Map;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.CalendarController;
import id.co.okhome.okhomeapp.lib.dialogplus.DialogController;
import id.co.okhome.okhomeapp.lib.dialogplus.ViewDialog;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarMonthView;
import id.co.okhome.okhomeapp.view.customview.calendar.dayview.CalendarDayView;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayType2Model;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningTimeDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChooseDayFragment extends Fragment implements CalendarMonthView.OnDayClickListener<CalendarDayType2Model>{

    CalendarDayView dayViewPrev = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_choose_day, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        CalendarController.with(this)
                .setCalendarType(CalendarMonthView.CalendarType.TYPE2)
                .setOnDayClickListner(this)
                .initCalendar();
    }


    @Override
    public void onDayClick(final List<CalendarDayView> listCalendarDayView, final CalendarDayType2Model dayModel
            , final String year, final String month, final String day) {

        DialogController.showBottomDialog(getContext(), new ChooseCleaningTimeDialog(year, month, day, dayModel.time).setCommonCallback(new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(DialogPlus dialogPlus, Map<String, Object> params) {
                String time = (String)params.get("TIME");

                if(dayViewPrev != null){
                    ((CalendarDayType2Model)dayViewPrev.getDayModel()).time = null;
                    dayViewPrev.refresh();
                }

                for(CalendarDayView dv : listCalendarDayView){
                    if(dv.getDayModel() == dayModel){
                        ((CalendarDayType2Model)dv.getDayModel()).time = time;
                        dayViewPrev = dv;
                    }else{
                        ((CalendarDayType2Model)dv.getDayModel()).time = null;
                    }
                    dv.refresh();
                }

                dialogPlus.dismiss();
            }
        }));


    }
}
