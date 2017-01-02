package id.co.okhome.okhomeapp.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.okhome.okhomeapp.config.Variables;

/**
 * Created by josongmin on 2016-07-29.
 */

public class Util {

    public final static String getCurrentLang(Context context){
        Locale systemLocale = context.getResources().getConfiguration().locale;
        String strLanguage = systemLocale.getLanguage();
        return strLanguage;
    }

    public static int getScreenHeight(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        return height;
    }
    public static int getScreenWidth(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        return width;
    }


    /**포맷형태로 날짜 더해서 변환*/
    public static final String getCurrentDateSimpleTypeWithAdd(int calendarField, int value){
        try{
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(calendarField, value);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(c.getTime());
            return date;
        }catch(Exception e){
            return null;
        }
    }

    /**포맷형태로 날짜 더해서 변환*/
    public static final String getCurrentDateWithAdd(String formatString, int calendarField, int value){
        try{
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(calendarField, value);

            SimpleDateFormat format = new SimpleDateFormat(formatString);
            String date = format.format(c.getTime());
            return date;
        }catch(Exception e){
            return null;
        }
    }

    /**포맷형태로 날짜 변환*/
    public static final String getCurrentDate(String formatString){
        try{
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            String date = format.format(System.currentTimeMillis());
            return date;
        }catch(Exception e){
            return null;
        }
    }

    /**yyyy-MM-dd 형태로 날짜 변환*/
    public static final String getCurrentDateSimpleType(){
        return getCurrentDate("yyyy-MM-dd");
    }

    /**날짜형식 변경*/
    public static final String getPatternedTimeString(String dateString, String patternFrom, String patternDest){

        try{
            SimpleDateFormat formatFrom = new SimpleDateFormat(patternFrom);
            SimpleDateFormat formatDest = new SimpleDateFormat(patternDest);

            return formatDest.format(formatFrom.parse(dateString).getTime());
        }catch(Exception e){
            return "err";
        }
    }

    /**pivot데이트 기준으로 targetDate가 몇번재 주인지 가져오기*/
    public static final int getWeekFromSpecificDate(String pivotDate, String targetDate) throws ParseException {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date datePivot = dateFormat.parse(pivotDate);
        Date dateTarget = dateFormat.parse(targetDate);

        Calendar cPivot = Calendar.getInstance();
        cPivot.setTime(datePivot);
        cPivot.set(Calendar.HOUR, 0);
        cPivot.set(Calendar.MINUTE, 0);
        cPivot.set(Calendar.SECOND, 0);

        Calendar cTarget = Calendar.getInstance();
        cTarget.setTime(dateTarget);
        cTarget.set(Calendar.HOUR, 0);
        cTarget.set(Calendar.MINUTE, 0);
        cTarget.set(Calendar.SECOND, 0);


        //시작일은 무슨요일
        int dayOfWeekPivot = cPivot.get(Calendar.DAY_OF_WEEK);
        int dayOfWeekTarget = cTarget.get(Calendar.DAY_OF_WEEK);

        long diff = cTarget.getTimeInMillis() - cPivot.getTimeInMillis();

        Calendar cDiff = Calendar.getInstance();
        cDiff.setTimeInMillis(diff);

        int diffDay = (int)(diff / 1000 / 60 / 60 / 24);
        int weekFromDay = ((dayOfWeekPivot-1) + diffDay) / 7;

        return weekFromDay;
    }

    public static String getFull2Decimal(int decimal){
        if(decimal < 10){
            return "0" + decimal;
        }else{
            return decimal + "";
        }
    }

    /**1개월을 42일로 나누어서 전, 후 월 일부도 다 가져옴*/
    public static final Map<String, String> getMonthRange42(int year, int month){
        String startDate = "", endDate = "";

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//Sunday is first day of week in this sample

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);//Get day of the week in first day of this month

        cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);//Move to first day of first week

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        startDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 41);
        endDate = dateFormat.format(cal.getTime());
        //
        return Util.makeStringMap("startDate", startDate, "endDate", endDate);
    }

    /**프래그먼트 파라미터랑 같이 생성*/
    public final static<T> T makeFragmentInstance(Class<T> fragmentClass, Map<String, Object> params){
        try{

            Constructor[] allConstructors = fragmentClass.getDeclaredConstructors();
            Util.Log("makeFragmentInstance constructor : " + allConstructors.length);
            T f = (T)allConstructors[0].newInstance();


            Bundle bundle = new Bundle();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if(value instanceof Integer){
                    bundle.putInt(key, (int)value);
                }
                else if(value instanceof String){
                    bundle.putString(key, (String)value);
                }
                else if(value instanceof Float){
                    bundle.putFloat(key, (Float)value);
                }
            }

            ((Fragment)f).setArguments(bundle);
            return f;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static final boolean isEmpty(String value){
        if(value == null || value.equals("")){
            return true;
        }else{
            return false;
        }
    }

    /**앱 버전 갖고오기*/
    public final static String getAppVersionName(Activity context) {

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }

    }


    public final static void openMarketIntent(Context context, String packageName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        context.startActivity(intent);
    }

    public final static void openWhatsAppMessageIntent(Context context, String phone, String subject, String text){
        try{
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            PackageInfo info= context.getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            waIntent.setPackage("com.whatsapp");
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            context.startActivity(Intent.createChooser(waIntent, "Share with"));

            Uri uri = Uri.parse("smsto:" + phone);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            context.startActivity(Intent.createChooser(i, ""));
        }catch(Exception e){
            openMarketIntent(context, "com.whatsapp");
        }
    }

    public final static void openLineMessageIntent(Context context, String phone, String subject, String text){
        try{
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            PackageInfo info= context.getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            waIntent.setPackage("com.whatsapp");
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            context.startActivity(Intent.createChooser(waIntent, "Share with"));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setPackage("jp.naver.line.android");

            context.startActivity(intent);
        }catch(Exception e){
            openMarketIntent(context, "jp.naver.line.android");
        }
    }
    public final static void openKakaoTalkMessageIntent(Context context, String phone, String subject, String text){
        try{
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            PackageInfo info= context.getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            waIntent.setPackage("com.whatsapp");
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            context.startActivity(Intent.createChooser(waIntent, "Share with"));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setPackage("com.kakao.talk");

            context.startActivity(intent);
        }catch(Exception e){
            openMarketIntent(context, "com.kakao.talk");
        }
    }

    public final static void openEmailIntent(Context context, String email, String subject, String text){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+email));
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(i);
    }

    public final static void openWebIntent(Context context, String url){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(i);
    }

    public static void openPhoneDialIntent(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public final static String getFormattedDateString(String date, String format){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time = dateFormat.parse(date).getTime();

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(format, Locale.KOREAN);
            String s = dateFormat2.format(time);
            return s;
        }catch(Exception e){
            return e.toString();
        }
    }

    public final static String changeDatetimeFormat(String date, String formatFrom, String formatTo, Locale locale){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatFrom);
            long time = dateFormat.parse(date).getTime();

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(formatTo, locale);
            String s = dateFormat2.format(time);
            return s;
        }catch(Exception e){
            return e.toString();
        }
    }

    public final static String getCurrentDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }

    //**이메일 정규식체크*/
    public final static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if( m.matches() ) {
            err = true;
        }
        return err;
    }

    /**맵값을 캐스팅없이 가져옴*/
    public static <T> T getMapValue(Map map, String key){
        return (T)map.get(key);
    }

    /**로그 뿌리기*/
    public static final void Log(Object msg){
        Log.d(Variables.LOGTAG, msg.toString());
    }

    /**etbox 키보드 컨트롤*/
    public static void setSoftKeyboardVisiblity( EditText etTarget, boolean on){
        if(on == true){
            InputMethodManager imm = (InputMethodManager)etTarget.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etTarget, InputMethodManager.SHOW_FORCED);
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }else{
            InputMethodManager imm = (InputMethodManager)etTarget.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etTarget.getWindowToken(), 0);
        }
    }

    /**키보드 숨기기*/
    public static final void hideKeyboard(Activity act){
        try{
            InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch(Exception e){
            ;
        }

    }

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

    public static final String fillupWith2Zero(String decimal){
        return fillupWith(Integer.parseInt(decimal), "XX", "0");
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

    public static Map<String, String> makeStringMap(String ... objs){
        Map<String, String> params = new HashMap<String, String>();

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
    public static final void showToast(Context context, String msg){
        if(context != null) Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /** 뷰페이저 스와이핑 막기*/
    public static final void disableViewPagerSwiping(ViewPager vp){
        vp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /** 뷰페이저 스와이핑 막기*/
    public static final void ableViewPagerSwiping(ViewPager vp){
        vp.setOnTouchListener(null);
    }

    /**화폐숫자 가져오기*/
    public static final String getMoneyString(String str){
        return getMoneyString(str, ',');
    }

    public static final String getMoneyString(int money, char ch){
        return getMoneyString(money + "", ch);
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
