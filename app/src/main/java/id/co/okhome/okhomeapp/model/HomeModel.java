package id.co.okhome.okhomeapp.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josongmin on 2016-08-26.
 */
public class HomeModel {

    public final static String HOMETYPE_STUDIO = "HOMETYPE_STUDIO";
    public final static String HOMETYPE_HOUSE = "HOMETYPE_HOUSE";
    public final static String HOMETYPE_APT = "HOMETYPE_APT";

    public final static String HOMESIZE_50LESS= "HOMESIZE_50LESS";
    public final static String HOMESIZE_50_100= "HOMESIZE_50_100";

    public final static String HOMESIZE_100LESS = "HOMESIZE_100_200";
    public final static String HOMESIZE_100_150 = "HOMESIZE_100_150";
    public final static String HOMESIZE_150_200 = "HOMESIZE_150_200";
    public final static String HOMESIZE_200_250 = "HOMESIZE_200_250";
    public final static String HOMESIZE_250_300 = "HOMESIZE_250_300";

    public final static String HOMESIZE_250MORE= "HOMESIZE_250MORE";
    public final static String HOMESIZE_300MORE = "HOMESIZE_300MORE";
    public final static String HOMESIZE_DONTKNOW = "HOMESIZE_DONTKNOW";

    public final static String HOMEPET_NOPE = "HOMEPET_NOPE";
    public final static String HOMEPET_ETC = "HOMEPET_ETC";
    public final static String HOMEPET_DOG = "HOMEPET_DOG";
    public final static String HOMEPET_CAT = "HOMEPET_CAT";
    public final static String COUNT_4MORE = "COUNT_4MORE";
    public final static String COUNT_3 = "COUNT_3";
    public final static String COUNT_2 = "COUNT_2";
    public final static String COUNT_1 = "COUNT_1";
    public final static String COUNT_0 = "COUNT_0";
    //체크박스 아이템들 구분용
    public final static String TAG_HOMESIZE_STUDIO = "TAG_HOMESIZE_STUDIO";
    public final static String TAG_HOMESIZE_HOME_APT = "TAG_HOMESIZE_HOME_APT";
    public final static String TAG_ROOMCOUNT = "TAG_ROOMCOUNT";
    public final static String TAG_TOILETCOUNT = "TAG_TOILETCOUNT";
    public final static String TAG_HOMETYPE = "TAG_HOMETYPE";
    public final static String TAG_PET = "TAG_PET";

    private final static int PRICE_PER_HOUR = 80000;
    private final static int PRICE_PER_HOUR_MOVEIN = 100000;

    public String id = "", userId = "", type = "", homeSize = "", roomCnt = "", restroomCnt = "", pets = "", address1 = "", address2 = "", address3 = "", address4 = "",
            addressSeqs, insertDate = "";

    public static float getCleaningHour(String homeType, String homeSize, String roomCount, String restroomCount){
        //집 크기가 있으면 그걸로 사이즈 추산.
        Map<String, Float> map = getCleaningDetail(homeType, homeSize, roomCount, restroomCount);
        float hour = map.get("hour");

        return hour;
    }

    public static int getCleaningPrice(String homeType, String homeSize, String roomCount, String restroomCount){
        //집 크기가 있으면 그걸로 사이즈 추산.
        Map<String, Float> map = getCleaningDetail(homeType, homeSize, roomCount, restroomCount);
        float price = map.get("price");

        return (int)price;
    }

    public static float getCleaningHourMoveIn(String homeType, String homeSize, String roomCount, String restroomCount){
        //집 크기가 있으면 그걸로 사이즈 추산.
        Map<String, Float> map = getCleaningDetailMoveIn(homeType, homeSize, roomCount, restroomCount);
        float hour = map.get("hour");

        return hour;
    }

    public static int getCleaningPriceMoveIn(String homeType, String homeSize, String roomCount, String restroomCount){
        //집 크기가 있으면 그걸로 사이즈 추산.
        Map<String, Float> map = getCleaningDetailMoveIn(homeType, homeSize, roomCount, restroomCount);
        float price = map.get("price");

        return (int)price;
    }

    private static Map<String, Float> getCleaningDetail(String homeType, String homeSize, String roomCount, String restroomCount){
        float price = 0;
        float hour = 0;
        boolean knowSize = true;
        if(homeType.equals(HOMETYPE_STUDIO)){
            if(homeSize.equals(HOMESIZE_50LESS)){
                hour = 3;
            }else if(homeSize.equals(HOMESIZE_50_100)){
                hour = 4;
            }else{
                knowSize = false;
                //몰라용일경우..
            }
        }
        else if(homeType.equals(HOMETYPE_APT) || homeType.equals(HOMETYPE_HOUSE)){
            if(homeSize.equals(HOMESIZE_100LESS)){
                hour = 4;
            }
            else if(homeSize.equals(HOMESIZE_100_150)){
                hour = 5;
            }
            else if(homeSize.equals(HOMESIZE_150_200)){
                hour = 5.5f;
            }
            else if(homeSize.equals(HOMESIZE_200_250)){
                hour = 6;
            }
            else if(homeSize.equals(HOMESIZE_250MORE)){
                hour = 6.5f;
            }
            else if(homeSize.equals(HOMESIZE_300MORE)){
                hour = 7f;
            }
            else{
                knowSize = false;
                //몰라용일경우..
            }
        }


        if(!knowSize){
            //방개수, 화장실개수로 측정
            int iroomCount = 0;
            int irestroomCount = 0;
            if(roomCount.equals(COUNT_1)){
                iroomCount = 1;
            }else if(roomCount.equals(COUNT_2)){
                iroomCount = 2;
            }else if(roomCount.equals(COUNT_3)){
                iroomCount = 3;
            }else if(roomCount.equals(COUNT_4MORE)){
                iroomCount = 4;
            }

            if(restroomCount.equals(COUNT_1)){
                irestroomCount = 1;
            }else if(restroomCount.equals(COUNT_2)){
                irestroomCount = 2;
            }else if(restroomCount.equals(COUNT_3)){
                irestroomCount = 3;
            }else if(restroomCount.equals(COUNT_4MORE)){
                irestroomCount = 4;
            }

            int sum = irestroomCount + iroomCount;
            if(sum <= 0){
                hour = 3;
            }
            else if(sum >= 6){
                hour = 7;
            }else if(sum >= 5){
                hour = 6.5f;
            }else if(sum >= 4){
                hour = 6;
            }else if(sum >= 3){
                hour = 5.5f;
            }else if(sum >= 2){
                hour = 5;
            }else if(sum >= 1){
                hour = 4;
            }

        }
        price = (int)((float)PRICE_PER_HOUR * hour);

        Map<String, Float> map = new HashMap<>();
        map.put("price", price);
        map.put("hour", hour);

        return map;
    }

    private static Map<String, Float> getCleaningDetailMoveIn(String homeType, String homeSize, String roomCount, String restroomCount){
        float price = 0;
        float hour = 0;
        boolean knowSize = true;
        if(homeType.equals(HOMETYPE_STUDIO)){
            if(homeSize.equals(HOMESIZE_50LESS)){
                hour = 3;
            }else if(homeSize.equals(HOMESIZE_50_100)){
                hour = 4;
            }else{
                knowSize = false;
                //몰라용일경우..
            }
        }
        else if(homeType.equals(HOMETYPE_APT) ){
            if(homeSize.equals(HOMESIZE_100LESS)){
                hour = 5;
            }
            else if(homeSize.equals(HOMESIZE_100_150)){
                hour = 6;
            }
            else if(homeSize.equals(HOMESIZE_150_200)){
                hour = 6.5f;
            }
            else if(homeSize.equals(HOMESIZE_200_250)){
                hour = 7;
            }
            else if(homeSize.equals(HOMESIZE_250MORE)){
                hour = 7f;
            }
            else if(homeSize.equals(HOMESIZE_300MORE)){
                hour = 7f;
            }
            else{
                knowSize = false;
                //몰라용일경우..
            }
        }
        else if(homeType.equals(HOMETYPE_HOUSE) ){
            if(homeSize.equals(HOMESIZE_100LESS)){
                hour = 5.5f;
            }
            else if(homeSize.equals(HOMESIZE_100_150)){
                hour = 6.5f;
            }
            else if(homeSize.equals(HOMESIZE_150_200)){
                hour = 7f;
            }
            else if(homeSize.equals(HOMESIZE_200_250)){
                hour = 7.5f;
            }
            else if(homeSize.equals(HOMESIZE_250MORE)){
                hour = 7.5f;
            }
            else if(homeSize.equals(HOMESIZE_300MORE)){
                hour = 7.5f;
            }
            else{
                knowSize = false;
                //몰라용일경우..
            }
        }

        if(!knowSize){
            //방개수, 화장실개수로 측정
            int iroomCount = 0;
            int irestroomCount = 0;
            if(roomCount.equals(COUNT_1)){
                iroomCount = 1;
            }else if(roomCount.equals(COUNT_2)){
                iroomCount = 2;
            }else if(roomCount.equals(COUNT_3)){
                iroomCount = 3;
            }else if(roomCount.equals(COUNT_4MORE)){
                iroomCount = 4;
            }

            if(restroomCount.equals(COUNT_1)){
                irestroomCount = 1;
            }else if(restroomCount.equals(COUNT_2)){
                irestroomCount = 2;
            }else if(restroomCount.equals(COUNT_3)){
                irestroomCount = 3;
            }else if(restroomCount.equals(COUNT_4MORE)){
                irestroomCount = 4;
            }

            int sum = irestroomCount + iroomCount;
            if(sum <= 0){
                hour = 3;
            }
            else if(sum >= 6){
                hour = 7;
            }else if(sum >= 5){
                hour = 6.5f;
            }else if(sum >= 4){
                hour = 6;
            }else if(sum >= 3){
                hour = 5.5f;
            }else if(sum >= 2){
                hour = 5;
            }else if(sum >= 1){
                hour = 4;
            }

        }
        price = (int)((float)PRICE_PER_HOUR * hour);

        Map<String, Float> map = new HashMap<>();
        map.put("price", price);
        map.put("hour", hour);

        return map;
    }
}
