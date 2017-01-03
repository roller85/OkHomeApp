package id.co.okhome.okhomeapp.view.customview.calendar;

import org.joda.time.LocalDate;

import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.CleaningScheduleModel;

/**
 * Created by josongmin on 2016-09-25.
 */

public class DayModel {
    public int year, month, day;
    public int dayOfWeek; //5토 6일
    public long timemill;
    public String yyyymmdd = null;


    //예정일, 시작일, Reserved
    public boolean optReserved = false; //하루 예약시 예약된 날짜미리 보이기
    public boolean optBeginDay = false; //주기예약시 시작일
    public boolean optPlanned = false; //주기예약 진행시 미리 보일날
    public String optBeginTime = null; //주기예약 진행시 미리 보일날
    public boolean optAbleReservationForPeriod = false;

    public boolean startMove = false; //이동전

    public CleaningScheduleModel cleaningScheduleModel = null;

    public DayModel(int year, int month, int day, int dayOfWeek, long timemill) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.timemill = timemill;
        this.yyyymmdd = year + Util.fillupWithZero(month, "XX") + Util.fillupWithZero(day, "XX");
    }

    //예약가능한날짜? 2일뒤부터 가능.
    public boolean isAbleToReservation(int year, int month){

        LocalDate targetDate = new LocalDate().withYear(year).withMonthOfYear(month).plusDays(2);

        if(targetDate.toDate().getTime() < timemill){
            return true;
        }else{
            return false;
        }
    }

    public void clear(){
        optBeginDay = false;
        optPlanned = false;
        optBeginTime = null;
        optReserved = false;
        cleaningScheduleModel = null;
        optAbleReservationForPeriod = false;
    }

    public void clear(String key){
        if(key.equals("optBeginTime")){
            optBeginTime = null;
        }
        else if(key.equals("optBeginDay")){
            optBeginDay = false;
        }
        else if(key.equals("cleaningScheduleModel")){
            cleaningScheduleModel = null;
        }

    }
}
