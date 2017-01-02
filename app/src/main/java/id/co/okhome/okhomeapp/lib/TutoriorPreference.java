package id.co.okhome.okhomeapp.lib;

import android.content.Context;

/**
 * Created by josong on 2016-12-24.
 */

public class TutoriorPreference {
    public static final String TUTORIORKEY_SPECIALCLEANING = "TUTORIORKEY_SPECIALCLEANING";
    public static final String[] arrKeys = new String[]{TUTORIORKEY_SPECIALCLEANING};
    private static Context context;

    //applicationContext참조
    public static void setContext(Context context){
        TutoriorPreference.context = context;
    }

    public static final void chkRead(String key){
        JoSharedPreference.with(context).push(key, "Y");
    }

    public static final boolean isFirst(String key){
        if(JoSharedPreference.with(context).get(key) == null){
            return true;
        }else{
            return false;
        }
    }

    public static final void clear(){
        for(String key : arrKeys){
            JoSharedPreference.with(context).push(key, null);
        }

    }
}
