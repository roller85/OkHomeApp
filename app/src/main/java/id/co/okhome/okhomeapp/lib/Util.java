package id.co.okhome.okhomeapp.lib;

import java.util.Calendar;

/**
 * Created by josongmin on 2016-07-29.
 */

public class Util {

    public static final String fillupWithZero(int decimal, String format){
        return fillupWith(decimal, format, "0");
    }
    public static final String fillupWith(int decimal, String format, String character){
        String target = decimal + "";

        while(target.length() < format.length()){
            target = character + target;
        }

        return target;
    }

    public static final int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static final int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }


}
