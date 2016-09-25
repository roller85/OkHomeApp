package id.co.okhome.okhomeapp.view.fragment.makereservation.flow;

/**
 * Created by josongmin on 2016-09-08.
 */

public class MakeReservationParam {
    public final static String TYPE_NORMAL_CLEANING = "1";
    public final static String TYPE_MOVE_IN_CLEANING = "2";
    public final static String TYPE_PERIODIC_CLEANING = "9";
    public final static int BASIC_CLEANING_PAY_PER_HOUR = 80000;
    public final static int MOVE_IN_CLEANING_PAY_PER_HOUR = 100000;


    public String type = TYPE_NORMAL_CLEANING;
    public String homeId;
    public String homeType;
    public String homeSize;
    public String floorcount;
    public String toiletCount;
    public String pet;
    public String name, phone, address1, address2, address3, address4;
    public String datetime;
    public String specialCleaingIds;
    public String cleaningDuration;
    public String consultingYN;

    //주기설정
    public String weekType;
    public String period;
    public String startDate;

    public int getDuration(){
        if(homeType == null){
            return 4;
        }
        else if(homeType.equals("1") && homeSize.equals("2")){
            return 5;
        }else{
            return 4;
        }
    }

    public int getPricePerHour(){
        int pricePerHour = 0;
        if(type.equals("ONEDAY")){
            pricePerHour = 80000;
        }else if(type.equals("MOVEIN")){
            pricePerHour = 100000;
        }else{
            pricePerHour = 0;
        }

        return pricePerHour;
    }
}
