package id.co.okhome.okhomeapp.view.customview.calendar.model;

import java.util.Calendar;

/**
 * Value object for a day
 * @author brownsoo
 *
 */
public class CalendarDayType1Model extends CalendarDayModel{
    public String time;
    public boolean isStartDate = false;

    public boolean chkAbleReservation(){
        //2일뒤 정보
        Calendar c = Calendar.getInstance();
        String r = "";

        c.add(Calendar.DAY_OF_MONTH, 2);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        if(c.getTimeInMillis() < cal.getTimeInMillis()){
            return true;
        }else{
            return false;
        }
    }
}