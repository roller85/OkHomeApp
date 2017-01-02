package id.co.okhome.okhomeapp;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {




    @Test
    public void sdfdasf() throws Exception {
        int year = 2017;
        int month = 1;

        LocalDate targetDate = new LocalDate().withYear(year).withMonthOfYear(month).withDayOfMonth(1).minusWeeks(1).withDayOfWeek(DateTimeConstants.SUNDAY);
        for(int i = 0; i < 42; i++){
            targetDate = targetDate.plusDays(1);
            DayModel dayModel = new DayModel(
                    targetDate.getYear(), targetDate.getMonthOfYear(), targetDate.getDayOfMonth(), targetDate.getDayOfWeek(),
                    targetDate.toDate().getTime());

            String yyyymmdd = dayModel.yyyymmdd;
            //yyyymmdd만들어서 키로 보관

            System.out.println(yyyymmdd);
        }
    }

}