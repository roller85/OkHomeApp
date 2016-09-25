package id.co.okhome.okhomeapp.model;

/**
 * Created by josongmin on 2016-09-14.
 */
public class CleaningScheduleModel {
    public String id;
    public String userId, homeId, datetime, cleaningType;
    public int price;
    public String periodicYN = "N";
    public String extraCleaningIds = "";

    public String yyyymmdd(){
        String yyyymmdd = datetime.substring(0, 10).replace("-", "");
        return yyyymmdd;
    }
}
