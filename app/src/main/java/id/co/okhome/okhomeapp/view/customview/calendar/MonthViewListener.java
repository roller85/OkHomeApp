package id.co.okhome.okhomeapp.view.customview.calendar;

/**
 * Created by josongmin on 2016-10-03.
 */

public interface MonthViewListener{
    public void onMonthSelected(int year, int month, MonthGridView monthGridView);
    public void onDayClick(int position, int week, DayModel dayModel);
}