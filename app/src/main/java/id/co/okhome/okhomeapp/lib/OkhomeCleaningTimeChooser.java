package id.co.okhome.okhomeapp.lib;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonListDialog;

/**
 * Created by josong on 2016-12-22.
 */

public class OkhomeCleaningTimeChooser {

    Activity act;
    TimeListener timeListener;
    String title;

    final String[] arrTime = new String[]{
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00"};

    final String[] arrTimeText = new String[]{
            "AM 09:00", "AM 09:30", "AM 10:00", "AM 10:30", "AM 11:00", "AM 11:30", "PM 12:00", "PM 12:30", "PM 01:00", "PM 01:30", "PM 02:00"};

    final Map<String, String> mapValues = new HashMap();

    public OkhomeCleaningTimeChooser(Activity act, TimeListener timeListener) {
        init(act, timeListener);
    }

    public OkhomeCleaningTimeChooser(Activity act, String title, TimeListener timeListener) {
        this.title = title;
        init(act, timeListener);

    }

    private void init(Activity act, TimeListener timeListener ){
        this.act = act;
        this.timeListener = timeListener;
        for(int i = 0; i < arrTime.length; i++){
            mapValues.put(arrTimeText[i], arrTime[i]);
        }
    }


    public void show(){
        DialogController.showListDialog(act, title != null ? title : "청소 시작 시간을 선택하세요", arrTimeText, (int)(Util.getScreenHeight(act) / 1.5), new ViewDialog.DialogCommonCallback(){
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String item = (String)params.get(CommonListDialog.TAG_ITEM);
                String value = mapValues.get(item);

                timeListener.onTimeChoosed(item, value);

            }
        });
    }

    public interface TimeListener{
        public void onTimeChoosed(String time, String timeValue);
    }
}
