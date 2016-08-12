package id.co.okhome.okhomeapp.lib;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by josongmin on 2016-07-29.
 */

public class Util {

    /**dp를 픽셀로받기*/
    public static int getPixelByDp(Context context, int dp){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());

        return (int)px;
    }

    /**메모리 주소*/
    public static final String getMemoryAddress(Object obj){
        return obj.getClass().getName() + '@' + Integer.toHexString(obj.hashCode());
    }

    /**숫자받아서 format카운트만큼 0으로 채워넣기*/
    public static final String fillupWithZero(int decimal, String format){
        return fillupWith(decimal, format, "0");
    }

    /**숫자받아서 format카운트만큼 character로 채워넣기*/
    public static final String fillupWith(int decimal, String format, String character){
        String target = decimal + "";

        while(target.length() < format.length()){
            target = character + target;
        }

        return target;
    }

    /**맵 만들기. 짝수로 넘겨야함*/
    public static Map<String, Object> makeMap(Object ... objs){
        Map<String, Object> params = new HashMap<String, Object>();

        for(int i = 0; i < objs.length; i+=2){
            params.put((String)objs[i], objs[i+1]);
        }

        return params;
    }

    /**현재 년도 가져오기*/
    public static final int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**현재 월 가져오기*/
    public static final int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }

    /**토스트 띄우기*/
    public static final void showToast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /** 뷰페이저 스와이핑 막기*/
    public static final void disableViewPagerSwiping(ViewPager vp){
        vp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return true;
            }
        });

    }

    /**화폐숫자 가져오기*/
    public static final String getMoneyString(String str){
        return getMoneyString(str, ',');
    }

    /**화폐숫자 가져오기*/
    public static final String getMoneyString(String str, char ch){

        boolean isMinus = false;
        if(str.contains("-")){
            isMinus = true;
            str = str.replace("-", "");
        }


        try{
            int commaCount = str.length() / 3;
            int remainder = str.length() % 3;
            if(remainder == 0){
                commaCount --;
            }

            System.out.println(commaCount  + "");

            char[] strParsed = new char[str.length() + commaCount];
            for(int i = str.length() - 1 , count = 1, z = strParsed.length - 1; z >= 0; z--, count++){

                if(count % 4 == 0 && count != 0){
                    strParsed[z] = ch;
                }else{
                    strParsed[z] = str.charAt(i);
                    i--;
                }
            }
            String s = new String(strParsed);

            if(isMinus){
                s = "-"+s;
            }

            return s;
        }catch (Exception e) {
            return "-";
        }
    }
}
