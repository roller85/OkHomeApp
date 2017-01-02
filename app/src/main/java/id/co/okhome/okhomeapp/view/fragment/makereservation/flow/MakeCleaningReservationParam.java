package id.co.okhome.okhomeapp.view.fragment.makereservation.flow;

import java.util.Map;

/**
 * Created by josongmin on 2016-09-08.
 */

public class MakeCleaningReservationParam {

    public final static String PERIODIC_1DAY_WEEK = "1W";
    public final static String PERIODIC_DAYS_WEEK = "1W";
    public final static String PERIODIC_1DAY_2WEEK = "2W";
    public final static String PERIODIC_1DAY_3WEEK = "3W";
    public final static String PERIODIC_1DAY_4WEEK = "4W";
    public final static String PERIODIC_NOCHOICE = "0";

    //step1
    public String periodType = "", periodValue = "";
    public boolean isMultiDaysInPeriod = false;

    //step2
    public String periodStartDate = "";

    //step3 회원정보, 집정보는 실시간으로 전체 정보 변경됨. 그래서 넘길게 없다

    //step4
    public float optCleaningHour = 0;
    public int optCleaningPrice = 0;

    //step5
    public boolean isJustPeriod = true; //false면 멀티신청임. period는 딱딱주기맞는거
    public int cleaningCount = 0, totalBasicCleaningPrice = 0, totalExtraCleaningPrice = 0;
    public String spcCleaningIdsSets = "", cleaningNumbersForSpc = ""; //청소 번호 및 스페셜정보

    //최종
    public int totalPrice = 0, totalDuration = 0;
    public String orderTitle = "";
    public String orderNo = "";
    public String orderName = "";

    public static final String makeOrderNo(){
        long time = System.currentTimeMillis();
        return "OKHOME_" + time;
    }

    //////하루청소용
    //step1
    public Map<String, String> mapCleaningDateTime = null;
    public String cleaningDateTimes;
    public String cleaningType = "";

}
