package id.co.okhome.okhomeapp.model;

import java.util.List;

/**
 * Created by josongmin on 2016-09-14.
 */
public class CleaningScheduleModel {

    public String
            rownum, id, cTicketId, orderNo, userId, homeId, managerId, basicCleaningPrice, cleaningType,
            status, spcCleaningIds, when, duration, insertDate, whenDate, expiryDate;

    public List<SpcCleaningModel> listExtraCleaning = null;

    public String yyyymmdd(){
        String yyyymmdd = when.substring(0, 10).replace("-", "");
        return yyyymmdd;
    }
}
