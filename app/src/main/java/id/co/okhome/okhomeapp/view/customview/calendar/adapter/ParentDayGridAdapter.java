package id.co.okhome.okhomeapp.view.customview.calendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import id.co.okhome.okhomeapp.view.customview.calendar.DayModel;
import id.co.okhome.okhomeapp.view.customview.calendar.MonthViewListener;

/**
 * Created by josongmin on 2016-09-29.
 */

public abstract class ParentDayGridAdapter extends BaseAdapter{
    public int colorBgCurrentMonth, colorBgNotCurrentMonth;
    public int colorTextCurrentPlainDay, colorTextCurrentSaturday, colorTextCurrentSunday, colorTextNotCurrentMonth;
    public int colorCaptionReserved, colorCaptionReservedAlpha;
    public int colorCaptionBlue, colorCaptionBlueAlpha;
    public int colorRed, colorYellow;

    public int itemHeight;
    public LayoutInflater inflater;
    public List<DayModel> listDayModel;
    public int size;
    public int year, month;

    @IdRes
    int id = 100000;

    MonthViewListener monthViewListener;

    public ParentDayGridAdapter(Context context, List<DayModel> listDayModel, int itemHeight, MonthViewListener monthViewListener) {
        this.itemHeight = itemHeight;
        this.inflater = (LayoutInflater)(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        this.listDayModel = listDayModel;
        this.size = listDayModel.size();
        this.monthViewListener = monthViewListener;

        colorBgCurrentMonth = Color.parseColor("#f9f9f9");
        colorBgNotCurrentMonth = Color.parseColor("#fdfdfd");
        colorTextCurrentSunday = Color.parseColor("#bc453f");
        colorTextCurrentSaturday = Color.parseColor("#438cd0");
        colorTextCurrentPlainDay = Color.parseColor("#454746");
        colorTextNotCurrentMonth = Color.parseColor("#e1e1e1");
        colorCaptionReserved = Color.parseColor("#b7bec8");
        colorCaptionReservedAlpha = Color.parseColor("#e2e6e9");

        colorCaptionBlue = Color.parseColor("#008fd5");
        colorCaptionBlueAlpha = Color.parseColor("#c0e3f3");

        colorRed = Color.parseColor("#fa418f");
        colorYellow = Color.parseColor("#ffda31");
    }

    public void setYearMonth(int year, int month){
        this.year = year;
        this.month = month;

    }

    @Override
    public abstract View getView(final int position, View convertView, ViewGroup parent);

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public DayModel getItem(int position) {
        return listDayModel.get(position);
    }

    @Override
    public int getCount() {
        return size;
    }
}
