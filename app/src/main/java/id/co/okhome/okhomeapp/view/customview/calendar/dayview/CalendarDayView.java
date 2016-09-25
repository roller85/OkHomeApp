package id.co.okhome.okhomeapp.view.customview.calendar.dayview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.Calendar;

import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayModel;

/**
 * View to display a day
 * @author Brownsoo
 *
 */
public abstract class CalendarDayView extends RelativeLayout {

    private CalendarDayModel dayModel;

    public CalendarDayView(Context context) {
        super(context);
        init(context);
    }

    public CalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
 
    private void init(Context context){
        initView(context);
        dayModel = new CalendarDayModel();
    }

    public void setDay(int year, int month, int day) {
        this.dayModel.cal.set(year, month, day);
    }

    public void setDay(Calendar cal) {
        this.dayModel.setDay((Calendar) cal.clone());
    }

    public void setDay(CalendarDayModel dayModel) {
        this.dayModel = dayModel;
    }

    public CalendarDayModel getDayModel() {
        return dayModel;
    }

    public int get(int field) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return dayModel.get(field);
    }

    public abstract void initView(Context context);
    public abstract void refresh();
    public abstract void clear();
}