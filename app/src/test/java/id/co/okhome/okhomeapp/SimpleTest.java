package id.co.okhome.okhomeapp;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * Created by josong on 2016-12-22.
 */

public class SimpleTest {

    @Test
    public void test(){
        System.out.println("z");
        int year = 2017;
        int month = 1;

        LocalDate targetDate = new LocalDate().withYear(year).withMonthOfYear(month).withDayOfMonth(1).minusWeeks(1).withDayOfWeek(DateTimeConstants.SUNDAY);
        for(int i = 0; i < 42; i++){
            targetDate = targetDate.plusDays(1);

            //yyyymmdd만들어서 키로 보관

            System.out.println(targetDate.dayOfMonth());
        }
    }

}
