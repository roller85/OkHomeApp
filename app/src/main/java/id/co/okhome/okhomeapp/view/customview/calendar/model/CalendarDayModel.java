package id.co.okhome.okhomeapp.view.customview.calendar.model;

import java.util.Calendar;


/**
 * Value object for a day
 * @author brownsoo
 *
 */
public class CalendarDayModel {

    public Calendar cal;
    private CharSequence msg = "";

    public boolean isCurrentMonth = false;
    public boolean isCleaningDay = false;

    public CalendarDayModel() {
        this.cal = Calendar.getInstance();
    }
    
    public void setDay(int year, int month, int day) {
        cal = Calendar.getInstance();
        cal.set(year, month, day);
    }

    public void setDay(Calendar cal) {
        this.cal = (Calendar) cal.clone();
    }

    public int get(int field) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return cal.get(field);
    }


}